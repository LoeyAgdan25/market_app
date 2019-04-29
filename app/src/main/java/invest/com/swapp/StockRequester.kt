package invest.com.swapp

import android.app.Activity
import android.content.Context
import android.net.Uri.Builder
import android.util.Log
import invest.com.swapp.model.Stock
import okhttp3.*
import java.io.IOException

class StockRequester(listeningActivity: Activity){
    interface StockRequesterResponse{
        fun receiveNewStocks(newStock: Stock)
    }


    private val reponseListener: StockRequesterResponse
    private val context:Context
    private val client:OkHttpClient

    var isLoadingData:Boolean = false

    private set

    init{
        reponseListener = listeningActivity as StockRequesterResponse
        context = listeningActivity.applicationContext
        client = OkHttpClient()
    }

    fun getStocks(){
        val urlRequest = Builder().scheme(URL_SCHEME)
                .authority(URL_AUTHORITY)
                .appendPath(URL_PATH_1)
                .build().toString()

        val request = Request.Builder().url(urlRequest).build()
        isLoadingData = false

        client.newCall(request).enqueue(object :Callback{
            override fun onResponse(call: Call, response: Response) {
                Log.d("_json", response.body()!!.string())


            }

            override fun onFailure(call: Call, e: IOException) {

            }
        })

    }

    //http://phisix-api2.appspot.com/stocks.json

    companion object {
        private val MEDIA_TYPE_KEY = "media_type"
        private val MEDIA_TYPE_VIDEO_VALUE = "video"
        private val URL_SCHEME = "http"
        private val URL_AUTHORITY = "phisix-api2.appspot.com"
        private val URL_PATH_1 = "stocks.json"
        private val URL_PATH_2 = ""
        private val URL_QUERY_PARAM_DATE_KEY = ""
        private val URL_QUERY_PARAM_API_KEY = ""
    }

}