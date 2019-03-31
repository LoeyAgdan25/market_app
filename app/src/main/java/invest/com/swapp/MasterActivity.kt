package invest.com.swapp

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import invest.com.swapp.R.id.btn_dashboard_search
import invest.com.swapp.R.id.recyclerViewMain
import invest.com.swapp.adapter.RecyclerAdapter
import invest.com.swapp.auth.LoginActivity
import invest.com.swapp.db.DBHelper
import invest.com.swapp.db.database
import kotlinx.android.synthetic.main.activity_master.*
import kotlinx.coroutines.experimental.DefaultDispatcher
import kotlinx.coroutines.*
import kotlinx.coroutines.experimental.awaitAll
import okhttp3.*
import org.jetbrains.anko.db.select
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast

import java.io.IOException
import kotlin.system.exitProcess

class MasterActivity : AppCompatActivity(){

    private var stockList: ArrayList<Stock> = ArrayList()
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: RecyclerAdapter
    private val stockListAll = ArrayList<Stock>()
    private lateinit var client: OkHttpClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_master)
        AppHelper.init(baseContext)

        linearLayoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        recyclerViewMain.layoutManager = linearLayoutManager
        adapter = RecyclerAdapter(stockList)
        recyclerViewMain.adapter = adapter


        client = OkHttpClient()
        btn_dashboard_search.setOnClickListener { doSearchStock() }
        setUpRecyclerview(recyclerViewMain)




    }



    private fun setUpRecyclerview(recyclerView: RecyclerView){
        //do sqlite database
        //add grouping wait
        //TODO:- Do coroutines...
        database.use {
            select(DBHelper.tblWatchlist).exec {
                while (moveToNext()){
                    val stockModel = Stock("","${getString(getColumnIndex("symbol"))}","","","","")

                    doAsync { fetchUpdate(stockModel.symbol) }
                    stockListAll.add(stockModel)
                }
            }
        }

        //Add group wait here...

        if(stockListAll.size > 0){
            recyclerView.adapter = RecyclerAdapter(stockListAll)
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
                exitProcess(-1)
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
