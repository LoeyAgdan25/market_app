package invest.com.swapp.repository

import androidx.lifecycle.LiveData
import invest.com.swapp.db.room.TradeDao
import invest.com.swapp.model.StockTrade

class TradeRepository(private val stockTradeDao: TradeDao) {
    var stockTrade:LiveData<List<StockTrade>> = stockTradeDao.getAllTrades()

    suspend fun insert(stockTrade: StockTrade){
        stockTradeDao.insert(stockTrade)
    }

    fun getTrade(id:Int):StockTrade{
        return stockTradeDao.getTrade(id)
    }

    suspend fun deleteTrade(trade: StockTrade){
        stockTradeDao.deleteTrade(trade)
    }

    suspend fun updateTrade(trade: StockTrade){
        stockTradeDao.updateTrade(trade)
    }

    suspend fun getAllTrade(type: Int){
        stockTrade = if(type != 0){
            stockTradeDao.getAllByType(type)
        }else{
            stockTradeDao.getAllTrades()
        }
    }

    suspend fun syncToServer(){
        //sync to server data
    }
}