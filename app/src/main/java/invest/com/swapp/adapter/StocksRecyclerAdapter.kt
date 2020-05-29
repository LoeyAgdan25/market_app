package invest.com.swapp.adapter

import android.content.Intent
import android.graphics.Color
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import invest.com.swapp.*
import invest.com.swapp.model.Stock
import invest.com.swapp.model.Stock2
import kotlinx.android.synthetic.main.stockitem_list_content.view.*

class StocksRecyclerAdapter(private val stocks:List<Stock2>) : RecyclerView.Adapter<StocksRecyclerAdapter.ViewHolder>(){

    private val onClickListener: View.OnClickListener
    init {
        onClickListener = View.OnClickListener { v ->
            val item = v.tag as Stock2
            Log.d("_details","${item.companyId} : ${item.securityID}")

            val intent = Intent(v.context, StockItemDetailActivity::class.java).apply {
                putExtra(StockItemDetailFragment.ARG_ITEM_ID, item.name)
                putExtra(StockItemDetailFragment.ARG_ITEM_SYMBOL, item.symbol)
                putExtra(StockItemDetailFragment.ARG_ITEM_NAME, item.name)
                putExtra(StockItemDetailFragment.ARG_ITEM_PERCENTAGE, item.percent_change)
                putExtra(StockItemDetailFragment.ARG_ITEM_VOLUME,item.volume)
                putExtra(StockItemDetailFragment.ARG_ITEM_PRICE,item.price)
                putExtra(StockItemDetailFragment.ARG_SEC_ID, item.securityID)
                putExtra(StockItemDetailFragment.ARG_COMP_ID, item.companyId)

            }
            v.context.startActivity(intent)


        }
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(p0.context)
                .inflate(R.layout.stockitem_list_content, p0, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = stocks.size

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val item = stocks[p1]
        p0.idView.text = item.symbol
        p0.contentView.text = item.name
        p0.percentView.text = item.percent_change
        p0.currentPrice.text = item.price


        if(item.percent_change.contains("-")){
            p0.imageIndicator.setImageResource(R.drawable.sort_down)
            p0.percentView.setBackgroundColor(Color.RED)
        }else{
            p0.imageIndicator.setImageResource(R.drawable.sort_up)
            p0.percentView.setBackgroundColor(Color.parseColor("#008b00"))

            if(item.percent_change == "0"){
                p0.percentView.setBackgroundColor(Color.parseColor("#FF9100"))
            }else{
                p0.percentView.text = "+${item.percent_change}"
            }
        }
        with(p0.itemView) {
            tag = item
            setOnClickListener(onClickListener)
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val idView: TextView = view.id_text
        val contentView: TextView = view.content
        val percentView: TextView = view.percent
        val imageIndicator: ImageView = view.indicator
        val currentPrice: TextView = view.tv_price
    }


}