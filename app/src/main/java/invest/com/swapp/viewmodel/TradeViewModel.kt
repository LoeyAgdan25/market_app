package invest.com.swapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import invest.com.swapp.db.room.StocksRoomDatabase
import invest.com.swapp.model.StockTrade
import invest.com.swapp.repository.TradeRepository
import kotlinx.coroutines.launch

class TradeViewModel(application: Application): AndroidViewModel(application) {
    private val repository: TradeRepository
    var trades: LiveData<List<StockTrade>>
    init {
        val tradeDao = StocksRoomDatabase.getDatabase(application,viewModelScope).tradeDao()
       // trades = repository.getAllTrade(0)
        repository = TradeRepository(tradeDao)
        trades = repository.stockTrade
    }

    fun saveTrade(trade:StockTrade) = viewModelScope.launch {
        repository.insert(trade)
    }

    suspend fun getTrade(id:Int):StockTrade{
        return repository.getTrade(id)
    }

    suspend fun updateTrade(trade: StockTrade){
        repository.updateTrade(trade)
    }

    suspend fun deleteTrade(trade: StockTrade){
        repository.deleteTrade(trade)
    }





}