package invest.com.swapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import invest.com.swapp.adapter.WatchedRecyclerAdapter
import invest.com.swapp.auth.LoginActivity
import invest.com.swapp.listener.WatchListener
import invest.com.swapp.model.StocksWatched
import invest.com.swapp.viewmodel.AuthViewModel
import invest.com.swapp.viewmodel.StocksViewModel
import kotlinx.android.synthetic.main.activity_master.*
import kotlinx.android.synthetic.main.layout_buy_sell_prompt.view.*
import kotlinx.android.synthetic.main.layout_watchlist_notification.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.anko.toast

//todo:- transfer to new master activity
// fragmented view pager2

class MasterActivity : AppCompatActivity(), WatchListener{

    //todo:- workmanager to update watchlist

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var watchListAdapter: WatchedRecyclerAdapter
    lateinit var mAdView : AdView
    var watchListener:WatchListener? = null
    val listen: MutableLiveData<List<String>> = MutableLiveData()

    /*mvvm*/
    private lateinit var stockViewModel: StocksViewModel
    private lateinit var authViewModel: AuthViewModel
    private lateinit var watchList:ArrayList<StocksWatched>
    private lateinit var pref:SharedPreferences


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_master)
        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713")

        title = ""
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_action_account)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        watchListener = this

        pref = getSharedPreferences("sharedPreferences",Context.MODE_PRIVATE)

        //Production ca-app-pub-4268048783942748~4717310066
        //Testing ca-app-pub-3940256099942544~3347511713
        //Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713


        linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerViewMain.layoutManager = linearLayoutManager

        //Uncomment the ADS
        mAdView = findViewById(R.id.adView)
        mAdView.visibility = View.GONE
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        watchList = ArrayList()
        watchListAdapter = WatchedRecyclerAdapter(watchList)
        watchListAdapter.watchListener = this
        recyclerViewMain.adapter = watchListAdapter

        /*mvvm*/
        authViewModel = ViewModelProviders.of(this).get(AuthViewModel::class.java)
        stockViewModel = ViewModelProviders.of(this).get(StocksViewModel::class.java)
        stockViewModel.watchedStocks.observe(this, Observer {
                watchList.clear()
                watchList.addAll(it)
                recyclerViewMain!!.adapter!!.notifyDataSetChanged()
                            for(watch: StocksWatched in it){
                                //todo:- do plan for logic and add stoploss logic
                                if(watch.buy_price == watch.price.toFloat()){
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
                    //todo:put this to shared preference link value
                    openBroker()

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
                    var i = Intent(baseContext, LoginActivity::class.java)
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK  or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(i)
                    finish()
                    true
                }
                android.R.id.home ->{
                    var view = layoutInflater.inflate(R.layout.layout_user_profile_account, null)
                    var txtLink = view.findViewById<TextInputEditText>(R.id.txt_broker_url)
                    var txtEmail = view.findViewById<TextView>(R.id.tv_email)

                    txtLink.setText(pref.getString("_broker",""))
                    txtEmail.text = pref.getString("email","")


                    MaterialAlertDialogBuilder(MasterActivity@this, R.style.AlertDialogTheme)
                            .setView(view)
                            .setPositiveButton("Save"){
                                dialog, which ->
                                with(pref.edit()){
                                    if(!txtLink.text.toString().contains("https://")){
                                        putString("_broker", "https://" + txtLink.text.toString())
                                    }else {
                                        putString("_broker", txtLink.text.toString())
                                    }
                                    commit()
                                }
                                toast("broker save")

                            }
                            .setNegativeButton("Open"){
                                dialog, which ->
                                openBroker()
                            }
                            .show()

                    true
                }
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

    fun openBroker(){
        val brokerLink = pref.getString("_broker","")

        if(brokerLink.isNullOrBlank() || brokerLink == "https://"){
                //check if valid url
                toast("broker settings not set")
        }else{
            val intent = Intent(this, ViewNews::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra("link", brokerLink) //get this from shared preference
            startActivity(intent)
        }
    }
}
