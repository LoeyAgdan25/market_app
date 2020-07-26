package invest.com.swapp.listener

import invest.com.swapp.model.StockTrade

interface TradeListener {
    fun onEdit(trade: StockTrade)
    fun onDelete(trade: StockTrade)
}