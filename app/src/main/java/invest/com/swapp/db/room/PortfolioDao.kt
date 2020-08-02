package invest.com.swapp.db.room

import androidx.lifecycle.LiveData
import androidx.room.*
import invest.com.swapp.model.StockPortfolio

@Dao
interface PortfolioDao {
    @Query("SELECT stock_portfolio_table.symbol, stock_portfolio_table.total_amount, stock_portfolio_table.total_shares, stock_portfolio_table.trans_type , stock_portfolio_table.average_price, stocks_table.symbol  ,stocks_table.price ,stocks_table.percent_change FROM stock_portfolio_table INNER JOIN stocks_table ON stock_portfolio_table.symbol = stocks_table.symbol")
    fun getPortfolio(): LiveData<List<StockPortfolio>>

    @Query("SELECT stock_portfolio_table.symbol, stock_portfolio_table.total_amount, stock_portfolio_table.total_shares, stock_portfolio_table.trans_type , stock_portfolio_table.average_price, stocks_table.symbol  ,stocks_table.price ,stocks_table.percent_change FROM stock_portfolio_table INNER JOIN stocks_table ON stock_portfolio_table.symbol = stocks_table.symbol")
    fun getPortfolioUnlive(): List<StockPortfolio>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(stock: StockPortfolio)

    @Update
    suspend fun updateStock(vararg stock: StockPortfolio)

    @Delete
    suspend fun deleteStock(vararg stock: StockPortfolio)

    @Query("SELECT * FROM stock_portfolio_table WHERE symbol = :symbol")
    suspend fun getStock(symbol:String):StockPortfolio
}