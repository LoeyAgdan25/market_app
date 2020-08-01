package invest.com.swapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import invest.com.swapp.db.room.StocksRoomDatabase
import invest.com.swapp.model.StockPortfolio
import invest.com.swapp.model.StockTrade
import invest.com.swapp.repository.PortfolioRepository
import invest.com.swapp.repository.TradeRepository
import kotlinx.coroutines.launch

class TradeViewModel(application: Application): AndroidViewModel(application) {

    private val repository: TradeRepository
    private val portfolioRepo: PortfolioRepository

    var trades: LiveData<List<StockTrade>>
    var portfolio: LiveData<List<StockPortfolio>>
    init {
        val tradeDao = StocksRoomDatabase.getDatabase(application,viewModelScope).tradeDao()
        val portDao = StocksRoomDatabase.getDatabase(application, viewModelScope).portolioDao()
       // trades = repository.getAllTrade(0)
        repository = TradeRepository(tradeDao)
        portfolioRepo = PortfolioRepository(portDao)

        trades = repository.stockTrade
        portfolio = portfolioRepo.portfolio
    }

    fun saveTrade(trade:StockTrade) = viewModelScope.launch {

        //convert stock trade to portfolio
        var stockPortolio = StockPortfolio( trade.code, trade.total_amount, trade.shares, trade.type,0f)
        portfolioRepo.insert(stockPortolio)
        repository.insert(trade)
    }

    suspend fun getTrade(id:Int):StockTrade{
        return repository.getTrade(id)
    }

    fun updateTrade(trade: StockTrade) = viewModelScope.launch{
        repository.updateTrade(trade)
    }

    suspend fun deleteTrade(trade: StockTrade){
        repository.deleteTrade(trade)
    }
}