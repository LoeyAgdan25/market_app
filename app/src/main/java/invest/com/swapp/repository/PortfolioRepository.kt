package invest.com.swapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import invest.com.swapp.db.room.PortfolioDao
import invest.com.swapp.model.StockPortfolio

class PortfolioRepository (private val portfolioDao: PortfolioDao){
    var portfolio: LiveData<List<StockPortfolio>> = portfolioDao.getPortfolio()

    suspend fun insert(stock: StockPortfolio){

        //do something before insert...
        //todo if list contains the insert item
        //if it contains
            //check if buy / sell
            //compute and update
        //

        //do the insert...

        var stockP = portfolioDao.getStock(stock.symbol)
        print("is found $stockP")
        Log.d("_FOUND", "$stockP  $stockP.tra")
        //todo:- onload get the current price...

        if(stockP == null){
            Log.d("_TRANS", "Portfolio inserted $stockP  $stock")
            portfolioDao.insert(stock)
        }else{
            if(stock.trans_type == 0){
                stockP.total_shares =  stockP.total_shares + stock.total_shares
                stockP.total_amount = stockP.total_amount + stock.total_amount
                portfolioDao.updateStock(stockP)
            }else{

                stockP.total_shares = stockP.total_shares - stock.total_shares

                if(stockP.total_shares == 0f){
                    portfolioDao.deleteStock(stockP)
                }else {
                    stockP.total_amount = stockP.total_amount - (stockP.total_amount * (stock.total_shares / stockP.total_shares))
                    portfolioDao.updateStock(stockP)
                }
            }
        }
    }

    suspend fun getStock(code: String):StockPortfolio{
        return portfolioDao.getStock(code)
    }

    suspend fun deleteStock(stock: StockPortfolio){
        portfolioDao.deleteStock(stock)
    }

    suspend fun updateStock(stock: StockPortfolio){
        portfolioDao.updateStock(stock)
    }



}