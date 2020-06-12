package invest.com.swapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import invest.com.swapp.api.ApiInterface
import invest.com.swapp.api.RetrofitInstance
import invest.com.swapp.db.room.StocksDao
import invest.com.swapp.db.room.WatchedDao
import invest.com.swapp.model.HistoryData
import invest.com.swapp.model.Stock2
import invest.com.swapp.model.StocksWatched
import org.json.JSONException
import org.json.JSONObject

class StocksRepository(private val stocksDao: StocksDao, private val watchedDao: WatchedDao) {

    val retStocks = RetrofitInstance.getRetrofitInstance().create(ApiInterface::class.java)
    val allStocks: LiveData<List<Stock2>> = stocksDao.getAllStocks()
    val allWatched: LiveData<List<StocksWatched>> = watchedDao.getAllWatched() //todo: must get current price and change...
    val watchedNotify = listOf<Stock2>()

    suspend fun insert(stock: Stock2){
        stocksDao.insert(stock)
    }

    suspend fun getStock(symbol:String): Stock2{
        return stocksDao.getStock(symbol)
    }

    suspend fun deleteWatched(watched: StocksWatched){
        watchedDao.deleteWatched(watched)
    }

    suspend fun updateWatched(watched: StocksWatched){
        watchedDao.updateWatched(watched)
    }

    suspend fun watch(watched: StocksWatched){
        watchedDao.insert(watched)
    }

    suspend fun getWatched(symbol:String){
        watchedDao.getWatchedStock(symbol)
    }

    suspend fun getAllStocks(){

        //todo:- move to work manager for everyday update of price
        //get stock changes...
        Log.d("tag","getstock executed")

        var ret = retStocks.getAllStocks().execute()
        if(ret.isSuccessful){
            var r = ret.body()!!.string()
            try{
                val rootObject = JSONObject(r)
                var roots = rootObject.getJSONArray("stock")

                var isExist = false
                if(stocksDao.getCountStocks() > 0){
                    isExist = true
                }

                for (i in 0 until roots.length()) {
                    val stock = roots.get(i).toString()
                    val obj = JSONObject(stock)

                    var symbol = obj.getString("symbol")
                    var nme = obj.getString("name")
                    var price = obj.getString("price")
                    var volume = obj.getString("volume")
                    var change = obj.getString("percent_change")
                    var cid = obj.getString("companyId")
                    var sid = obj.getString("securityID")
                    var stockThis = Stock2(nme,symbol,"",change,volume,cid.toInt(),sid.toInt(),price)

                    if(isExist){
                        stocksDao.updateStock(stockThis)
                    }else{
                        stocksDao.insert(stockThis)
                    }


                    //todo:- add stock update in the background...
                }
                //notify
            }catch (ex:JSONException){
                ex.printStackTrace()
            }

        }else{
            Log.d("_logdb","failed stocks ${ret.errorBody()}")
        }
    }

    suspend fun getHistoryData(securityId:Int, companyId:Int): ArrayList<HistoryData>{

        var historyData = ArrayList<HistoryData>()
        var response = ""
            var resp = retStocks.getHistoryData(securityId,companyId).execute()
            if(resp.isSuccessful){
                response = resp.body()!!.string()
                try {
                    val json = JSONObject(response)
                    val jsonArray = json.getJSONArray("records")

                    for (i in jsonArray.length() - 1 downTo 0) {

                        val json_data = jsonArray.getJSONObject(i)
                        val shadowHigh = "${json_data.getInt("sqHigh")}"
                        val shadowLow = "${json_data.getInt("sqLow")}"
                        val open = "${json_data.getString("sqOpen")}"
                        val close = "${json_data.getString("sqClose")}"

                        historyData.add(HistoryData(shadowHigh, shadowLow, open, close))
                    }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
        return historyData
    }

}


