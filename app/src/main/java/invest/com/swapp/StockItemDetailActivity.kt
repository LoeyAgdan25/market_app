package invest.com.swapp

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import invest.com.swapp.db.database
import kotlinx.android.synthetic.main.activity_stockitem_detail.*
import kotlinx.android.synthetic.main.content_detail_stocks.*
import org.jetbrains.anko.custom.async
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import java.util.*


class StockItemDetailActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stockitem_detail)
        setSupportActionBar(toolbar)

        var symbol = intent.getStringExtra(StockItemDetailFragment.ARG_ITEM_SYMBOL)
        var name = intent.getStringExtra(StockItemDetailFragment.ARG_ITEM_NAME)
        var volume = intent.getStringExtra(StockItemDetailFragment.ARG_ITEM_VOLUME)
        var percent = intent.getStringExtra(StockItemDetailFragment.ARG_ITEM_PERCENTAGE)
        var price = intent.getStringExtra(StockItemDetailFragment.ARG_ITEM_PRICE)
        var status = intent.getStringExtra(StockItemDetailFragment.ARG_ITEM_STATUS)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "${name}"


        txt_stock_symbol.text = "${symbol}"
        txt_stock_percent.text = "${percent}"
        txt_stock_price.text = "${price}"
        txt_stock_status.text = "${status}"
        txt_stock_volume.text = "${volume}"

        if (savedInstanceState == null) {
            val fragment = StockItemDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(StockItemDetailFragment.ARG_ITEM_ID,
                            intent.getStringExtra(StockItemDetailFragment.ARG_ITEM_ID))
                }
            }

            supportFragmentManager.beginTransaction()
                    .add(R.id.stockitem_detail_container, fragment)
                    .commit()
        }

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own detail action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        btn_watch_stock.setOnClickListener { doWatchStock() }
    }

    fun doWatchStock(){

        Log.d("event","watching stock");

//        doAsync {
            database.use {
                insert("tblWatched",
                        "symbol" to txt_stock_symbol.text,
                        "name" to intent.getStringExtra(StockItemDetailFragment.ARG_ITEM_NAME),
                        "currency" to "PHP",
                        "amount" to txt_stock_price.text,
                        "volume" to txt_stock_volume.text
                )

                toast("stock is saved!")
            }
//        }
    }

    override fun onOptionsItemSelected(item: MenuItem) =
            when (item.itemId) {
                android.R.id.home -> {
                    navigateUpTo(Intent(this, StockItemListActivity::class.java))
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }
}
