package invest.com.swapp.db.room

import androidx.lifecycle.LiveData
import androidx.room.*
import invest.com.swapp.model.Stock2
import invest.com.swapp.model.StocksWatched

@Dao
interface WatchedDao {
    @Query("SELECT stocks_watched_table.buy_price, stocks_watched_table.sell_price, stocks_watched_table.symbol,stocks_table.symbol, stocks_table.price, stocks_table.percent_change, stocks_table.name  FROM stocks_watched_table INNER JOIN stocks_table ON stocks_watched_table.symbol = stocks_table.symbol")
    fun getAllWatched(): LiveData<List<StocksWatched>>

    @Insert(onConflict =  OnConflictStrategy.IGNORE)
    suspend fun insert(stock: StocksWatched)

    @Update
    suspend fun updateWatched(vararg watched: StocksWatched)

    @Delete
    suspend fun deleteWatched(vararg watched: StocksWatched)

    //todo get watched
    @Query("SELECT * FROM stocks_watched_table WHERE symbol = :symbol")
    fun getWatchedStock(symbol:String): StocksWatched

}