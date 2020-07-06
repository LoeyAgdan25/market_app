package invest.com.swapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_trade.*
import kotlinx.android.synthetic.main.layout_buy_sell_prompt.view.*
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
        }
    }
}
