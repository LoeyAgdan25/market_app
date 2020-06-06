package invest.com.swapp.db.room

import androidx.lifecycle.LiveData
import androidx.room.*
import invest.com.swapp.model.Stock2
import javax.sql.DataSource

@Dao
interface StocksDao {

    @androidx.room.Query("SELECT * FROM stocks_table")
    fun getAllStocks(): LiveData<List<Stock2>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(stock: Stock2)

    @Query("SELECT * FROM stocks_table WHERE symbol = :symbol")
    fun getStock(symbol:String): Stock2

    @Update
    suspend fun updateStock(vararg watched: Stock2)

    @Query("SELECT * FROM stocks_table")
    fun pagedStockList(): androidx.paging.DataSource.Factory<Int, Stock2>

    @Query("SELECT COUNT(*) FROM stocks_table")
    fun getCountStocks():Int

}