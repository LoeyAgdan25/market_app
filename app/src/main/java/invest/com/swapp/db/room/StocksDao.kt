package invest.com.swapp.db.room

import androidx.lifecycle.LiveData
import androidx.room.*
import invest.com.swapp.model.Stock2

@Dao
interface StocksDao {

    @androidx.room.Query("SELECT * FROM stocks_table")
    fun getAllStocks(): LiveData<List<Stock2>>

    //todo:- fix this to insert all
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(stock: Stock2)

    @Query("SELECT * FROM stocks_table WHERE symbol = :symbol")
    fun getStock(symbol:String): Stock2

    @Update
    suspend fun updateStock(vararg watched: Stock2)

}