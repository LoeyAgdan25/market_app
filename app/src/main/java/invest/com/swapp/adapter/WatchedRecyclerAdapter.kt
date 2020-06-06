package invest.com.swapp.adapter

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import invest.com.swapp.R
import invest.com.swapp.StockItemDetailActivity
import invest.com.swapp.helper.UtilityHelper
import invest.com.swapp.listener.WatchListener
import invest.com.swapp.model.Stock2
import invest.com.swapp.model.StocksWatched
import kotlinx.android.synthetic.main.watch_list_content.view.*

class WatchedRecyclerAdapter(private val stocks:List<StocksWatched>) : RecyclerView.Adapter<WatchedRecyclerAdapter.ViewHolder>(){

    var watchListener:WatchListener? = null
    private val onClickListener: View.OnClickListener
    init {
        onClickListener = View.OnClickListener { v ->
            val item = v.tag as Stock2
        }
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(p0.context)
                .inflate(R.layout.watch_list_content, p0, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = stocks.size

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val item = stocks[p1]
        p0.symbol.text = item.symbol
        p0.buy.text = "Buy: ${item.buy_price}"
        p0.sell.text = "Sell: ${item.sell_price}"
        p0.price.text = UtilityHelper.getInstance().formatCurrency(item.price.toDouble())
        p0.percentChange.text = item.percent_change
        p0.companyName.text = item.name

        if(item.percent_change.contains("-")){
            p0.percentChange.setBackgroundColor(Color.RED)
        }else{
            p0.percentChange.setBackgroundColor(Color.parseColor("#008b00"))
            if(item.percent_change == "0"){
                p0.percentChange.setBackgroundColor(Color.parseColor("#FF9100"))
            }else{
                p0.percentChange.text = "+${item.percent_change}"
            }
        }

        p0.btnAction.setOnClickListener {
            watchListener!!.onWatchedAction(item)
        }

        p0.h.setOnClickListener {
            watchListener!!.onSelectWatched(item.symbol)
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val symbol: TextView = view.watched_symbol
        val buy: TextView = view.watched_buy
        val sell: TextView = view.watched_sell
        val price: TextView = view.current_price
        val percentChange: TextView = view.percent_change
        val btnAction:MaterialButton = view.btn_buy_sell
        val companyName:TextView = view.company_name
        val h = view.watchlist_card
    }

}