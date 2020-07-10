package invest.com.swapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import invest.com.swapp.R
import invest.com.swapp.model.StockTrade
import kotlinx.android.synthetic.main.trade_list_content.*
import kotlinx.android.synthetic.main.trade_list_content.view.*


class TradeListAdapter(private val stocks:List<StockTrade>) : RecyclerView.Adapter<TradeListAdapter.ViewHolder>(){

//    var watchListener:WatchListener? = null
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
        p0.symbol.text = item.code
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val symbol: TextView = view.stock_code_trade_list

    }

}