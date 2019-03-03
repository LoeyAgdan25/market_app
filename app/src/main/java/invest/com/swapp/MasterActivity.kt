package invest.com.swapp

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.SearchView

class MasterActivity : AppCompatActivity(){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_master)
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



}
