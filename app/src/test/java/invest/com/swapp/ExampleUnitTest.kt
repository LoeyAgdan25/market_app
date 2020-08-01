package invest.com.swapp

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun get_count_per_percentage(){
        var total_price = 1000f


        var total_share = 100f
        var total_share_sell = 50f

        //check how many percent of sell is the total price

        var percentage_less = total_share_sell / 100f
        var get_percent_of_total = percentage_less * total_price
        var total_remain = total_price - get_percent_of_total


        assertEquals(50f, total_share - total_share_sell)
        assertEquals(0.50f, percentage_less)
        assertEquals(500f, get_percent_of_total)
        assertEquals(500f, total_remain)
    }

    @Test
    fun get_update_total_shares_and_investment(){

        /*
        *   Getting remaining price based on number of shares deducted
        * */

        var total_price = 7750f
        var total_share = 200f
        var total_share_sell = 50f

        var percentage = total_share_sell / total_share
        var get_percent_of_price_less = total_price * percentage
        var remaining_price = total_price - get_percent_of_price_less

        assertEquals(0.25f,percentage)
        assertEquals(1937.5f, get_percent_of_price_less)
        assertEquals( 5812.5f, remaining_price)

        // =======

        var total_price2 = 1000f
        var total_share2 = 100f
        var total_share_sell2 = 50f

        var percentage2 = total_share_sell2 / total_share2
        var get_percent_of_price_less2 = total_price2 * percentage2
        var remaining_price2 = total_price2 - get_percent_of_price_less2

        assertEquals(0.50f,percentage2)
        assertEquals(500f, get_percent_of_price_less2)
        assertEquals( 500f, remaining_price2)

    }

    @Test
    fun compute_pie_distribution(){
        //get all total in portfolio
        //get percentage of each portfolio

        var totalPortfolioValue = 7750f

        var cebPercent = 1200f
        var sscPercent = 3000f
        var jfcPercent = 2000f
        var merPercent = 30f
        var hlcmPercent = 1520f

        assertEquals(totalPortfolioValue, cebPercent + sscPercent + jfcPercent + merPercent + hlcmPercent)

        var ceb = (cebPercent / totalPortfolioValue) * 100   // percentage in total amount of shares
        var ssc = (sscPercent / totalPortfolioValue) * 100
        var jfc = (jfcPercent / totalPortfolioValue) * 100
        var mer = (merPercent / totalPortfolioValue) * 100
        var hlcm = (hlcmPercent / totalPortfolioValue) * 100

        //get total pie value in percentage

        assertEquals(100f , ceb + ssc + jfc + mer + hlcm)


        var cebPercentNew = 1200f // check changed in percent if how many percent shrunk or shrink
        var cebPercentGain = 1200f * 2f // for 100% gain
        var cebLoss = 600f

        //var totalGainLoss = cebPercentNew - cebPercent
        //get cebloss / percent...

        var gainNoChanged = ((cebPercentNew - cebPercent) / cebPercent) * 100
        var gain = ((cebPercentGain - cebPercent) / cebPercent) * 100
        var loss = ((cebLoss - cebPercent) / cebPercent) * 100

        assertEquals(0f, gainNoChanged) // no changed
        assertEquals(100f, gain) //gain
        assertEquals(-50f, loss)

    }

    @Test
    fun compute_current_market_share_value(){
        //compute current market value
        //and percentage changed...
        //get current market value...

    }
}
