package invest.com.swapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.RadioGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.ViewModelStores
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import invest.com.swapp.adapter.TradeListAdapter
import invest.com.swapp.helper.UtilityHelper
import invest.com.swapp.listener.TradeListener
import invest.com.swapp.model.StockTrade
import invest.com.swapp.viewmodel.TradeViewModel
import kotlinx.android.synthetic.main.activity_trade.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.anko.longToast
import org.jetbrains.anko.toast

class TradeActivity : AppCompatActivity(), TradeListener {


    private lateinit var stockTrade:ArrayList<StockTrade>
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var stockTradeViewModel: TradeViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trade)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        stockTrade = ArrayList()
//        stockTrade.add(StockTrade("CEB",1,0f,0f,0f,0f,0f, 0f,0f,0f,"","",""))
//        stockTrade.add(StockTrade("SCC",1,0f,0f,0f,0f,0f, 0f,0f,0f,"","",""))
//        stockTrade.add(StockTrade("MER",1,0f,0f,0f,0f,0f, 0f,0f,0f,"","",""))


        linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyler_trades_list.layoutManager = linearLayoutManager
        var tradeAdapter = TradeListAdapter(stockTrade)
        tradeAdapter.tradeListener = this
        recyler_trades_list.adapter = tradeAdapter


        stockTradeViewModel = ViewModelProviders.of(this).get(TradeViewModel::class.java)
        stockTradeViewModel.trades.observe(this, Observer {
            stockTrade.clear()
            stockTrade.addAll(it)
            recyler_trades_list!!.adapter!!.notifyDataSetChanged()
        })

        radio_sort_trade.setOnCheckedChangeListener { p0, p1 ->
            if(p1 == R.id.radio_sort_all){
                recyler_trades_list!!.adapter = TradeListAdapter(stockTrade)
                recyler_trades_list!!.adapter!!.notifyDataSetChanged()
            }else if(p1 == R.id.radio_sort_buy){
                val filtered = stockTrade.filter { it.type == 0 }
                recyler_trades_list!!.adapter = TradeListAdapter(ArrayList(filtered))
                recyler_trades_list!!.adapter!!.notifyDataSetChanged()
            }else if(p1 == R.id.radio_sort_sell){
                val filtered = stockTrade.filter { it.type == 1 }
                recyler_trades_list!!.adapter = TradeListAdapter(ArrayList(filtered))
                recyler_trades_list!!.adapter!!.notifyDataSetChanged()
            }
        }


        fab_new_trade.setOnClickListener {
            //0 - Add
            //1 - Modify
            AlertUtil().showTradeInput(this, stockTradeViewModel,0)

        }
    }

    fun sort(category: Int){
        if(category == 1){


        }
    }

    override fun onEdit(trade: StockTrade) {
        toast("edit ${trade.id}")
    }

    override fun onDelete(trade: StockTrade) {
        //toast("delete ${trade.id}")
        GlobalScope.launch {
            stockTradeViewModel.deleteTrade(trade)
        }
    }
}

class AlertUtil{
    fun showTradeInput(ctx: TradeActivity, stockTradeViewModel: TradeViewModel, modify:Int){
        var type = 0;

        var view: View = ctx.layoutInflater.inflate(R.layout.layout_trade_prompt,null)
        var buysellGroup = view.findViewById<RadioGroup>(R.id.radio_group_buy_sell)
        var txtPrice = view.findViewById<TextInputEditText>(R.id.txt_buy_price1)
        var txtShares =  view.findViewById<TextInputEditText>(R.id.txt_shares)
        var lblTotal = view.findViewById<MaterialTextView>(R.id.lbl_total_value)
        var txtTax = view.findViewById<TextInputEditText>(R.id.txt_tax)
        var txtComm = view.findViewById<TextInputEditText>(R.id.txt_commission)
        var txtOthers = view.findViewById<TextInputEditText>(R.id.txt_others)
        var txtStockCode = view.findViewById<TextInputEditText>(R.id.txt_stock_code_trade)
        var txtReasons = view.findViewById<TextInputEditText>(R.id.txt_reason)

        buysellGroup.setOnCheckedChangeListener { p0, p1 ->
            if(p1.equals(R.id.radioSell)){
                type = 1
            }else{
                type = 0
            }
        }

        var price = 0f
        var shares = 0f
        var tax = 0f
        var comm = 0f
        var others = 0f

        MaterialAlertDialogBuilder(ctx,R.style.AlertDialogTheme).setTitle("Trade")
                .setView(view)
                .setCancelable(false)
                .setNegativeButton("Cancel"){
                    dialog, which ->
                    dialog.dismiss()
                }
                .setPositiveButton("Save"){
                    dialog, which ->
                    var stockTradeNew = StockTrade(0
                            ,txtStockCode.text.toString()
                            ,type,0f
                            ,txtPrice.text.toString().replace(",","").toFloat()
                            ,lblTotal.text.toString().replace(",","").toFloat()
                            ,0f,txtShares.text.toString().toFloat()
                            ,comm
                            ,tax
                            ,others,txtReasons.text.toString(),"","",""
                    )
                    stockTradeViewModel.saveTrade(stockTradeNew)
                    ctx.longToast("trade is saved.")
                }.show()


        txtPrice.addTextChangedListener(object:TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                price = if(txtPrice.text.isNullOrEmpty()){0f}else{txtPrice.text.toString().toFloat()}
                shares = if(txtShares.text.isNullOrEmpty()){ 0f }else{ txtShares.text.toString().toFloat() }
                var total = price * shares
                //UtilityHelper.getInstance().formatCurrency(
                lblTotal.text = UtilityHelper.getInstance().formatCurrency(total.toString().toFloat())
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {


            }
        })

        txtShares.addTextChangedListener(object:TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                price = if(txtPrice.text.isNullOrEmpty()){ 0f }else{txtPrice.text.toString().toFloat()}
                shares = if(txtShares.text.isNullOrEmpty()){ 0f }else{ txtShares.text.toString().toFloat() }
                var total = price * shares
                Log.d("_total", "" + total + " price & shares" + price + " " + shares)
                lblTotal.text = UtilityHelper.getInstance().formatCurrency(total.toString().toFloat())
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

        })

        txtTax.addTextChangedListener(object:TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                tax = if(txtTax.text.isNullOrEmpty()){0f}else{txtTax.text.toString().toFloat()}
                lblTotal.setText(UtilityHelper.getInstance().formatCurrency(((price * shares) - tax)))
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })

        txtComm.addTextChangedListener(object:TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                comm = if(txtComm.text.isNullOrEmpty()){0f}else{txtComm.text.toString().toFloat()}
                lblTotal.text = UtilityHelper.getInstance().formatCurrency(((price * shares) - (tax + comm)))
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })

        txtOthers.addTextChangedListener(object:TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                others = if(txtOthers.text.isNullOrEmpty()){0f}else{txtOthers.text.toString().toFloat()}
                lblTotal.text = UtilityHelper.getInstance().formatCurrency((price * shares) - (tax + comm + others))
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
        })
    }
}
