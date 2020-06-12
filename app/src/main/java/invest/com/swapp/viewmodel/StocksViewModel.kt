package invest.com.swapp.viewmodel

import android.app.Application
import androidx.lifecycle.*
import invest.com.swapp.db.room.StocksRoomDatabase
import invest.com.swapp.model.HistoryData
import invest.com.swapp.model.Stock
import invest.com.swapp.model.Stock2
import invest.com.swapp.model.StocksWatched
import invest.com.swapp.repository.StocksRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.BufferedReader

class StocksViewModel(application: Application): AndroidViewModel(application){

    private val repository: StocksRepository
    var stocks: LiveData<List<Stock2>>
    var response: MutableLiveData<String> = MutableLiveData()
    var historyData: MutableLiveData<ArrayList<HistoryData>> = MutableLiveData()
    var watchedStocks: LiveData<List<StocksWatched>>

    //val stockList2:LiveData<PagedList<Stock2>>

    init {
        val stocksDao = StocksRoomDatabase.getDatabase(application,viewModelScope).stockDao()
        val watchedDao = StocksRoomDatabase.getDatabase(application,viewModelScope).watchDao()

        repository = StocksRepository(stocksDao,watchedDao)
        stocks = repository.allStocks
        watchedStocks = repository.allWatched
        //åstockList2 = stocksDao.pagedStockList().toLiveData(pageSize = 20)
    }

    fun insert(stock: Stock2) = viewModelScope.launch{
        repository.insert(stock)
    }

    //download all the stocks
    suspend fun getStocks(){
        repository.getAllStocks()
    }

    suspend fun getNotificationMatched(){

    }

    suspend fun isWatched(symbol: String):Boolean{
        return true
    }

    suspend fun getStock(symbol: String):Stock2{
       return repository.getStock(symbol)
    }

    suspend fun getWatched(symbol: String) {
        return repository.getWatched(symbol)
    }

    suspend fun updateWatched(watched: StocksWatched){
        repository.updateWatched(watched)
    }

    suspend fun deleteWatched(watched: StocksWatched){
        repository.deleteWatched(watched)
    }

    fun buildWatchlistAlertMessage(list: List<String>):String{
        var strBuff = StringBuffer()
        for(l in list){
            var arr = l.split(":")

            strBuff.append(arr[2].toUpperCase() + " alert for " + arr[0] + " at price " + arr[1] + "\n")
        }

        return strBuff.toString()
    }


    suspend fun getStockHistory(sid: Int, cid: Int){
        var responseData = repository.getHistoryData(sid, cid)
        if(responseData != null){
            //response.postValue(responseData)
            historyData.postValue(responseData)
        }else{
            //response.postValue("history data is null")
            historyData.postValue(responseData)
        }
    }

    suspend fun watched(stock: StocksWatched){
        repository.watch(stock)
    }


}