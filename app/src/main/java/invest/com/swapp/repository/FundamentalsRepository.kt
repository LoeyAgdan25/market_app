package invest.com.swapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import org.jsoup.select.Elements

//add daos in constructor

class FundamentalsRepository {

//todo add extra heder for period end, fiscal, + two colums on quartery income statement

    suspend fun retrieveFinancial(cpid: Int): List<FundamentalRow>{
        val fundamentals = ArrayList<FundamentalRow>()

            val getData = GlobalScope.async{
                try{
                    var userAgent = "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.116 Safari/537.36"
                    var link = "https://edge.pse.com.ph/companyPage/financial_reports_view.do?cmpy_id=$cpid"

                    var document = Jsoup.connect(link).userAgent(userAgent).get()

                    var mainHeader1 =document.getElementsByClass("textCont")[1].text()

                    if(mainHeader1.contains("(in millions)")){
                        mainHeader1 = mainHeader1.substring(27, 40) + " (PHP in millions)"
                    }else{
                        mainHeader1 = mainHeader1.substring(27, 40)
                    }

                    var mainHeader2 =document.getElementsByClass("textCont")[2].text()

                    Log.d("table", mainHeader1)

                    //ANNUALLY

                    var tableAsset = document.select("table")[0]
                    var row: Elements = tableAsset.select("tr")
                    fundamentals.add(FundamentalRow(0,"$mainHeader1","",""))
                    fundamentals.add(FundamentalRow(0,"Balance Sheet","",""))
                    var i = 0
                    for(f in 1 until row.size){
                        i++ //1-8 : Balance Sheet
                        fundamentals.add(FundamentalRow(i,row[f].getElementsByTag("th").text(), row[f].getElementsByTag("td")[0].text(), row[f].getElementsByTag("td")[1].text()))
                    }

                    fundamentals.add(FundamentalRow(0,"Income Statement","",""))
                    var tableIncomeStatement = document.select("table")[1]
                    var row2 = tableIncomeStatement.select("tr")
                    for(f in 1 until row2.size){
                        i++ //9-17 : Income Statement
                        fundamentals.add(FundamentalRow(i,row2[f].getElementsByTag("th").text(), row2[f].getElementsByTag("td")[0].text(), row2[f].getElementsByTag("td")[1].text()))
                    }

                    //QUARTERLY
                    fundamentals.add(FundamentalRow(0,"${mainHeader2.substring(23,36)}","",""))
                    fundamentals.add(FundamentalRow(0,"Balance Sheet (Quarterly)","",""))
                    var tableQuarterBalanceSheet = document.select("table")[2]
                    var row3 = tableQuarterBalanceSheet.select("tr")
                    for(f in 1 until row3.size){
                        i++ //1-26 : Income Statement
                        fundamentals.add(FundamentalRow(i,row3[f].getElementsByTag("th").text(), row3[f].getElementsByTag("td")[0].text(), row3[f].getElementsByTag("td")[1].text()))
                    }

                    //4 rows
                    fundamentals.add(FundamentalRow(0,"Income Statement (Quarterly)","",""))
                    var tableQIncomeStatement = document.select("table")[3]
                    var row4 = tableQIncomeStatement.select("tr")
                    for(f in 1 until row4.size){
                        i++ //26-29 : Income Statement
                        fundamentals.add(FundamentalRow(i,row4[f].getElementsByTag("th").text(), row4[f].getElementsByTag("td")[0].text(), row4[f].getElementsByTag("td")[1].text()))
                    }

                }catch (ex: Exception){
                    ex.printStackTrace()
                }


            }

            getData.await()
//        }

        return fundamentals

    }


}

class FundamentalRow(pos:Int,h:String, curr:String, prev:String){
    var ps = pos
    var h = h
    var c = curr
    var p = prev
}