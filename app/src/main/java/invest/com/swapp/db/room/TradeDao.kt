package invest.com.swapp.db.room

import androidx.lifecycle.LiveData
import androidx.room.*
import invest.com.swapp.model.StockTrade

@Dao
interface TradeDao {

    @Query("SELECT * FROM stock_trades") //add to get current price
    fun getAllTrades():LiveData<List<StockTrade>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(trade: StockTrade)

    @Update
    suspend fun updateTrade(vararg trade: StockTrade)

    @Delete
    suspend fun deleteTrade(vararg  trade: StockTrade)

    @Query("SELECT * FROM stock_trades WHERE type=:type") //buy
    fun getAllByType(type: Int): LiveData<List<StockTrade>>

    @Query("SELECT * FROM stock_trades WHERE id=:id") //WHERE ID
    fun getTrade(id: Int):StockTrade

}