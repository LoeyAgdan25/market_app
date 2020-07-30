package invest.com.swapp.adapter

import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import invest.com.swapp.R
import invest.com.swapp.model.StockPortfolio
import invest.com.swapp.model.StockTrade
import kotlinx.android.synthetic.main.portfolio_list_layout.view.*

class PortfolioAdapter(private val stocks:List<StockPortfolio>) : RecyclerView.Adapter<PortfolioAdapter.ViewHolder>(){

    //var tradeListener:TradeListener? = null
    private val onClickListener: View.OnClickListener
    init {
        onClickListener = View.OnClickListener { v ->
            val item = v.tag as StockTrade
        }
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(p0.context)
                .inflate(R.layout.portfolio_list_layout, p0, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = stocks.size


    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val item = stocks[p1]
        p0.symbol.text = item.code.toUpperCase()
        p0.price.text = "Shares: ${item.total_shares}"
        p0.share.text = "Price: ${item.total_amount}"


    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val symbol: TextView = view.code_port
        val price: TextView = view.price_port
        val share: TextView = view.shares_port
//        val shares:MaterialTextView = view.no_share_trade_list

    }

}