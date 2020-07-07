package invest.com.swapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import kotlinx.android.synthetic.main.activity_trade.*
import kotlinx.android.synthetic.main.layout_buy_sell_prompt.view.*
import kotlinx.android.synthetic.main.layout_trade_prompt.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TradeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trade)


        fab_new_trade.setOnClickListener {
            var view: View = layoutInflater.inflate(R.layout.layout_trade_prompt,null)
            MaterialAlertDialogBuilder(TradeActivity@this,R.style.AlertDialogTheme).setTitle("Trade")
                    .setView(view)
                    .setPositiveButton("Save"){
                        dialog, which ->

                    }.show()


            var txtPrice = view.findViewById<TextInputEditText>(R.id.txt_buy_price1)
            var txtShares =  view.findViewById<TextInputEditText>(R.id.txt_shares)
            var lblTotal = view.findViewById<MaterialTextView>(R.id.lbl_total_value)
            var txtTax = view.findViewById<TextInputEditText>(R.id.txt_tax)
            var txtComm = view.findViewById<TextInputEditText>(R.id.txt_commission)
            var txtOthers = view.findViewById<TextInputEditText>(R.id.txt_others)

            var price = 0f
            var shares = 0f
            var tax = 0f
            var comm = 0f
            var others = 0f

            txtPrice.addTextChangedListener(object:TextWatcher{
                override fun afterTextChanged(p0: Editable?) {
                    price = if(txtPrice.text.isNullOrEmpty()){0f}else{txtPrice.text.toString().toFloat()}
                    shares = if(txtShares.text.isNullOrEmpty()){ 0f }else{ txtShares.text.toString().toFloat() }
                    var total = price * shares
                    lblTotal.setText(total.toString())
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
                    lblTotal.setText(total.toString())
                }
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

            })

            txtTax.addTextChangedListener(object:TextWatcher{
                override fun afterTextChanged(p0: Editable?) {
                    tax = if(txtTax.text.isNullOrEmpty()){0f}else{txtTax.text.toString().toFloat()}
                    lblTotal.setText(((price * shares) - tax).toString())
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

            })

            txtComm.addTextChangedListener(object:TextWatcher{
                override fun afterTextChanged(p0: Editable?) {
                    comm = if(txtComm.text.isNullOrEmpty()){0f}else{txtComm.text.toString().toFloat()}
                    lblTotal.setText(((price * shares) - (tax + comm)).toString())
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

            })

            txtOthers.addTextChangedListener(object:TextWatcher{
                override fun afterTextChanged(p0: Editable?) {
                    others = if(txtOthers.text.isNullOrEmpty()){0f}else{txtOthers.text.toString().toFloat()}
                    lblTotal.setText(((price * shares) - (tax + comm + others)).toString())
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

            })

        }
    }
}
