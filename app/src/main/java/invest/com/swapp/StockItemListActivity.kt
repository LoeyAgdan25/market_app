package invest.com.swapp

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import invest.com.swapp.db.DBHelper
import invest.com.swapp.db.database
import invest.com.swapp.dummy.DummyContent
import kotlinx.android.synthetic.main.activity_stockitem_list.*
import kotlinx.android.synthetic.main.stockitem_list_content.view.*
import kotlinx.android.synthetic.main.stockitem_list.*
import okhttp3.*
import org.jetbrains.anko.db.select
import org.jetbrains.anko.toast
import org.json.JSONObject
import java.io.IOException
import java.lang.Exception
import kotlin.coroutines.experimental.coroutineContext
import kotlin.system.exitProcess

class StockItemListActivity : AppCompatActivity() {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private var twoPane: Boolean = false
    private lateinit var client: OkHttpClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stockitem_list)
        setSupportActionBar(toolbar)

        this.supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        toolbar.title = title

        fab.setOnClickListener { view ->
            //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show()
            showPortfolio()
        }

        if (stockitem_detail_container != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            twoPane = true
        }
        client = OkHttpClient()
        setupRecyclerView(stockitem_list)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(
                R.menu.option_menu2,
                menu
        )
        val searchView = menu?.findItem(R.id.searchMenu)?.actionView as SearchView
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setOnSearchClickListener {
            //            LoadQuery("null")
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
            //            LoadQuery("%")
            false
        }
        return super.onCreateOptionsMenu(menu)
    }

//    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
//        return when(item!!.itemId){
//            R.id.logout_menu -> {
//                AppHelper.userPool!!.currentUser.signOut()
//                startActivity(Intent(baseContext,LoginActivity::class.java))
//                exitProcess(-1)
//                true
//            }
//            else -> super.onOptionsItemSelected(item)
//        }
//    }

    private fun filter(str: String){
        val filtered:List<Stock> = stockListAll.filter{it.name.contains(str,true)}
        Log.d("_filtered", filtered.toString())
        stockitem_list!!.adapter = SimpleItemRecyclerViewAdapter(this,ArrayList(filtered),true)
    }
    val stockListAll = ArrayList<Stock>()
    var recyclerView:RecyclerView? = null

    private fun setupRecyclerView(recyclerView: RecyclerView) {


        val urlRequest = Uri.Builder().scheme(StockItemListActivity.URL_SCHEME)
                .authority(StockItemListActivity.URL_AUTHORITY)
                .appendPath(StockItemListActivity.URL_PATH_1)
                .build().toString()
        val request = Request.Builder().url(urlRequest).build()
        client.newCall(request).enqueue(object : Callback{

            override fun onResponse(call: Call, response: Response) {
                var r = response.body()!!.string()
                try {
                    runOnUiThread {
                        val rootJsonObject = JSONObject(r)
                        var roots = rootJsonObject.getJSONArray("stock")
                        for (i in 0 until roots.length()) {
                            val stock = roots.get(i).toString()
                            val obj = JSONObject(stock)

                            val imageModel = Stock("${obj.getString("name")}",
                                    obj.getString("symbol"),"",
                                    obj.getString("percent_change"),
                                    obj.getString("volume"),
                                    obj.getJSONObject("price").getString("amount"))
                            stockListAll.add(imageModel)
                        }

                        recyclerView.adapter = SimpleItemRecyclerViewAdapter(this@StockItemListActivity,stockListAll,twoPane)

                        Log.d("_json", rootJsonObject.toString())
                        Log.d("_json", "date: " + rootJsonObject.getString("as_of"))
                    }
                }catch (e: Exception){
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                Log.d("_json", e.message)
            }
        })

    }

    class SimpleItemRecyclerViewAdapter(private val parentActivity: StockItemListActivity,
                                        private val values: ArrayList<Stock>,
                                        private val twoPane: Boolean) :
            RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>() {

        private val onClickListener: View.OnClickListener

        init {
            onClickListener = View.OnClickListener { v ->
                val item = v.tag as Stock
                if (twoPane) {
                    val fragment = StockItemDetailFragment().apply {
                        arguments = Bundle().apply {
                            putString(StockItemDetailFragment.ARG_ITEM_ID, item.name)
                            putString(StockItemDetailFragment.ARG_ITEM_NAME, item.description)
                            putString(StockItemDetailFragment.ARG_ITEM_PERCENTAGE, item.percent)
                            putString(StockItemDetailFragment.ARG_ITEM_VOLUME,item.volume)
                            putString(StockItemDetailFragment.ARG_ITEM_PRICE,item.price)
                        }
                    }
                    parentActivity.supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.stockitem_detail_container, fragment)
                            .commit()
                } else {
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
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.stockitem_list_content, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = values[position]
            holder.idView.text = item.symbol
            holder.contentView.text = item.name
            holder.percentView.text = item.percent

            if(item.percent.contains("-")){
                holder.imageIndicator.setImageResource(R.drawable.sort_down)
            }


            with(holder.itemView) {
                tag = item
                setOnClickListener(onClickListener)
            }
        }

        override fun getItemCount() = values.size

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val idView: TextView = view.id_text
            val contentView: TextView = view.content
            val percentView: TextView = view.percent
            val imageIndicator: ImageView = view.indicator
        }
    }

    companion object {
        private val URL_SCHEME = "http"
        private val URL_AUTHORITY = "phisix-api2.appspot.com"
        private val URL_PATH_1 = "stocks.json"
    }

    private fun showPortfolio(){

        val list = ArrayList<String>()

        database.use {
            select(DBHelper.tblInvestment,"symbol").exec {
                while (moveToNext()){
                    Log.d("_symbol", getString(getColumnIndex("symbol")) )
                    list.add(getString(getColumnIndex("symbol")))
                }
            }
        }


        if(list.size > 0){

            var array = arrayOfNulls<String>(list.size)
            list.toArray(array)

            Log.d("array", array.toString())

            var list = mutableListOf<Stock>()
            val filtered: List<Stock> = stockListAll.filter{array.contains(it.symbol)}

            Log.d("_list","${filtered.size} array size ${array!!.size} stock list ${stockListAll.size}" )
            stockitem_list!!.adapter = SimpleItemRecyclerViewAdapter(this,ArrayList(filtered),true)
        }


    }
}
