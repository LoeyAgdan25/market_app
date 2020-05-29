package invest.com.swapp

import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import invest.com.swapp.db.database
import invest.com.swapp.helper.UtilityHelper
import kotlinx.android.synthetic.main.activity_invest.view.*
import kotlinx.android.synthetic.main.activity_stockitem_detail.*
import kotlinx.android.synthetic.main.content_detail_stocks.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.db.*
import org.jetbrains.anko.toast
import com.github.mikephil.charting.charts.CandleStickChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry
import invest.com.swapp.model.HistoryData
import invest.com.swapp.model.Stock
import invest.com.swapp.model.StocksWatched
import invest.com.swapp.viewmodel.StocksViewModel
import kotlinx.android.synthetic.main.watch_list_content.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.lang.reflect.InvocationTargetException
import java.util.ArrayList


class StockItemDetailActivity : AppCompatActivity() {

    private lateinit var mInterstitialAd: InterstitialAd
    private lateinit var client: OkHttpClient
    private lateinit var stockViewModel:StocksViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stockitem_detail)
        setSupportActionBar(toolbar)

        stockViewModel = ViewModelProviders.of(this).get(StocksViewModel::class.java)
        stockViewModel.historyData.observe(this, Observer {
            setupCandle2(it)
        })

        var symbol = intent.getStringExtra(StockItemDetailFragment.ARG_ITEM_SYMBOL)
        var name = intent.getStringExtra(StockItemDetailFragment.ARG_ITEM_NAME)
        var volume = intent.getStringExtra(StockItemDetailFragment.ARG_ITEM_VOLUME)
        var percent = intent.getStringExtra(StockItemDetailFragment.ARG_ITEM_PERCENTAGE)
        var price = intent.getStringExtra(StockItemDetailFragment.ARG_ITEM_PRICE)
        var status = intent.getStringExtra(StockItemDetailFragment.ARG_ITEM_STATUS)
        var companyId = intent.getIntExtra(StockItemDetailFragment.ARG_COMP_ID,0)
        var securityID = intent.getIntExtra(StockItemDetailFragment.ARG_SEC_ID,0)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = ""

        txt_stock_symbol.text = "${symbol}"
        txt_stock_percent.text = "${percent}"
        txt_stock_price.text = "${UtilityHelper.getInstance().formatCurrency(price.toDouble())}"
        txt_stock_status.text = "${status}"
        txt_stock_volume.text = "${UtilityHelper.getInstance().formatVolume(volume.toDouble())}"
        txt_company_description.text = "${name}"

        //btn_watch_stock.setOnClickListener { doWatchStock(symbol) }
        btn_watch_remove.setOnClickListener { doRemoveWatched(symbol) }
        btn_watch_invest.setOnClickListener { doInvest(symbol) }
        doFindStock(symbol)
        //doFindStockAll()
        //Production: ca-app-pub-4268048783942748/7970283074
        //Testing: ca-app-pub-3940256099942544/1033173712
        mInterstitialAd = InterstitialAd(this)
        mInterstitialAd.adUnitId = "ca-app-pub-3940256099942544/1033173712"
        mInterstitialAd.loadAd(AdRequest.Builder().build())
        client = OkHttpClient()

        GlobalScope.async {
            stockViewModel.getStockHistory(securityID,companyId)
        }

        btn_watch_stock.setOnClickListener {
           GlobalScope.async {
               stockViewModel.watched(StocksWatched(symbol,0f,0f))
           }
        }
    }

    fun doFindStockAll(){
        database.use {
            select("tblWatched").exec {
                while (moveToNext()) {
                    Log.d("_symbol", getString(getColumnIndex("symbol")) +
                            " " + getString(getColumnIndex("name"))
                            + getString(getColumnIndex("amount"))
                            + getString(getColumnIndex("volume")))
                }
            }
        }
    }

    fun doInvest(sym:String){
        var intent = Intent(this,InvestActivity::class.java).apply {
            putExtra("symbol","$sym")
        }
        startActivity(intent)
    }

    fun doRemoveWatched(sym:String){

        alert {
            title("Watchlist")
            message("Remove Stock from watchlist")
            yesButton {
                database.use {
                    delete("tblWatched","symbol = {symbol}", "symbol" to sym)
                }
                btn_watch_stock.visibility = View.VISIBLE
                btn_watch_remove.visibility = View.GONE
                txt_stock_status.text = "Unwatched"
            }

            noButton{
                //do nothing
            }
        }.show()
    }

    fun doFindStock(sym: String){
        database.use {
            select("tblWatched").where("symbol = {symbol}","symbol" to sym).limit(1).exec {
                //moveToNext()
                if(moveToFirst()){
                    txt_stock_status.text = "Watch"
                    btn_watch_stock.visibility = View.GONE
                }else{
                    txt_stock_status.text = "Unwatched"
                    btn_watch_invest.visibility = View.GONE

                }
            }
        }
    }

    fun doWatchStock(sym:String){

//        if (mInterstitialAd.isLoaded) {
//            mInterstitialAd.show()
//        }

        Log.d("event","watching stock")
            database.use {

               //check first if already exist dont add again
                select("tblWatched").where("symbol = {symbol}","symbol" to sym).limit(1).exec{
                    if(moveToFirst()){
                        toast("Stock already in the watchlist")
                    }else{
                        insert("tblWatched",
                                "symbol" to txt_stock_symbol.text,
                                "name" to intent.getStringExtra(StockItemDetailFragment.ARG_ITEM_NAME),
                                "currency" to "PHP",
                                "amount" to txt_stock_price.text,
                                "volume" to txt_stock_volume.text,
                                "status" to "watched"
                        )

                        txt_stock_status.text = "Watched"
                        btn_watch_stock.visibility = View.GONE
                    }
                }
            }
    }

    fun setupCandle2(historyData:ArrayList<HistoryData>){
        val candleStickChart = candle_stick_chart
        candleStickChart.setHighlightPerDragEnabled(true)
        candleStickChart.setBackgroundColor(Color.parseColor("#000000"))
        candleStickChart.setDrawBorders(true)
        candleStickChart.setBorderColor(resources.getColor(R.color.colorPrimary))
        val yAxis = candleStickChart.getAxisLeft()
        val rightAxis = candleStickChart.getAxisRight()
        yAxis.setDrawGridLines(false)
        rightAxis.setDrawGridLines(false)
        candleStickChart.requestDisallowInterceptTouchEvent(true)

        val xAxis = candleStickChart.getXAxis()

        xAxis.setDrawGridLines(false)// disable x axis grid lines
        xAxis.setDrawLabels(false)
        rightAxis.setTextColor(Color.WHITE)
        yAxis.setDrawLabels(true)
        xAxis.setGranularity(1f)
        xAxis.setGranularityEnabled(true)
        xAxis.setAvoidFirstLastClipping(true)

        val l = candleStickChart.getLegend()

        l.setEnabled(false)

        val yValsCandleStick = ArrayList<CandleEntry>()

        var j = 0
        for (data in historyData) {
            yValsCandleStick.add(CandleEntry(j++.toFloat(), data.high.toFloat(), data.low.toFloat(), data.open.toFloat(), data.close.toFloat()))
        }

        val set1 = CandleDataSet(yValsCandleStick, "DataSet 1")
        set1.color = Color.rgb(80, 80, 80)
        set1.shadowColor = resources.getColor(R.color.colorPrimaryDark)
        set1.shadowWidth = 0.8f
        set1.decreasingColor = resources.getColor(R.color.colorDownTrend)
        set1.decreasingPaintStyle = Paint.Style.FILL
        set1.increasingColor = resources.getColor(R.color.colorUpTrend)
        set1.increasingPaintStyle = Paint.Style.FILL
        set1.neutralColor = Color.LTGRAY
        set1.setDrawValues(false)
        set1.label = "test"

        val data = CandleData(set1)
        candleStickChart.data = data
        candleStickChart.invalidate()
    }

    override fun onOptionsItemSelected(item: MenuItem) =
            when (item.itemId) {
                android.R.id.home -> {
                    //navigateUpTo(Intent(this, StockItemListActivity::class.java))
                    startActivity(Intent(this,MasterActivity::class.java))
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }
}
