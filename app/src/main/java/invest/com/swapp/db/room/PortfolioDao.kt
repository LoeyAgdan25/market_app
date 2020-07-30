package invest.com.swapp.db.room

import androidx.lifecycle.LiveData
import androidx.room.*
import invest.com.swapp.model.StockPortfolio

@Dao
interface PortfolioDao {
    @Query("SELECT * FROM stock_portfolio")
    fun getPortfolio(): LiveData<List<StockPortfolio>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(stock: StockPortfolio)

    @Update
    suspend fun updateStock(vararg stock: StockPortfolio)

    @Delete
    suspend fun deleteStock(vararg stock: StockPortfolio)

    @Query("SELECT * FROM stock_portfolio WHERE code=:code")
    suspend fun getStock(code:String):StockPortfolio
}