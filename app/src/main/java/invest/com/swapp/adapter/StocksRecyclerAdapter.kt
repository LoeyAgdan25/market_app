package invest.com.swapp.adapter

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import invest.com.swapp.*
import kotlinx.android.synthetic.main.stockitem_list_content.view.*

class StocksRecyclerAdapter(private val stocks:ArrayList<Stock>) : RecyclerView.Adapter<StocksRecyclerAdapter.ViewHolder>(){

    private val onClickListener: View.OnClickListener
    init {
        onClickListener = View.OnClickListener { v ->
            val item = v.tag as Stock

            val intent = Intent(v.context, StockItemDetailActivity::class.java).apply {
                putExtra(StockItemDetailFragment.ARG_ITEM_ID, item.name)
                putExtra(StockItemDetailFragment.ARG_ITEM_SYMBOL, item.symbol)
                putExtra(StockItemDetailFragment.ARG_ITEM_NAME, item.name)
                putExtra(StockItemDetailFragment.ARG_ITEM_PERCENTAGE, item.percent)
                putExtra(StockItemDetailFragment.ARG_ITEM_VOLUME,item.volume)
                putExtra(StockItemDetailFragment.ARG_ITEM_PRICE,item.price)

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
        p0.percentView.text = item.percent + "%"
        p0.currentPrice.text = item.price + " PHP"


        if(item.percent.contains("-")){
            p0.imageIndicator.setImageResource(R.drawable.sort_down)
        }else{
            p0.imageIndicator.setImageResource(R.drawable.sort_up)
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