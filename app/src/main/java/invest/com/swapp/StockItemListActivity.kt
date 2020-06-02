package invest.com.swapp

import android.app.SearchManager
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.*
import android.widget.SearchView
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import invest.com.swapp.adapter.StocksRecyclerAdapter
import invest.com.swapp.model.Stock2
import invest.com.swapp.model.StocksWatched
import invest.com.swapp.viewmodel.StocksViewModel
import kotlinx.android.synthetic.main.activity_stockitem_list.*
import kotlinx.android.synthetic.main.stockitem_list.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

//todo:- correct the data from api

class StockItemListActivity : AppCompatActivity() {

    private var twoPane: Boolean = false
    private lateinit var stockViewModel:StocksViewModel
    private lateinit var stockListAll:List<Stock2>

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stockitem_list)
        setSupportActionBar(toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        window.statusBarColor = this.getColor(R.color.colorPrimary)
        title = ""

        if (stockitem_detail_container != null) {
            twoPane = true
        }

        stockViewModel = ViewModelProviders.of(this).get(StocksViewModel::class.java)
        stockViewModel.stocks.observe(this, androidx.lifecycle.Observer {
            stocks ->
            stocks.let {
                //todo:- do filtering here...
                stockitem_list!!.adapter = StocksRecyclerAdapter(it)
                stockListAll = it //for query search
            }
        })

        GlobalScope.launch {
            stockViewModel.getStocks()
        }
    }

    //search menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu2,menu)

        val searchView = menu?.findItem(R.id.searchMenu)?.actionView as SearchView
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setOnSearchClickListener {

        }
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    if (query.isNotEmpty()) filter("$query")
                    if (query.isEmpty()) filter("null")
                    Log.d("_query","doing some query")
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    if (newText.length > 1) filter("$newText")
                    if (newText.isEmpty()) filter("null")

                    Log.d("_query","doing query onchange" + newText);
                }
                return false
            }
        })
        searchView.setOnCloseListener {
            //stockitem_list!!.adapter = StocksRecyclerAdapter( stockListAll)
            false
        }
        return super.onCreateOptionsMenu(menu)
    }

    //filter menu
    private fun filter(str: String){
        val filtered:List<Stock2> = stockListAll.filter{it.name.contains(str,true)}
        Log.d("_filtered", filtered.toString())
        stockitem_list!!.adapter = StocksRecyclerAdapter(ArrayList(filtered))
    }
}
