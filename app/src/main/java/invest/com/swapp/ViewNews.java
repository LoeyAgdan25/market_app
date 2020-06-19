package invest.com.swapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

public class ViewNews extends AppCompatActivity {
    WebView webView;
    ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_news);
        progress = new ProgressDialog(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        this.webView = (WebView) findViewById(R.id.webview);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        WebviewClient webViewClient = new WebviewClient(this);

//        webView.addJavascriptInterface(this, "MyApp");


        webView.setWebViewClient(webViewClient);
        webView.loadUrl(getIntent().getStringExtra("link"));

//        progress.setMessage("Loading news...");
//        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        progress.setIndeterminate(true);
//        progress.show();
    }

    private class WebviewClient extends WebViewClient {
        private Activity activity = null;

        public WebviewClient(Activity activity) {
            this.activity = activity;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String url) {

            return false;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
        }
        @Override
        public void onPageFinished(WebView view, String url) {
            webView.getSettings().setUseWideViewPort(true);
            webView.getSettings().setLoadWithOverviewMode(true);
            webView.getSettings().setBuiltInZoomControls(true);
            webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
            super.onPageFinished(view, url);
        }
    }


    @JavascriptInterface
    public void resize(final float height) {
        ViewNews.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                webView.setLayoutParams(new LinearLayout.LayoutParams(getResources().getDisplayMetrics().widthPixels, (int) (height * getResources().getDisplayMetrics().density)));
            }
        });
    }

}
