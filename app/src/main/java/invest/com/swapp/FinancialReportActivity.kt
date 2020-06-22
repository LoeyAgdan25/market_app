package invest.com.swapp

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.omapay.viewpagerfragmenttest.FragmentFundamental
import com.omapay.viewpagerfragmenttest.FragmentMain
import com.omapay.viewpagerfragmenttest.FragmentNews
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.anko.toast
import org.jsoup.Jsoup


class FinancialReportActivity : FragmentActivity(){

    private val NUM_PAGES = 3
    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_financial_report)



        viewPager = findViewById(R.id.pager)
        val pagerAdapter = ScreenSlidePagerAdapter(this)
        viewPager.adapter = pagerAdapter
        val pageMarginPx = 20
        val offsetPx = 30

        viewPager.setPageTransformer{ page, position ->
            val viewPager = page.parent.parent as ViewPager2
            val offset = position * -(2 * offsetPx + pageMarginPx)
            if (viewPager.orientation == ViewPager2.ORIENTATION_HORIZONTAL) {

                if (ViewCompat.getLayoutDirection(viewPager) == ViewCompat.LAYOUT_DIRECTION_RTL) {
                    page.translationX = -offset
                } else {
                    page.translationX = offset
                }

                page.apply {
                    val r = 1 - Math.abs(position)
                    page.alpha = 0.5f + r
                    page.scaleY = 0.75f + r * 0.25f
                }

            } else {
                page.translationY = offset
            }
        }

        //todo:- share this with news...
        //https://edge.pse.com.ph/companyInformation/form.do?cmpy_id=624
        var userAgent = "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.116 Safari/537.36"
        var link = "https://edge.pse.com.ph/companyPage/financial_reports_view.do?cmpy_id=624"

        GlobalScope.launch {
            try{


                var document = Jsoup.connect(link).userAgent(userAgent).get()

                var mainHeader =document.getElementsByClass("textCont")[1].text()
                Log.d("table", mainHeader)
                //this is annual
                var table = document.select("table")[0]
                var row = table.select("tr")

                Log.d("table","${table.allElements.count()}")
                Log.d("table","tag${table.getElementsByTag("caption").text()}")
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
                var isTr = table.select("tr")

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


    }

    override fun onBackPressed() {
        if(viewPager.currentItem == 0){
            super.onBackPressed()
        }else{
            viewPager.currentItem = viewPager.currentItem -1
        }
    }
}

class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
    override fun getItemCount(): Int = 3
    override fun createFragment(position: Int): Fragment {
        if(position == 2){
            return FragmentFundamental()
        }
        if(position == 1){
            return FragmentNews()
        }
        return FragmentMain()
    }
}
