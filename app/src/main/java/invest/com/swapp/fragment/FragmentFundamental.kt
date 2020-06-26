package com.omapay.viewpagerfragmenttest

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import invest.com.swapp.R
import kotlinx.android.synthetic.main.activity_invest.*
import kotlinx.android.synthetic.main.activity_stockitem_detail.*
import kotlinx.android.synthetic.main.fragment_fundamental.*
import kotlinx.coroutines.*
import org.jsoup.Jsoup
import org.jsoup.select.Elements

class FragmentFundamental : Fragment() {
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View?  = inflater.inflate(R.layout.fragment_fundamental, container, false)

    lateinit var cardHolder:CardView

    //todo: https://stackoverflow.com/questions/59491707/how-to-wait-for-end-of-a-coroutine

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.fragment_fundamental, container, false)


        cardHolder = view.findViewById(R.id.card_holder_fundamental)
        var rowDetail = ArrayList<FinRow>()


        val lp = TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        tableLayout.apply {
            layoutParams = lp
            isShrinkAllColumns = true
        }

        rowDetail.add(FinRow("test","test","test"))



        //todo:- share this with news...
        //https://edge.pse.com.ph/companyInformation/form.do?cmpy_id=624
        var userAgent = "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.116 Safari/537.36"
        var link = "https://edge.pse.com.ph/companyPage/financial_reports_view.do?cmpy_id=624"

        val value = GlobalScope.async {
            try{
//                delay(1000)

                var document = Jsoup.connect(link).userAgent(userAgent).get()

                var mainHeader =document.getElementsByClass("textCont")[1].text()
                Log.d("table", mainHeader)
                //this is annual
                var tableAsset = document.select("table")[0]
                var row:Elements = tableAsset.select("tr")



                Log.d("table","${tableAsset.allElements.count()}")
                Log.d("table","tag${tableAsset.getElementsByTag("caption").text()}")
                Log.d("table","row" + row.size)
                Log.d("table", "current assets" + row[1].getElementsByTag("th").text())
                Log.d("table", "current assets" + row[1].getElementsByTag("td")[0].text())
                Log.d("table", "current assets" + row[1].getElementsByTag("td")[1].text())
                Log.d("table", "total assets" + row[2].getElementsByTag("th").text())
                Log.d("table", "total assets" + row[2].getElementsByTag("td")[0].text())
                Log.d("table", "total assets" + row[2].getElementsByTag("td")[1].text())
                Log.d("table", "current liabilities" + row[3].getElementsByTag("th").text())
                Log.d("table", "current liabilities" + row[3].getElementsByTag("td")[0].text())
                Log.d("table", "current liabilities" + row[3].getElementsByTag("td")[1].text())
                Log.d("table", "total liabilities" + row[3].getElementsByTag("th").text())
                Log.d("table", "total liabilities" + row[3].getElementsByTag("td")[0].text())
                Log.d("table", "total liabilities" + row[3].getElementsByTag("td")[1].text())
                Log.d("table", "retained earnings" + row[4].getElementsByTag("th").text())
                Log.d("table", "retained earnings" + row[4].getElementsByTag("td")[0].text())
                Log.d("table", "retained earnings" + row[4].getElementsByTag("td")[1].text())
                Log.d("table", "Stockholders' Equity" + row[5].getElementsByTag("th").text())
                Log.d("table", "Stockholders' Equity" + row[5].getElementsByTag("td")[0].text())
                Log.d("table", "Stockholders' Equity" + row[5].getElementsByTag("td")[1].text())
                Log.d("table", "Stockholders' Equity - Parent" + row[6].getElementsByTag("th").text())
                Log.d("table", "Stockholders' Equity - Parent" + row[6].getElementsByTag("td")[0].text())
                Log.d("table", "Stockholders' Equity - Parent" + row[6].getElementsByTag("td")[1].text())
                Log.d("table", "Book Value Per Share" + row[7].getElementsByTag("th").text())
                Log.d("table", "Book Value Per Share" + row[7].getElementsByTag("td")[0].text())
                Log.d("table", "Book Value Per Share" + row[7].getElementsByTag("td")[1].text())
                var tableIncomeStatement = document.select("table")[1]
                var isTr = tableAsset.select("tr")

                Log.d("table","tag${tableIncomeStatement.getElementsByTag("caption").text()}")
                Log.d("table","Gross Revenue" + isTr[1].getElementsByTag("th").text())
                Log.d("table", "Gross Revenue" + isTr[1].getElementsByTag("td")[0].text())
                //todo use for each then get count

                //this quarterly
                var tableQuarterBalanceSheet = document.select("table")[2]
                var tableQIncomeStatement = document.select("table")[3]

            }catch (ex: Exception){
                ex.printStackTrace()
            }
        }
        createTable(rowDetail)
       //GlobalScope.launch {  value.await() }

        return view
    }

    var res = ""

    suspend fun updateUi(){
        val value = GlobalScope.async { // creates worker thread

        }
        println(value.await()) //waits for workerthread to finish
        //button.isEnabled = true //runs on ui thread as calling function is on Dispatchers.main
    }

    val ROWS = 9
    val COLUMNS = 3
    val tableLayout by lazy { TableLayout(requireActivity()) }

    //fun createTable(rows: Int, cols: Int) {
    fun createTable(rows: ArrayList<FinRow>) {
        //Toast.makeText(requireContext(), "$rows.size" , Toast.LENGTH_LONG).show()

        Log.d("tableSize", "$rows")
        for (i in 1 until 9) {

            val row = TableRow(requireActivity())
            row.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT)

            for (j in 1 until 3) {

                val button = Button(requireActivity())
                button.apply {
                    layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                            TableRow.LayoutParams.WRAP_CONTENT)
                    text = "R $i C $j"
                }
                row.addView(button)
            }
            tableLayout.addView(row)
        }
        //linearLayout3.addView(tableLayout)
        cardHolder.addView(tableLayout)

    }

    override fun onResume() {
        super.onResume()

    }
}

class FinRow(th:String, curr:String, prev: String)