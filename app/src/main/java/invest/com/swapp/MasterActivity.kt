package invest.com.swapp

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import invest.com.swapp.adapter.StocksRecyclerAdapter
import invest.com.swapp.auth.LoginActivity
import invest.com.swapp.db.DBHelper
import invest.com.swapp.db.database
import kotlinx.android.synthetic.main.activity_master.*
import okhttp3.*
import org.jetbrains.anko.db.select
import org.json.JSONObject

import java.io.IOException
import java.lang.Exception


class MasterActivity : AppCompatActivity(){

    private var stockList: ArrayList<Stock> = ArrayList()
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: StocksRecyclerAdapter
    private val stockListAll = ArrayList<Stock>()
    private lateinit var client: OkHttpClient
    lateinit var mAdView : AdView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_master)
        AppHelper.init(baseContext)

        //Production ca-app-pub-4268048783942748~4717310066
        //Testing ca-app-pub-3940256099942544~3347511713
        //Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713

        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713")

        linearLayoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        recyclerViewMain.layoutManager = linearLayoutManager
        adapter = StocksRecyclerAdapter(stockList)
        recyclerViewMain.adapter = adapter


        client = OkHttpClient()
        btn_dashboard_search.setOnClickListener { doSearchStock() }


        if(checkConnectivity(this)) {
            setupRecyclerView(recyclerViewMain)
        }else{
            status_main.text = "Internet is not connected."
            empty_view.visibility = View.VISIBLE
        }

        mAdView = findViewById(R.id.adView)
        mAdView.visibility = View.GONE
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

// TODO: Add adView to your view hierarchy.

    }

    override fun onResume() {
        super.onResume()
        if(checkConnectivity(this)) {
            //setupRecyclerView(recyclerViewMain)
        }else{
            status_main.text = "Internet not connected."
            empty_view.visibility = View.VISIBLE
        }
    }


    fun checkConnectivity(context: Context): Boolean {
            val cm = ConnectivityManager()
            getSystemService(Context.CONNECTIVITY_SERVICE)
            return cm.isConnectingToInternet(this)
    }


    private fun setupRecyclerView(recyclerView: RecyclerView) {


        val urlRequest = Uri.Builder().scheme(MasterActivity.URL_SCHEME)
                .authority(MasterActivity.URL_AUTHORITY)
                .appendPath(MasterActivity.URL_PATH_1)
                .build().toString()
        val request = Request.Builder().url(urlRequest).build()
        client.newCall(request).enqueue(object : Callback{

            override fun onResponse(call: Call, response: Response) {

                if(response == null){
                   return
                }

                var r = response.body()!!.string()
                    runOnUiThread {

                        try{
                            val rootJsonObject = JSONObject(r)
                            var roots = rootJsonObject.getJSONArray("stock")
                            for (i in 0 until roots.length()) {
                                val stock = roots.get(i).toString()
                                val obj = JSONObject(stock)

                                val imageModel = Stock("${obj.getString("name")}",
                                        obj.getString("symbol"),"",
                                        obj.getString("percent_change"),
                                        obj.getString("volume"),
                                        obj.getJSONObject("price").getString("amount"))
                                stockListAll.add(imageModel)
                            }

                            filter(stockListAll)
                            Log.d("_json", rootJsonObject.toString())
                            Log.d("_json", "date: " + rootJsonObject.getString("as_of"))

                        }catch (e: Exception){
                            e.printStackTrace()
                        }
                    }
            }

            override fun onFailure(call: Call, e: IOException) {
                Log.d("_json", e.message)
            }
        })

        }

        private fun filter(stocks: ArrayList<Stock>){
            val list = ArrayList<String>()

            database.use {
                select(DBHelper.tblWatchlist,"symbol").limit(5).exec {
                    while (moveToNext()){
                        Log.d("_symbol", getString(getColumnIndex("symbol")) )
                        list.add(getString(getColumnIndex("symbol")))
                    }
                }
            }

            if(list.size > 0){
                recyclerViewMain.visibility = View.VISIBLE
                var array = arrayOfNulls<String>(list.size)
                list.toArray(array)

                Log.d("array", array.toString())

                var list = mutableListOf<Stock>()
                val filtered: List<Stock> = stocks.filter{array.contains(it.symbol)}

                Log.d("_list","${filtered.size} array size ${array!!.size} stock list ${stocks.size}" )
                //stockitem_list!!.adapter = StockItemListActivity.SimpleItemRecyclerViewAdapter(this, ArrayList(filtered), true)
                recyclerViewMain.adapter = StocksRecyclerAdapter(ArrayList(filtered))
            }else{
                empty_view.visibility = View.VISIBLE
                recyclerViewMain.visibility = View.INVISIBLE
            }
        }

        private fun fetchUpdate(symbol: String){

           val urlRequest = Uri.Builder().scheme(MasterActivity.URL_SCHEME)
                   .authority(MasterActivity.URL_AUTHORITY)
                   .appendPath("stocks")
                   .appendPath("$symbol.json")
                   .build().toString()


            Log.d("_logs","symbol $symbol $urlRequest")
            val request = Request.Builder().url(urlRequest).build()

            client.newCall(request).enqueue(object : Callback{
                override fun onResponse(call: Call, response: Response) {
                    Log.d("_log", response.body()!!.string())
                }

                override fun onFailure(call: Call, e: IOException) {
                    Log.d("_log", "$e")
                }

            })
        }

        fun doSearchStock(){
            startActivity(Intent(baseContext, StockItemListActivity::class.java))
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
                    AppHelper.userPool!!.currentUser.signOut()
                    startActivity(Intent(baseContext, LoginActivity::class.java))
//                    exitProcess(-1)
                    finish()
                    true
                }

                R.id.news_menu -> {
                    startActivity(Intent(baseContext, NewsActivity::class.java))
                    true
                }

                else -> super.onOptionsItemSelected(item)
            }
        }

        //http://phisix-api2.appspot.com/stocks/BDO.json
        companion object {
            private val URL_SCHEME = "http"
            private val URL_AUTHORITY = "phisix-api2.appspot.com"
            private val URL_PATH_1 = "stocks.json"

        }
}
