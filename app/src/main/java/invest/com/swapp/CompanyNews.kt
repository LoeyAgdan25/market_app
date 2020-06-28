package invest.com.swapp

import android.os.Bundle
import android.util.Log
import android.util.Xml
import androidx.appcompat.app.AppCompatActivity
import invest.com.swapp.model.RssFeedModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.util.*

class CompanyNews : AppCompatActivity() {

    private var mFeedTitle: String? = null
    private var mFeedLink: String? = null
    private var mFeedDescription: String? = null
    private var mFeedModelList: List<RssFeedModel>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_company_news)
        var urlLink = "http://news.google.com/news?q=jollibee&output=rss"
        //https://news.google.com/rss/search?q=jollibee&hl=en-US&gl=US&ceid=US:en

        GlobalScope.launch {
            try {
                if (!urlLink.startsWith("http://") && !urlLink.startsWith("https://")) urlLink = "http://$urlLink"
                val url = URL(urlLink)
                val inputStream = url.openConnection().getInputStream()
                mFeedModelList = parseFeed(inputStream)

                Log.d("_newsCount", "${mFeedModelList!!.size}")
                //return true
            } catch (e: IOException) {
                Log.e("NewsException", "Error", e)
            } catch (e: XmlPullParserException) {
                Log.e("NewsException", "Error", e)
            }
        }

    }

    @Throws(XmlPullParserException::class, IOException::class)
    fun parseFeed(inputStream: InputStream): List<RssFeedModel>? {
        var title: String? = null
        var link: String? = null
        var description: String? = null
        var isItem = false
        val items: MutableList<RssFeedModel> = ArrayList()
        var counter = 0
        return try {
            val xmlPullParser = Xml.newPullParser()
            xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            xmlPullParser.setInput(inputStream, null)
            while (xmlPullParser.next() != XmlPullParser.END_DOCUMENT) {
                val eventType = xmlPullParser.eventType
                val name = xmlPullParser.name ?: continue
                if (eventType == XmlPullParser.START_TAG) {
                    if (name.equals("item", ignoreCase = true)) {
                        isItem = true
                        description = null
                        title = null
                        link = null
                        ++counter
                        continue
                    }
                }
                if (eventType == XmlPullParser.END_TAG) {
                    if (name.equals("item", ignoreCase = true)) {
                        isItem = false
                    }
                    continue
                }
                //Log.d("NewsActivity", "Parsing name ==> " + name);
                var result: String? = ""
                if (xmlPullParser.next() == XmlPullParser.TEXT) {
                    result = xmlPullParser.text
                    xmlPullParser.nextTag()
                }
                if (name.equals("title", ignoreCase = true)) {
                    title = result
                } else if (name.equals("link", ignoreCase = true)) {
                    link = result
                } else if (name.equals("description", ignoreCase = true)) {
                    description = result
                }
                Log.d("rss => ", "title=>$title, link=>$link, desc=>$description")
                if (title != null && link != null && description != null) {
                    if (isItem) { //new Content().execute(title.replace(" ","%20"),counter + "");
                        val item = RssFeedModel(title, link, description, "")
                        items.add(item)
                    } else {
                        mFeedTitle = title
                        mFeedLink = link
                        mFeedDescription = description
                    }
                    title = null
                    link = null
                    description = null
                    isItem = false
                }
            }
            items
        } finally {
            inputStream.close()
        }
    }
}
