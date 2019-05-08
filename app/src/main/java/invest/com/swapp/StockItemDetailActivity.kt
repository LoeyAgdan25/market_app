package invest.com.swapp

import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import android.view.View
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
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.lang.reflect.InvocationTargetException
import java.util.ArrayList


class StockItemDetailActivity : AppCompatActivity() {

    internal var string = "{\"count\":30,\"records\":[{\"sqLow\":301.2,\"sqOpen\":303,\"sqHigh\":305,\"percChangeClose\":0,\"sqClose\":303,\"secQid\":\"SCYQT_20190400018902\",\"changeClose\":0,\"totalValue\":1.49795242E8,\"totalVolume\":494290,\"lastTradePrice\":303,\"avgPrice\":303.0513301907787,\"tradingDate\":\"2019-04-26 00:00:00.0\",\"sqPrevious\":303},{\"sqLow\":301,\"sqOpen\":301,\"sqHigh\":307.6,\"percChangeClose\":0.86551,\"sqClose\":303,\"secQid\":\"SCYQT_20190400018652\",\"changeClose\":2.6,\"totalValue\":2.74904516E8,\"totalVolume\":906290,\"lastTradePrice\":303,\"avgPrice\":303.329525869203,\"tradingDate\":\"2019-04-25 00:00:00.0\",\"sqPrevious\":300.4},{\"sqLow\":300,\"sqOpen\":301.8,\"sqHigh\":302.8,\"percChangeClose\":-0.59563,\"sqClose\":300.4,\"secQid\":\"SCYQT_20190400018412\",\"changeClose\":-1.8,\"totalValue\":3.43598008E8,\"totalVolume\":1141800,\"lastTradePrice\":300.4,\"avgPrice\":300.9266141180592,\"tradingDate\":\"2019-04-24 00:00:00.0\",\"sqPrevious\":302.2},{\"sqLow\":301.4,\"sqOpen\":303,\"sqHigh\":304.4,\"percChangeClose\":-0.26403,\"sqClose\":302.2,\"secQid\":\"SCYQT_20190400018167\",\"changeClose\":-0.8,\"totalValue\":2.91312114E8,\"totalVolume\":963380,\"lastTradePrice\":302.2,\"avgPrice\":302.3854699080321,\"tradingDate\":\"2019-04-23 00:00:00.0\",\"sqPrevious\":303},{\"sqLow\":302.6,\"sqOpen\":304,\"sqHigh\":309.8,\"percChangeClose\":-0.32895,\"sqClose\":303,\"secQid\":\"SCYQT_20190400017934\",\"changeClose\":-1,\"totalValue\":3.95456306E8,\"totalVolume\":1300150,\"lastTradePrice\":303,\"avgPrice\":304.1620628389032,\"tradingDate\":\"2019-04-22 00:00:00.0\",\"sqPrevious\":304},{\"sqLow\":303,\"sqOpen\":306,\"sqHigh\":307.8,\"percChangeClose\":-0.65359,\"sqClose\":304,\"secQid\":\"SCYQT_20190400017711\",\"changeClose\":-2,\"totalValue\":2.13308476E8,\"totalVolume\":700640,\"lastTradePrice\":304,\"avgPrice\":304.4480417903631,\"tradingDate\":\"2019-04-17 00:00:00.0\",\"sqPrevious\":306},{\"sqLow\":305.8,\"sqOpen\":310.2,\"sqHigh\":310.4,\"percChangeClose\":-1.29032,\"sqClose\":306,\"secQid\":\"SCYQT_20190400017480\",\"changeClose\":-4,\"totalValue\":2.40388716E8,\"totalVolume\":781860,\"lastTradePrice\":306,\"avgPrice\":307.4574936689433,\"tradingDate\":\"2019-04-16 00:00:00.0\",\"sqPrevious\":310},{\"sqLow\":305.8,\"sqOpen\":310,\"sqHigh\":312,\"percChangeClose\":0,\"sqClose\":310,\"secQid\":\"SCYQT_20190400017239\",\"changeClose\":0,\"totalValue\":3.20423108E8,\"totalVolume\":1037610,\"lastTradePrice\":310,\"avgPrice\":308.80880870461925,\"tradingDate\":\"2019-04-15 00:00:00.0\",\"sqPrevious\":310},{\"sqLow\":310,\"sqOpen\":318,\"sqHigh\":319.8,\"percChangeClose\":-2.26986,\"sqClose\":310,\"secQid\":\"SCYQT_20190400016994\",\"changeClose\":-7.2,\"totalValue\":5.26382822E8,\"totalVolume\":1671610,\"lastTradePrice\":310,\"avgPrice\":314.89571251667553,\"tradingDate\":\"2019-04-12 00:00:00.0\",\"sqPrevious\":317.2},{\"sqLow\":317.2,\"sqOpen\":322,\"sqHigh\":322.6,\"percChangeClose\":-1.49,\"sqClose\":317.2,\"secQid\":\"SCYQT_20190400016770\",\"changeClose\":-4.8,\"totalValue\":1.07146204E8,\"totalVolume\":335080,\"lastTradePrice\":317.2,\"avgPrice\":319.7631,\"tradingDate\":\"2019-04-11 00:00:00.0\",\"sqPrevious\":322},{\"sqLow\":320.4,\"sqOpen\":322,\"sqHigh\":322.6,\"percChangeClose\":1.00376,\"sqClose\":322,\"secQid\":\"SCYQT_20190400016535\",\"changeClose\":3.2,\"totalValue\":2.54266656E8,\"totalVolume\":790720,\"lastTradePrice\":322,\"avgPrice\":321.5634560906516,\"tradingDate\":\"2019-04-10 00:00:00.0\",\"sqPrevious\":318.8},{\"sqLow\":318.8,\"sqOpen\":320.2,\"sqHigh\":320.6,\"percChangeClose\":-0.375,\"sqClose\":318.8,\"secQid\":\"SCYQT_20190400016277\",\"changeClose\":-1.2,\"totalValue\":1.57618072E8,\"totalVolume\":493570,\"lastTradePrice\":318.8,\"avgPrice\":319.34289361184835,\"tradingDate\":\"2019-04-08 00:00:00.0\",\"sqPrevious\":320},{\"sqLow\":319,\"sqOpen\":319,\"sqHigh\":321,\"percChangeClose\":0.37641,\"sqClose\":320,\"secQid\":\"SCYQT_20190400016021\",\"changeClose\":1.2,\"totalValue\":4.45013356E8,\"totalVolume\":1390800,\"lastTradePrice\":320,\"avgPrice\":319.9693385102099,\"tradingDate\":\"2019-04-05 00:00:00.0\",\"sqPrevious\":318.8},{\"sqLow\":317.8,\"sqOpen\":318.8,\"sqHigh\":319.6,\"percChangeClose\":0,\"sqClose\":318.8,\"secQid\":\"SCYQT_20190400015803\",\"changeClose\":0,\"totalValue\":1.67851596E8,\"totalVolume\":526570,\"lastTradePrice\":318.8,\"avgPrice\":318.76406935450177,\"tradingDate\":\"2019-04-04 00:00:00.0\",\"sqPrevious\":318.8},{\"sqLow\":316.4,\"sqOpen\":317,\"sqHigh\":319,\"percChangeClose\":0.56782,\"sqClose\":318.8,\"secQid\":\"SCYQT_20190400015542\",\"changeClose\":1.8,\"totalValue\":1.23880212E8,\"totalVolume\":389250,\"lastTradePrice\":318.8,\"avgPrice\":318.25359537572257,\"tradingDate\":\"2019-04-03 00:00:00.0\",\"sqPrevious\":317},{\"sqLow\":315,\"sqOpen\":315,\"sqHigh\":318.6,\"percChangeClose\":0.63492,\"sqClose\":317,\"secQid\":\"SCYQT_20190400015299\",\"changeClose\":2,\"totalValue\":2.18588468E8,\"totalVolume\":689090,\"lastTradePrice\":317,\"avgPrice\":317.21323484595626,\"tradingDate\":\"2019-04-02 00:00:00.0\",\"sqPrevious\":315},{\"sqLow\":311.8,\"sqOpen\":317,\"sqHigh\":317.4,\"percChangeClose\":-0.63091,\"sqClose\":315,\"secQid\":\"SCYQT_20190400015070\",\"changeClose\":-2,\"totalValue\":1.36790566E8,\"totalVolume\":436040,\"lastTradePrice\":315,\"avgPrice\":313.7110494450051,\"tradingDate\":\"2019-04-01 00:00:00.0\",\"sqPrevious\":317},{\"sqLow\":316,\"sqOpen\":316.2,\"sqHigh\":319,\"percChangeClose\":0.31646,\"sqClose\":317,\"secQid\":\"SCYQT_20190300014824\",\"changeClose\":1,\"totalValue\":1.9937501E8,\"totalVolume\":628390,\"lastTradePrice\":317,\"avgPrice\":317.2790941930966,\"tradingDate\":\"2019-03-29 00:00:00.0\",\"sqPrevious\":316},{\"sqLow\":313,\"sqOpen\":317,\"sqHigh\":317,\"percChangeClose\":0,\"sqClose\":316,\"secQid\":\"SCYQT_20190300014578\",\"changeClose\":0,\"totalValue\":8.5794952E7,\"totalVolume\":272100,\"lastTradePrice\":316,\"avgPrice\":315.30669606762217,\"tradingDate\":\"2019-03-28 00:00:00.0\",\"sqPrevious\":316},{\"sqLow\":313,\"sqOpen\":316.6,\"sqHigh\":317,\"percChangeClose\":-0.18951,\"sqClose\":316,\"secQid\":\"SCYQT_20190300014311\",\"changeClose\":-0.6,\"totalValue\":1.05627558E8,\"totalVolume\":334490,\"lastTradePrice\":316,\"avgPrice\":315.7868934796257,\"tradingDate\":\"2019-03-27 00:00:00.0\",\"sqPrevious\":316.6},{\"sqLow\":314,\"sqOpen\":315.8,\"sqHigh\":319.2,\"percChangeClose\":0.18987,\"sqClose\":316.6,\"secQid\":\"SCYQT_20190300014079\",\"changeClose\":0.6,\"totalValue\":2.31388642E8,\"totalVolume\":730920,\"lastTradePrice\":316.6,\"avgPrice\":316.57177529688613,\"tradingDate\":\"2019-03-26 00:00:00.0\",\"sqPrevious\":316},{\"sqLow\":308,\"sqOpen\":313,\"sqHigh\":316,\"percChangeClose\":1.15237,\"sqClose\":316,\"secQid\":\"SCYQT_20190300013863\",\"changeClose\":3.6,\"totalValue\":8.50683636E8,\"totalVolume\":2737700,\"lastTradePrice\":316,\"avgPrice\":310.72931146582897,\"tradingDate\":\"2019-03-25 00:00:00.0\",\"sqPrevious\":312.4},{\"sqLow\":312.2,\"sqOpen\":313.4,\"sqHigh\":317.2,\"percChangeClose\":0,\"sqClose\":312.4,\"secQid\":\"SCYQT_20190300013616\",\"changeClose\":0,\"totalValue\":2.53693268E8,\"totalVolume\":809450,\"lastTradePrice\":312.4,\"avgPrice\":313.4143776638458,\"tradingDate\":\"2019-03-22 00:00:00.0\",\"sqPrevious\":312.4},{\"sqLow\":312,\"sqOpen\":313.6,\"sqHigh\":315,\"percChangeClose\":-0.38265,\"sqClose\":312.4,\"secQid\":\"SCYQT_20190300013374\",\"changeClose\":-1.2,\"totalValue\":1.4804702E8,\"totalVolume\":472780,\"lastTradePrice\":312.4,\"avgPrice\":313.14146114471845,\"tradingDate\":\"2019-03-21 00:00:00.0\",\"sqPrevious\":313.6},{\"sqLow\":312.8,\"sqOpen\":313,\"sqHigh\":315,\"percChangeClose\":0,\"sqClose\":313.6,\"secQid\":\"SCYQT_20190300013122\",\"changeClose\":0,\"totalValue\":8.9737984E7,\"totalVolume\":285920,\"lastTradePrice\":313.6,\"avgPrice\":313.85696698377166,\"tradingDate\":\"2019-03-20 00:00:00.0\",\"sqPrevious\":313.6},{\"sqLow\":312.2,\"sqOpen\":315.2,\"sqHigh\":316,\"percChangeClose\":-0.50761,\"sqClose\":313.6,\"secQid\":\"SCYQT_20190300012881\",\"changeClose\":-1.6,\"totalValue\":1.15083494E8,\"totalVolume\":365990,\"lastTradePrice\":313.6,\"avgPrice\":314.4443673324408,\"tradingDate\":\"2019-03-19 00:00:00.0\",\"sqPrevious\":315.2},{\"sqLow\":313,\"sqOpen\":316.4,\"sqHigh\":317.2,\"percChangeClose\":-0.31626,\"sqClose\":315.2,\"secQid\":\"SCYQT_20190300012644\",\"changeClose\":-1,\"totalValue\":2.81602046E8,\"totalVolume\":893290,\"lastTradePrice\":315.2,\"avgPrice\":315.2414624589999,\"tradingDate\":\"2019-03-18 00:00:00.0\",\"sqPrevious\":316.2},{\"sqLow\":310.6,\"sqOpen\":313,\"sqHigh\":316.2,\"percChangeClose\":1.34615,\"sqClose\":316.2,\"secQid\":\"SCYQT_20190300012400\",\"changeClose\":4.2,\"totalValue\":2.33490632E8,\"totalVolume\":740420,\"lastTradePrice\":316.2,\"avgPrice\":315.34889927338537,\"tradingDate\":\"2019-03-15 00:00:00.0\",\"sqPrevious\":312},{\"sqLow\":312,\"sqOpen\":316,\"sqHigh\":316,\"percChangeClose\":-1.39064,\"sqClose\":312,\"secQid\":\"SCYQT_20190300012172\",\"changeClose\":-4.4,\"totalValue\":8.0441566E7,\"totalVolume\":257020,\"lastTradePrice\":312,\"avgPrice\":312.977846082017,\"tradingDate\":\"2019-03-14 00:00:00.0\",\"sqPrevious\":316.4},{\"sqLow\":315,\"sqOpen\":315.6,\"sqHigh\":318,\"percChangeClose\":-0.18927,\"sqClose\":316.4,\"secQid\":\"SCYQT_20190300011932\",\"changeClose\":-0.6,\"totalValue\":1.15340046E8,\"totalVolume\":364290,\"lastTradePrice\":316.4,\"avgPrice\":316.6160092234209,\"tradingDate\":\"2019-03-13 00:00:00.0\",\"sqPrevious\":317}]}"


