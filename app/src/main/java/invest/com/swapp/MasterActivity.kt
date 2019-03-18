package invest.com.swapp

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.*
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import kotlinx.android.synthetic.main.activity_master.*
import okhttp3.*
import java.io.IOException
import kotlin.system.exitProcess

class MasterActivity : AppCompatActivity(){

    private var stockList: ArrayList<Stock> = ArrayList()
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: RecyclerAdapter
    private lateinit var stockRequester:StockRequester
    private lateinit var client: OkHttpClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_master)
        AppHelper.init(baseContext)

        linearLayoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        recyclerView.layoutManager = linearLayoutManager

        adapter = RecyclerAdapter(stockList)
        recyclerView.adapter = adapter

        //stockRequester = StockRequester(this)
        client = OkHttpClient()

        processFetch()

        btn_dashboard_search.setOnClickListener { doSearchStock() }

    }

    fun doSearchStock(){
        startActivity(Intent(baseContext, StockItemListActivity::class.java))
    }

    fun processFetch(){
        val urlRequest = Uri.Builder().scheme(MasterActivity.URL_SCHEME)
                .authority(MasterActivity.URL_AUTHORITY)
                .appendPath(MasterActivity.URL_PATH_1)
                .build().toString()

        val request = Request.Builder().url(urlRequest).build()
        //isLoadingData = false

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                Log.d("_json", response.body()!!.string())


            }

            override fun onFailure(call: Call, e: IOException) {
                Log.d("_json", e.message)
            }
        })
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(
                R.menu.option_menu,
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
//                    if (query.isNotEmpty()) LoadQuery("%$query%")
//                    if (query.isEmpty()) LoadQuery("null")

                    Log.d("_query","doing some query")
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
//                    if (newText.length > 1) LoadQuery("%$newText%")
//                    if (newText.isEmpty()) LoadQuery("null")
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

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item!!.itemId){
            R.id.logout_menu -> {
                AppHelper.userPool!!.currentUser.signOut()
                startActivity(Intent(baseContext,LoginActivity::class.java))
                exitProcess(-1)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        private val URL_SCHEME = "http"
        private val URL_AUTHORITY = "phisix-api2.appspot.com"
        private val URL_PATH_1 = "stocks.json"

    }


}
