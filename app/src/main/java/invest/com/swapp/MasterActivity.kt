package invest.com.swapp

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.recyclerview.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
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
import invest.com.swapp.model.Stock2
import invest.com.swapp.model.StocksWatched
import invest.com.swapp.viewmodel.StocksViewModel
import kotlinx.android.synthetic.main.activity_master.*
import kotlinx.android.synthetic.main.layout_buy_sell_prompt.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*

//todo:- transfer to new master activity
// fragmented view pager2

class MasterActivity : AppCompatActivity(), WatchListener{

    //todo:- workmanager to update watchlist

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: WatchedRecyclerAdapter
    lateinit var mAdView : AdView
    var watchListener:WatchListener? = null

    /*mvvm*/
    private lateinit var stockViewModel: StocksViewModel


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

        stockViewModel = ViewModelProviders.of(this).get(StocksViewModel::class.java)
        stockViewModel.watchedStocks.observe(this, Observer {
                adapter = WatchedRecyclerAdapter(it)
                adapter.watchListener = this
                recyclerViewMain.adapter = adapter

            //todo:- move to service
            //this has issue on looping 5 times
            //                for(watch: StocksWatched in it){
            //                    if(watch.buy_price == watch.price.toFloat()){
            //                        toast("buy price marked is reached ${watch.symbol}")
            //                    }
            //
            //                    if(watch.sell_price == watch.price.toFloat()){
            //                        toast("buy price marked is reached ${watch.symbol}")
            //                    }
            //                }

                if(it.isEmpty()){
                    empty_view.visibility = View.VISIBLE
                }else{
                    empty_view.visibility = View.GONE
                }

        })

        btn_search.setOnClickListener {
            startActivity(Intent(this, StockItemListActivity::class.java))
        }

//        val handler = Handler()
//        val runnable = Runnable {
//            GlobalScope.launch {
//                stockViewModel.getStocks()
//            }
//        }
//
//
//        //        Uncomment to run on API level 23
//        //        this will be added to background service
//                var day = Date()
//                val londonZone = ZoneId.of("Asia/Manila")
//                val philLocalDate = ZonedDateTime.now(londonZone)
//
//                //toast("day is ${day.day} hour is ${day.hours}  $philLocalDate")
//                Log.d("_day","day is ${day.day} hour is ${day.hours}  ${philLocalDate.dayOfWeek}  ${philLocalDate.hour}")
//
//                //move to view model
//                //add to broadcast receiver...
//                if((!philLocalDate.equals("SUNDAY") || !philLocalDate.equals("SATURDAY"))){
//                    if(philLocalDate.hour in 7..4){
//                        //timer.start()
//                        val timer: Job = update(6000,5000){
//                            handler.post(runnable)
//                        }
//                        //check api version
//                        //timer.start()
//                    }
//                }
    }

    //time
    private inline fun update(delayMillis: Long = 0, repeatMillis: Long = 0, crossinline action: () -> Unit) = GlobalScope.launch {
        delay(delayMillis)
        if (repeatMillis > 0) {
            while (true) {
                action()
                delay(repeatMillis)
            }
        } else {
            action()
        }
    }

    override fun onResume() {
        super.onResume()
        if(checkConnectivity(this)) {

        }else{
            status_main.text = "Internet not connected."
            empty_view.visibility = View.VISIBLE
        }
    }

    private fun checkConnectivity(context: Context): Boolean {
            val cm = ConnectivityManager()
            getSystemService(Context.CONNECTIVITY_SERVICE)
            return cm.isConnectingToInternet(this)
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
                    startActivity(Intent(baseContext, LoginActivity::class.java))
                    finish()
                    true
                }

                R.id.home ->{
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
                    .setNegativeButton("Remove"){
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
}
