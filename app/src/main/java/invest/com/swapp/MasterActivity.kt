package invest.com.swapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import invest.com.swapp.adapter.RecyclerAdapter
import invest.com.swapp.auth.LoginActivity
import kotlinx.android.synthetic.main.activity_master.*
import kotlin.system.exitProcess

class MasterActivity : AppCompatActivity(){

    private var stockList: ArrayList<Stock> = ArrayList()
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: RecyclerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_master)
        AppHelper.init(baseContext)

        linearLayoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        recyclerViewMain.layoutManager = linearLayoutManager
        adapter = RecyclerAdapter(stockList)
        recyclerViewMain.adapter = adapter


        btn_dashboard_search.setOnClickListener { doSearchStock() }

    }

    fun doSearchStock(){
        startActivity(Intent(baseContext, StockItemListActivity::class.java))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(
                R.menu.option_menu,
                menu
        )
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item!!.itemId){
            R.id.logout_menu -> {
                AppHelper.userPool!!.currentUser.signOut()
                startActivity(Intent(baseContext, LoginActivity::class.java))
                exitProcess(-1)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }



}
