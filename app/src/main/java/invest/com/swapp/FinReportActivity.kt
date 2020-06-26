package invest.com.swapp

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import invest.com.swapp.repository.FundamentalRow
import invest.com.swapp.viewmodel.FundamentalModel
import kotlinx.android.synthetic.main.activity_fin_report.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FinReportActivity : AppCompatActivity() {

    private lateinit var fundamentalViewModel:FundamentalModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fin_report)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        tv_fin_header.text = intent.getStringExtra("cpn") + "\nFinancial Reports"

        fundamentalViewModel = ViewModelProviders.of(this).get(FundamentalModel::class.java)
        GlobalScope.launch { fundamentalViewModel.getFundamentals(intent.getIntExtra("cpid",0)) }

        //build table view in kotlin...
        fundamentalViewModel.funRow.observe(this, Observer {
            createTable(it)
        })

    }


    fun createTable(rows: List<FundamentalRow>) {

        var tableBalance = tbl_balance_sheet
        tbl_balance_sheet.removeAllViews()

        for(i in rows){

            val row = TableRow(this)
            row.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT)
                val label0 = TextView(this)

            var params = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT)
            params.setMargins(0,0,0,0)

                label0.apply {

                    layoutParams = params
                    if(i.ps != 0){
                        background = getDrawable(R.drawable.table_no_border)
                        setTextColor(Color.parseColor("#424242"))
                    }else{
                        setTextColor(Color.BLACK)
                    }

                    text = "${i.h}"
                }
                row.addView(label0)


                val label1 = TextView(this)
                label1.apply {
                    layoutParams = params
                    textAlignment = View.TEXT_ALIGNMENT_TEXT_END

                    if(i.ps != 0){
                        background = getDrawable(R.drawable.table_border)
                    }else{
                        setTextColor(Color.BLACK)
                    }
                    text = " ${i.c} "
                }
                row.addView(label1)

                val label2 = TextView(this)
                label2.apply {
                    layoutParams = params
                    textAlignment = View.TEXT_ALIGNMENT_TEXT_END

                    if(i.ps != 0){
                        background = getDrawable(R.drawable.table_border)

                    }else{
                        setTextColor(Color.BLACK)
                    }
                    text = " ${i.p} "
                }
                row.addView(label2)

            tableBalance.addView(row)
        }

    }
}
