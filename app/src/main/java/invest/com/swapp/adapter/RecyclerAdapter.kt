package invest.com.swapp.adapter

import android.support.annotation.NonNull
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import invest.com.swapp.R
import invest.com.swapp.Stock
import invest.com.swapp.inflate
import kotlinx.android.synthetic.main.recyclerview_item_row.view.*

class RecyclerAdapter(private val stocks:ArrayList<Stock>) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>(){

    override fun onCreateViewHolder(@NonNull p0: ViewGroup, p1: Int): ViewHolder {
        val inflatedView = p0.inflate(R.layout.recyclerview_item_row, false)
        return ViewHolder(inflatedView)
    }

    override fun getItemCount(): Int = stocks.size

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val stockItem = stocks[p1]
        p0.bindStock(stockItem)
    }


    class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view), View.OnClickListener{
        private var stock: Stock? = null
        init{
            view.setOnClickListener(this)
        }

        fun bindStock(stock: Stock){
            view.itemDescription.text = stock.description
            view.itemDate.text = stock.name
        }

        override fun onClick(p0: View?) {

        }
    }
}