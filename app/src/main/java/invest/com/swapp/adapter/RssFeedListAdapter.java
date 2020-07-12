package invest.com.swapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import invest.com.swapp.R;
import invest.com.swapp.model.RssFeedModel;
import invest.com.swapp.ViewNews;

//Add result to existing result...

public class RssFeedListAdapter
        extends RecyclerView.Adapter<RssFeedListAdapter.FeedModelViewHolder> {

    private List<RssFeedModel> mRssFeedModels;
    Context context;
    public ArrayList<String> imageURL = new  ArrayList<>();

    public static class FeedModelViewHolder extends RecyclerView.ViewHolder {
        private View rssFeedView;


        public FeedModelViewHolder(View v) {
            super(v);
            rssFeedView = v;
        }
    }

    public RssFeedListAdapter(List<RssFeedModel> rssFeedModels, Context context) {
        mRssFeedModels = rssFeedModels;
        this.context = context;
    }

    @Override
    public FeedModelViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rss_feed, parent, false);
        FeedModelViewHolder holder = new FeedModelViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(FeedModelViewHolder holder, int position) {
        final RssFeedModel rssFeedModel = mRssFeedModels.get(position);
        ((TextView)holder.rssFeedView.findViewById(R.id.titleText)).setText(rssFeedModel.title);



        TextView tvDescription = ((TextView)holder.rssFeedView.findViewById(R.id.descriptionText));
       // tvDescription.setText(rssFeedModel.description);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvDescription.setText(Html.fromHtml(rssFeedModel.description, Html.FROM_HTML_MODE_COMPACT));
        } else {
            tvDescription.setText(Html.fromHtml(rssFeedModel.description));
        }
        ImageView img = holder.rssFeedView.findViewById(R.id.img_feature_news);


        ((TextView)holder.rssFeedView.findViewById(R.id.linkText)).setVisibility(View.GONE);
        try {
            //Picasso.with(context).load(new Content().execute(rssFeedModel.title, position + "").get()).into(img);
        }catch (Exception ex){

        }
        holder.rssFeedView.findViewById(R.id.linear_news_row).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent(context, ViewNews.class);
               intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
               intent.putExtra("link", rssFeedModel.link);
               context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mRssFeedModels.size();
    }

    private class Content extends AsyncTask<String, Void, String> {

        String view;
        String imgSrc;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... param) {

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


            return imgSrc;
        }

        @Override
        protected void onPostExecute(String str) {
            super.onPostExecute(str);
            imageURL.add(imgSrc);
        }
    }

    //TODO:- Add jsoup feature image gathering
}

