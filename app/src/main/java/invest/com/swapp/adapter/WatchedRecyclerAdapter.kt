package invest.com.swapp.adapter

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import invest.com.swapp.R
import invest.com.swapp.listener.WatchListener
import invest.com.swapp.model.StocksWatched
import kotlinx.android.synthetic.main.watch_list_content.view.*

class WatchedRecyclerAdapter(private val stocks:List<StocksWatched>, private val context: Context) : RecyclerView.Adapter<WatchedRecyclerAdapter.ViewHolder>(){

    var watchListener:WatchListener? = null
    private val onClickListener: View.OnClickListener
    init {
        onClickListener = View.OnClickListener { v ->
            val item = v.tag as StocksWatched
//            Log.d("_details","${item.companyId} : ${item.securityID}")
//            val intent = Intent(v.context, StockItemDetailActivity::class.java).apply {
//                putExtra(StockItemDetailFragment.ARG_ITEM_ID, item.name)
//                putExtra(StockItemDetailFragment.ARG_ITEM_SYMBOL, item.symbol)
//                putExtra(StockItemDetailFragment.ARG_ITEM_NAME, item.name)
//                putExtra(StockItemDetailFragment.ARG_ITEM_PERCENTAGE, item.percent_change)
//                putExtra(StockItemDetailFragment.ARG_ITEM_VOLUME,item.volume)
//                putExtra(StockItemDetailFragment.ARG_ITEM_PRICE,item.price)
//                putExtra(StockItemDetailFragment.ARG_SEC_ID, item.securityID)
//                putExtra(StockItemDetailFragment.ARG_COMP_ID, item.companyId)
//            }
//            v.context.startActivity(intent)
        }

        //watchListener = this.watchListener

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
        p0.price.text = item.price // database not resetting...
        p0.percentChange.text = item.percent_change
        p0.companyName.text = item.name

        if(item.percent_change.contains("-")){
            p0.percentChange.setBackgroundColor(Color.RED)
        }else{
            p0.percentChange.setBackgroundColor(context.getColor(R.color.colorPrimary))

            if(item.percent_change == "0"){
                p0.percentChange.setBackgroundColor(Color.parseColor("#FF9100"))
            }else{
                p0.percentChange.text = "+${item.percent_change}"
            }
        }

        p0.btnAction.setOnClickListener {
                watchListener!!.onWatchedAction(item)
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
    }

}