package invest.com.swapp

import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import invest.com.swapp.helper.UtilityHelper
import kotlinx.android.synthetic.main.activity_stockitem_detail.*
import kotlinx.android.synthetic.main.content_detail_stocks.*
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry
import invest.com.swapp.model.HistoryData
import invest.com.swapp.model.Stock2
import invest.com.swapp.model.StocksWatched
import invest.com.swapp.viewmodel.StocksViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import okhttp3.*
import java.util.ArrayList


class StockItemDetailActivity : AppCompatActivity() {

    private lateinit var mInterstitialAd: InterstitialAd
    private lateinit var stockViewModel:StocksViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stockitem_detail)
        setSupportActionBar(toolbar)

        stockViewModel = ViewModelProviders.of(this).get(StocksViewModel::class.java)
        stockViewModel.historyData.observe(this, Observer {
            setupCandle2(it)
        })

        var stock = intent.getSerializableExtra("stockDetail") as? Stock2
        var symbol = stock!!.symbol
        var name = stock!!.name
        var volume = stock!!.volume
        var percent = stock!!.percent_change
        var price = stock!!.price
        var status = ""
        var companyId = stock.companyId
        var securityID = stock.securityID



        GlobalScope.async {
            stockViewModel.getStockHistory(securityID,companyId)
        }

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = ""

        txt_stock_symbol.text = "${symbol}"

        if(percent.contains("-")){
            txt_stock_percent.setBackgroundColor(Color.RED)
            txt_stock_percent.text = "${percent}"
        }else{

            txt_stock_percent.setBackgroundColor(resources.getColor(R.color.colorPrimary))
            txt_stock_percent.text = "+${percent}"
            if(percent == "0"){
                txt_stock_percent.setBackgroundColor(Color.parseColor("#FF9800"))
            }
        }

        txt_stock_price.text = "${UtilityHelper.getInstance().formatCurrency(price.toDouble())}"
        txt_stock_status.text = "${status}"
        txt_stock_volume.text = "${UtilityHelper.getInstance().formatVolume(volume.toDouble())}"
        txt_company_description.text = "${name}"

        btn_watch_invest.setOnClickListener { doInvest(symbol) }
        //doFindStockAll()
        //Production: ca-app-pub-4268048783942748/7970283074
        //Testing: ca-app-pub-3940256099942544/1033173712
        mInterstitialAd = InterstitialAd(this)
        mInterstitialAd.adUnitId = "ca-app-pub-3940256099942544/1033173712"
        mInterstitialAd.loadAd(AdRequest.Builder().build())



        btn_watch_stock.setOnClickListener {
           GlobalScope.async {
               stockViewModel.watched(StocksWatched(symbol,0f,0f))
           }
        }
    }

    fun doInvest(sym:String){
        var intent = Intent(this,InvestActivity::class.java).apply {
            putExtra("symbol","$sym")
        }
        startActivity(intent)
    }

    fun setupCandle2(historyData:ArrayList<HistoryData>){
        val candleStickChart = candle_stick_chart
        candleStickChart.isHighlightPerDragEnabled = true
        candleStickChart.setBackgroundColor(Color.parseColor("#000000"))
        candleStickChart.setDrawBorders(true)
        candleStickChart.setBorderColor(resources.getColor(R.color.colorPrimary))
        val yAxis = candleStickChart.axisLeft
        val rightAxis = candleStickChart.axisRight
        yAxis.setDrawGridLines(false)
        rightAxis.setDrawGridLines(false)
        candleStickChart.requestDisallowInterceptTouchEvent(true)

        val xAxis = candleStickChart.xAxis

        xAxis.setDrawGridLines(false)// disable x axis grid lines
        xAxis.setDrawLabels(false)
        rightAxis.textColor = Color.WHITE
        yAxis.setDrawLabels(true)
        xAxis.granularity = 1f
        xAxis.isGranularityEnabled = true
        xAxis.setAvoidFirstLastClipping(true)

        val l = candleStickChart.getLegend()

        l.setEnabled(false)

        val yValsCandleStick = ArrayList<CandleEntry>()

        for ((j, data) in historyData.withIndex()) {
            yValsCandleStick.add(CandleEntry(j.toFloat(), data.high.toFloat(), data.low.toFloat(), data.open.toFloat(), data.close.toFloat()))
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
                    startActivity(Intent(this,MasterActivity::class.java))
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }
}
