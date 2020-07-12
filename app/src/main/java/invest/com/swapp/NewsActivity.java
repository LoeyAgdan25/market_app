package invest.com.swapp;

import android.os.AsyncTask;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import invest.com.swapp.adapter.RssFeedListAdapter;
import invest.com.swapp.model.RssFeedModel;

//TODO:- Title should not be included...

public class NewsActivity extends AppCompatActivity {

    private static final String TAG = "NewsActivity";

    private RecyclerView mRecyclerView;
    private EditText mEditText;
    private Button mFetchFeedButton;
    private SwipeRefreshLayout mSwipeLayout;
    private TextView mFeedTitleTextView;
    private TextView mFeedLinkTextView;
    private TextView mFeedDescriptionTextView;

    private List<RssFeedModel> mFeedModelList;
    private String mFeedTitle;
    private String mFeedLink;
    private String mFeedDescription;
    private String mFeedImage;
    public ArrayList<String> imageURL;
    public String[] ls = new String[]{};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Stock News");

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mEditText = (EditText) findViewById(R.id.rssFeedEditText);
        mFetchFeedButton = (Button) findViewById(R.id.fetchFeedButton);
        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mFeedTitleTextView = (TextView) findViewById(R.id.feedTitle);
        mFeedDescriptionTextView = (TextView) findViewById(R.id.feedDescription);
        mFeedLinkTextView = (TextView) findViewById(R.id.feedLink);
        imageURL = new ArrayList<>();

        if(getIntent().getStringExtra("cpnews") != null){
            //if(getIntent().getStringExtra("cpnews").equals("cpnews")){
                ls = new String[]{getIntent().getStringExtra("cpnews")};

                //Toast.makeText(this,"",Toast.LENGTH_LONG).show();
//            }
        }else{
            ls = new String[]{"https://data.gmanews.tv/gno/rss/money/economy/feed.xml",
                    "https://www.philstar.com/rss/business-as-usual",
                    "https://data.gmanews.tv/gno/rss/money/personalfinance/feed.xml",
                    "https://business.mb.com.ph/category/business-news/feed/"};
        }

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        new FetchFeedTask(ls).execute((Void) null);

        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new FetchFeedTask(ls).execute((Void) null);
            }
        });
    }

    public List<RssFeedModel> parseFeed(InputStream inputStream) throws XmlPullParserException, IOException {

        String title = null;
        String link = null;
        String description = null;

        boolean isItem = false;
        List<RssFeedModel> items = new ArrayList<>();

        int counter = 0;

        try {
            XmlPullParser xmlPullParser = Xml.newPullParser();
            xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            xmlPullParser.setInput(inputStream, null);

            while (xmlPullParser.next() != XmlPullParser.END_DOCUMENT) {

                int eventType = xmlPullParser.getEventType();

                String name = xmlPullParser.getName();
                if(name == null)
                    continue;

                if (eventType == XmlPullParser.START_TAG) {
                    if(name.equalsIgnoreCase("item")) {
                        isItem = true;
                        description = null;
                        title = null;
                        link = null;
                        ++counter;
                        continue;
                    }
                }

                if(eventType == XmlPullParser.END_TAG) {
                    if(name.equalsIgnoreCase("item")) {
                        isItem = false;
                    }
                    continue;
                }

                //Log.d("NewsActivity", "Parsing name ==> " + name);

                String result = "";
                if (xmlPullParser.next() == XmlPullParser.TEXT) {
                    result = xmlPullParser.getText();
                    xmlPullParser.nextTag();
                }

                if (name.equalsIgnoreCase("title")) {
                    title = result;

                } else if (name.equalsIgnoreCase("link")) {
                    link = result;

                } else if (name.equalsIgnoreCase("description")) {
                    description = result;

                }

                Log.d("rss => ","title=>" + title + ", link=>" + link + ", desc=>" + description);

                if (title != null && link != null && description != null) {
                    if(isItem) {

                        //new Content().execute(title.replace(" ","%20"),counter + "");
                        RssFeedModel item = new RssFeedModel(title, link, description, "");

                        items.add(item);
                    } else {
                        mFeedTitle = title;
                        mFeedLink = link;
                        mFeedDescription = description;
                    }

                    title = null;
                    link = null;
                    description = null;
                    isItem = false;
                }
            }

            return items;
        } finally {
            inputStream.close();
        }
    }

    private class FetchFeedTask extends AsyncTask<Void, Void, Boolean> {

        //TODO:- Load Image...

        String[] linkListings = new String[]{};

        FetchFeedTask(String[] newLink){
            linkListings = newLink;
        }

        private String urlLink;

        @Override
        protected void onPreExecute() {
            mSwipeLayout.setRefreshing(true);
            mFeedTitle = null;
            mFeedLink = null;
            mFeedDescription = null;
            mFeedTitleTextView.setText("Feed Title: " + mFeedTitle);
            mFeedDescriptionTextView.setText("Feed Description: " + mFeedDescription);
            mFeedLinkTextView.setText("Feed Link: " + mFeedLink);



//            linkListings = new String[]{"https://data.gmanews.tv/gno/rss/money/economy/feed.xml",
//                    "https://www.philstar.com/rss/business-as-usual",
//                    "https://data.gmanews.tv/gno/rss/money/personalfinance/feed.xml",
//                    "https://business.mb.com.ph/category/business-news/feed/"};

            //String[] linkListings = {"https://news.google.com/news?q=jollibee&output=rss"};

            Random random = new Random();


            if(linkListings.length == 1){
                urlLink = linkListings[0];
            }else{
                urlLink = linkListings[random.nextInt(linkListings.length)];
            }

            //urlLink = linkListings[random.nextInt(3)];
            //urlLink = linkListings[0];
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            if (TextUtils.isEmpty(urlLink))
                return false;

            try {
                if(!urlLink.startsWith("http://") && !urlLink.startsWith("https://"))
                    urlLink = "http://" + urlLink;

                URL url = new URL(urlLink);
                InputStream inputStream = url.openConnection().getInputStream();
                mFeedModelList = parseFeed(inputStream);
                return true;
            } catch (IOException e) {
                Log.e(TAG, "Error", e);
            } catch (XmlPullParserException e) {
                Log.e(TAG, "Error", e);
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            mSwipeLayout.setRefreshing(false);

            if (success) {
                mFeedTitleTextView.setText("Feed Title: " + mFeedTitle);
                mFeedDescriptionTextView.setText("Feed Description: " + mFeedDescription);
                mFeedLinkTextView.setText("Feed Link: " + mFeedLink);

                // Fill RecyclerView
                mRecyclerView.setAdapter(new RssFeedListAdapter(mFeedModelList,getBaseContext()));
            } else {
                Toast.makeText(NewsActivity.this,
                        "Enter a valid Rss feed url",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    //TODO:- Add image Async task here for jsoup image...
}