    private lateinit var mInterstitialAd: InterstitialAd
    private lateinit var client: OkHttpClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stockitem_detail)
        setSupportActionBar(toolbar)



        var symbol = intent.getStringExtra(StockItemDetailFragment.ARG_ITEM_SYMBOL)
        var name = intent.getStringExtra(StockItemDetailFragment.ARG_ITEM_NAME)
        var volume = intent.getStringExtra(StockItemDetailFragment.ARG_ITEM_VOLUME)
        var percent = intent.getStringExtra(StockItemDetailFragment.ARG_ITEM_PERCENTAGE)
        var price = intent.getStringExtra(StockItemDetailFragment.ARG_ITEM_PRICE)
        var status = intent.getStringExtra(StockItemDetailFragment.ARG_ITEM_STATUS)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = ""


        txt_stock_symbol.text = "${symbol}"
        txt_stock_percent.text = "${percent}"
        txt_stock_price.text = "${UtilityHelper.getInstance().formatCurrency(price.toDouble())}"
        txt_stock_status.text = "${status}"
        txt_stock_volume.text = "${UtilityHelper.getInstance().formatVolume(volume.toDouble())}"
        txt_company_description.text = "${name}"

        btn_watch_stock.setOnClickListener { doWatchStock(symbol) }
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

        //setupCandle(string)
        searchSecurityToLoad()

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
//                    if(getString(getColumnIndex("status")).equals("watched")){
//                        toast("Stock already in watched")
//                    }else{
//                        doWatchStock()
//                    }
                    //toast("this is in watchlist")
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

    fun searchSecurityToLoad(){
        try{

            val file_name = "stocksinfo.json"
            val json_string = application.assets.open(file_name).bufferedReader().use {
                it.readText()
            }

            val json = JSONObject(json_string)
            val jsonArray = json.getJSONArray("records")
            for(i in 0 until jsonArray.length()){
                val stock = jsonArray.get(i).toString()
                val obj = JSONObject(stock)

                    if(intent.getStringExtra(StockItemDetailFragment.ARG_ITEM_SYMBOL) == "${obj.getString("securitySymbol")}"){
                        val urlRequest = Uri.parse("http://52.14.187.143/history.php?cid=${obj.getString("companyId")}&sid=${obj.getString("securityID")}").toString()
                        val request = Request.Builder().url(urlRequest).build()
                        client.newCall(request).enqueue(object : Callback {

                            override fun onResponse(call: Call, response: Response) {

                                if(response == null){
                                    toast("could not connect to api, try again.")
                                    return
                                }

                                var r = response.body()!!.string()
                                try {
                                    runOnUiThread {
                                        Log.d("candle value",r)
                                        setupCandle(r)
                                    }
                                }catch (e: InvocationTargetException){
                                    e.printStackTrace()
                                    val cause = e.cause
                                    Log.d("_json_error", "${cause}")

                                }
                            }

                            override fun onFailure(call: Call, e: IOException) {
                                Log.d("_json_network_error", e.message)

                            }
                        })
                    }

                Log.d("_securities", "${obj.getString("securitySymbol")}  ${obj.getString("securityID")} ${obj.getString("companyId")}")
            }

        }catch (ex:Exception){
            ex.printStackTrace()
        }
    }

    fun setupCandle(string: String){
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

        val historyData = ArrayList<HistoryData>()

        try {
            val json = JSONObject(string)
            val jsonArray = json.getJSONArray("records")



            for (i in jsonArray.length() - 1 downTo 0) {

                val json_data = jsonArray.getJSONObject(i)

                val shadowHigh = "${json_data.getInt("sqHigh")}"
                val shadowLow = "${json_data.getInt("sqLow")}"
                val open = "${json_data.getString("sqOpen")}"
                val close = "${json_data.getString("sqClose")}"


                historyData.add(HistoryData(shadowHigh, shadowLow, open, close))


                Log.d("_i", "" + i)

                //yValsCandleStick.add(new CandleEntry(i, new Float(shadowHigh), new Float(shadowLow), new Float(open), new Float(close)));
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }


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
