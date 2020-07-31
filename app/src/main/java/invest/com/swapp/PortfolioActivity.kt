package invest.com.swapp

import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.snackbar.Snackbar
import invest.com.swapp.adapter.PortfolioAdapter
import invest.com.swapp.adapter.TradeListAdapter
import invest.com.swapp.model.StockPortfolio
import invest.com.swapp.model.StockTrade
import invest.com.swapp.viewmodel.TradeViewModel
import kotlinx.android.synthetic.main.activity_portfolio.*
import kotlinx.android.synthetic.main.activity_trade.*
import kotlinx.android.synthetic.main.content_portfolio.*


class PortfolioActivity : AppCompatActivity() , OnChartValueSelectedListener, AdapterView.OnItemSelectedListener{

    private lateinit var stockTrade:ArrayList<StockPortfolio>
    private lateinit var stockTradeViewModel: TradeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_portfolio)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        portfolio_pie_chart.setUsePercentValues(true)
        val xvalues = ArrayList<PieEntry>()
        xvalues.add(PieEntry(34.0f, "CEB"))
        xvalues.add(PieEntry(28.2f, "SSC"))
        xvalues.add(PieEntry(37.9f, "JFC"))
        val dataSet = PieDataSet(xvalues, "Stocks")
        val data = PieData(dataSet)

        dataSet.colors = ColorTemplate.COLORFUL_COLORS.toList()
        data.setValueFormatter(PercentFormatter())

        portfolio_pie_chart.data = data
        portfolio_pie_chart.description.text = ""
        portfolio_pie_chart.isDrawHoleEnabled = false
        data.setValueTextSize(13f)

        portfolio_pie_chart.setOnChartValueSelectedListener(this)
        chartDetails(portfolio_pie_chart, Typeface.SANS_SERIF)

        stockTrade = ArrayList()

        var linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        portfolio_recycler.layoutManager = linearLayoutManager
        var portfolio = PortfolioAdapter(stockTrade)
        portfolio_recycler.adapter = portfolio

        stockTradeViewModel = ViewModelProviders.of(this).get(TradeViewModel::class.java)
        stockTradeViewModel.portfolio.observe(this, Observer { it ->
            stockTrade.clear()
            stockTrade.addAll(it)
            portfolio_recycler!!.adapter!!.notifyDataSetChanged()
        })

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }

    fun chartDetails(mChart: PieChart, tf: Typeface) {
        mChart.description.isEnabled = true
        mChart.centerText = ""
        mChart.setCenterTextSize(10F)
        mChart.setCenterTextTypeface(tf)
        val l = mChart.legend
        mChart.legend.isWordWrapEnabled = true
        mChart.legend.isEnabled = true
        l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        l.formSize = 20F
        l.formToTextSpace = 5f
        l.form = Legend.LegendForm.SQUARE
        l.textSize = 12f
        l.orientation = Legend.LegendOrientation.HORIZONTAL
        l.isWordWrapEnabled = true
        l.setDrawInside(false)
        mChart.setTouchEnabled(false)
        mChart.setDrawEntryLabels(false)
        mChart.legend.isWordWrapEnabled = true
        mChart.setExtraOffsets(20f, 0f, 20f, 0f)
        mChart.setUsePercentValues(true)
        // mChart.rotationAngle = 0f
        mChart.setUsePercentValues(true)
        mChart.setDrawCenterText(false)
        mChart.description.isEnabled = true
        mChart.isRotationEnabled = false}

    override fun onNothingSelected() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {


    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) { }

}

private fun Legend.setCustom(vordiplomColors: IntArray?, arrayOf: Array<String>) {

}
