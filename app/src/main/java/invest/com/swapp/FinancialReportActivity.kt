package invest.com.swapp

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.anko.toast
import org.jsoup.Jsoup


class FinancialReportActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_financial_report)

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

        /*
             String query = param[0];
            view = param[1];

            String userAgent = "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.116 Safari/537.36";
            String url = "https://www.google.com/search?site=imghp&tbm=isch&source=hp&q="+ query +"&gws_rd=cr";

            List<String> resultUrls = new ArrayList<String>();

            try {
                Document doc = Jsoup.connect(url).userAgent(userAgent).referrer("https://www.google.com/").get();

                Elements elements = doc.select("div.rg_meta");

                JSONObject jsonObject;

                for (Element element : elements) {
                    if (element.childNodeSize() > 0) {
                        jsonObject = (JSONObject) new JSONObject(element.childNode(0).toString());
                        resultUrls.add((String) jsonObject.get("ou"));
                        Log.d("image=>" , (String) jsonObject.get("ou"));
                        imgSrc = (String) jsonObject.get("ou");
                        break;
                    }
                }

                System.out.println("number of results: " + resultUrls.size());

                for (String imageUrl : resultUrls) {
                    System.out.println(imageUrl);
                }

            }  catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        */
    }
}
