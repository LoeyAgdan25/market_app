package invest.com.swapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import invest.com.swapp.db.DBHelper
import invest.com.swapp.db.database
import kotlinx.android.synthetic.main.activity_invest.*
import org.jetbrains.anko.db.insert
import java.util.*

class InvestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invest)

        supportActionBar!!.title = intent.getStringExtra("symbol")
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)


        txt_price_sold.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int,count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int,before: Int, count: Int) {doCompute()}
        })

        txt_stocks_sold.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int,count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int,before: Int, count: Int) {doCompute()}
        })

        txt_broker_charge.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int,count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int,before: Int, count: Int) {doCompute()}
        })

        txt_tax_charge.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int,count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int,before: Int, count: Int) {doCompute()}
        })

    }

    fun doCompute(){

        Log.d("_log","doCompute()")

        var price = 0.00
        var stock = 0.00
        var amount = 0.00
        var charge = 0.00
        var tax = 0.00

        if(txt_price_sold.text.isEmpty()){
            price = 0.00
        }else{
            price = txt_price_sold.text.toString().toDouble()
        }

        if(txt_stocks_sold.text.isEmpty()){
            stock = 0.00
        }else{
            stock = txt_stocks_sold.text.toString().toDouble()
        }

        if(txt_broker_charge.text.isEmpty()){
            stock = 0.00
        }else{
            stock = txt_broker_charge.text.toString().toDouble()
        }

        if(txt_tax_charge.text.isEmpty()){
            tax = 0.00
        }else{
            tax = txt_tax_charge.text.toString().toDouble()
        }

        Log.d("_logs", "$price $stock")
        amount = (price * stock)
        txt_amount_sold.setText("$amount")


        var allTotal = (price * stock) - (charge + tax)
        txt_total_investment.setText("$allTotal")

    }

    fun doSaveInvestment(){
        database.use {
            insert(DBHelper.tblInvestment,
                "symbol" to intent.getStringExtra("symbol"),
                    "price" to txt_price_sold.text as Double,
                    "stocks" to txt_stocks_sold.text as Double,
                    "amount" to txt_amount_sold.text as Double,
                    "bcharge" to txt_broker_charge.text as Double,
                    "tax" to txt_tax_charge.text as Double,
                    "total" to txt_total_investment.text as Double,
                    "date" to Date().toString()
                    )

        }
    }

}
