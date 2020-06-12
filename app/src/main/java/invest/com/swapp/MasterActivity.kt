package invest.com.swapp

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import invest.com.swapp.adapter.WatchedRecyclerAdapter
import invest.com.swapp.auth.LoginActivity
import invest.com.swapp.helper.ConnectivityManager
import invest.com.swapp.listener.WatchListener
import invest.com.swapp.model.StocksWatched
import invest.com.swapp.security.SSharedPreferenceManager
import invest.com.swapp.viewmodel.AuthViewModel
import invest.com.swapp.viewmodel.StocksViewModel
import kotlinx.android.synthetic.main.activity_master.*
import kotlinx.android.synthetic.main.layout_buy_sell_prompt.view.*
import kotlinx.android.synthetic.main.layout_watchlist_notification.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.anko.toast
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*
import kotlin.collections.ArrayList

//todo:- transfer to new master activity
// fragmented view pager2

class MasterActivity : AppCompatActivity(), WatchListener{

    //todo:- workmanager to update watchlist

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: WatchedRecyclerAdapter
    lateinit var mAdView : AdView
    var watchListener:WatchListener? = null
    val listen: MutableLiveData<List<String>> = MutableLiveData()

    /*mvvm*/
    private lateinit var stockViewModel: StocksViewModel
    private lateinit var authViewModel: AuthViewModel


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_master)
        title = ""
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_action_account)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        watchListener = this


        //Production ca-app-pub-4268048783942748~4717310066
        //Testing ca-app-pub-3940256099942544~3347511713
        //Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713

        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713")
        linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerViewMain.layoutManager = linearLayoutManager

        //Uncomment the ADS
        mAdView = findViewById(R.id.adView)
        mAdView.visibility = View.GONE
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)


        /*mvvm*/
        authViewModel = ViewModelProviders.of(this).get(AuthViewModel::class.java)
        stockViewModel = ViewModelProviders.of(this).get(StocksViewModel::class.java)
        stockViewModel.watchedStocks.observe(this, Observer {
                adapter = WatchedRecyclerAdapter(it)
                adapter.watchListener = this
                recyclerViewMain.adapter = adapter
                            for(watch: StocksWatched in it){
                                if(watch.buy_price == watch.price.toFloat()){
                                    //toast("buy price marked is reached ${watch.symbol}")
                                    //todo:do same logic as in service
                                    addToNotifyList("${watch.symbol}:${watch.price}:buy")
                                }

                                if(watch.sell_price == watch.price.toFloat()){
                                    //toast("buy price marked is reached ${watch.symbol}")
                                    addToNotifyList("${watch.symbol}:${watch.price}:sell")
                                }
                            }

                if(it.isEmpty()){
                    empty_view.visibility = View.VISIBLE
                }else{
                    empty_view.visibility = View.GONE
                }

            listen.value = notifyList

        })

        btn_search.setOnClickListener {
            startActivity(Intent(this, StockItemListActivity::class.java))
        }

        listen.observe(this, Observer {
            Log.d("listener", "count ${it.size}")
            if(it.isNotEmpty()){
                if(!notifyWatched) {
                    notifyWatchedPrice()
                }
            }
        })

    }

    var notifyList = ArrayList<String>()

    private fun addToNotifyList(notifyString: String){
        if(!notifyList.contains(notifyString)){
            notifyList.add(notifyString)
        }
    }

    var notifyWatched = false

    fun notifyWatchedPrice(){
        var view:View = layoutInflater.inflate(R.layout.layout_watchlist_notification,null)
        //todo: add this inflater to view up as notification, to clear

        view.watchlist_price_alert.text = stockViewModel.buildWatchlistAlertMessage(listen.value!!)

        var alertPrice = MaterialAlertDialogBuilder(MasterActivity@this,R.style.AlertDialogTheme).setTitle("Watchlist Alert")
                .setView(view)
                .setPositiveButton("Open Broker"){
                    dialog, _ ->
                    //call browser open same as in news opening...

                }
                .setNegativeButton("Cancel"){
                    dialog, _ ->
                    dialog.cancel()
                }

        alertPrice.setCancelable(false)

        //if(!alertPrice.create().isShowing){
            alertPrice.show()
            notifyWatched = true
        //}
    }

    override fun onResume() {
        super.onResume()
        if(!authViewModel.userRepository.isCredentialValid()){
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
            menuInflater.inflate(
                    R.menu.option_menu,
                    menu
            )
            return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
            return when(item!!.itemId){
                R.id.logout_menu -> {
                    authViewModel.logout()
                    startActivity(Intent(baseContext, LoginActivity::class.java))
                    finish()
                    true
                }
                R.id.home ->{   true    }
                R.id.news_menu -> {
                    startActivity(Intent(baseContext, NewsActivity::class.java))
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }
    }

    override fun onWatchedAction(stockWatched: StocksWatched) {
            var view:View = layoutInflater.inflate(R.layout.layout_buy_sell_prompt,null)
                    MaterialAlertDialogBuilder(MasterActivity@this,R.style.AlertDialogTheme).setTitle(stockWatched.symbol)
                    .setView(view)
                    .setPositiveButton("Save"){
                        dialog, which ->
                        var buyPrice = view.txt_buy_price.text.toString()
                        var sellPrice = view.txt_sell_price.text.toString()

                        if(buyPrice.isEmpty()){
                            buyPrice = "0"
                        }

                        if(sellPrice.isEmpty()){
                            sellPrice = "0"
                        }

                        GlobalScope.launch {
                           stockWatched.buy_price = buyPrice.toFloat()
                           stockWatched.sell_price = sellPrice.toFloat()
                           stockViewModel.updateWatched(stockWatched)
                        }

                    }
                    .setNegativeButton("Unwatched"){
                        dialog, which ->
                        GlobalScope.launch {
                            stockViewModel.deleteWatched(stockWatched)
                        }
                    }
//                    .setNeutralButton("Cancel"){
//                        dialog, _ ->
//                                    dialog.cancel()
//                            }
//                            .setCancelable(false)
                    .show()
    }

    override fun onSelectWatched(symbol: String) {
        GlobalScope.launch {
            var stock = stockViewModel.getStock(symbol)
            var i = Intent(this@MasterActivity, StockItemDetailActivity::class.java)
            i.putExtra("stockDetail", stock)
            startActivity(i)
        }
    }
}
