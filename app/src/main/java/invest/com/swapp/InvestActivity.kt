package invest.com.swapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class InvestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invest)

        supportActionBar!!.title = intent.getStringExtra("symbol")
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }
}
