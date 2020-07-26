package invest.com.swapp.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import invest.com.swapp.R
import invest.com.swapp.listener.TradeListener
import invest.com.swapp.model.StockTrade
import kotlinx.android.synthetic.main.trade_list_content.*
import kotlinx.android.synthetic.main.trade_list_content.view.*


class TradeListAdapter(private val stocks:List<StockTrade>) : RecyclerView.Adapter<TradeListAdapter.ViewHolder>(){

    var tradeListener:TradeListener? = null
    private val onClickListener: View.OnClickListener
    init {
        onClickListener = View.OnClickListener { v ->
            val item = v.tag as StockTrade
        }
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(p0.context)
                .inflate(R.layout.trade_list_content, p0, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = stocks.size


    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val item = stocks[p1]
        p0.symbol.text = item.code.toUpperCase()
        p0.price.text = "Price: ${item.buy_price}"
        p0.shares.text = "Shares: ${item.shares}"
        p0.total.text = "Total: ${item.total_amount}"
        p0.btnDelete.setOnClickListener {
            tradeListener!!.onDelete(item)
        }

        p0.btnEditTrade.setOnClickListener {
            tradeListener!!.onEdit(item)
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val symbol: TextView = view.stock_code_trade_list
        val price: MaterialTextView = view.buy_price_trade_list
        val shares:MaterialTextView = view.no_share_trade_list
        val total:MaterialTextView = view.no_total_cost_list
        var btnDelete:MaterialButton = view.delete_trade
        var btnEditTrade:MaterialButton = view.edit_trade
    }

}