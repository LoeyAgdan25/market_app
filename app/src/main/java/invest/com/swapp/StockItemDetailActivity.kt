package invest.com.swapp

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_stockitem_detail.*
import org.jetbrains.anko.toast


class StockItemDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stockitem_detail)
        //setSupportActionBar(detail_toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own detail action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        // Show the Up button in the action bar.
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        supportActionBar!!.title = intent.getStringExtra(StockItemDetailFragment.ARG_ITEM_ID)


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
