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


class InvestmentActivity : FragmentActivity(){

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
