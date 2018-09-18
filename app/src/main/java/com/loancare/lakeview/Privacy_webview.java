package com.loancare.lakeview;


import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import com.loancare.lakeview.Utils.Consts;


public class Privacy_webview extends AppCompatActivity
{


    public final String url = Consts.privacy_url;
    public WebView pri_webview;
    LinearLayout back_layout;
    ProgressBar progressBar;

    int downloadedSize = 0, totalsize;
    String download_file_url = "http://ilabs.uw.edu/sites/default/files/sample_0.pdf";
    float per = 0;
    String dest_file_path = "test.pdf";
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_webview);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        back_layout=(LinearLayout) findViewById(R.id.back_layout);
        pri_webview = (WebView) findViewById(R.id.privacy_webView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar_privacy);
        pri_webview.setWebContentsDebuggingEnabled(true);
        pri_webview.setFocusable(false);
        pri_webview.setFocusableInTouchMode(true);
        pri_webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        pri_webview.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        pri_webview.setVerticalScrollBarEnabled(true);
        pri_webview.setHorizontalScrollBarEnabled(true);
        pri_webview.setScrollbarFadingEnabled(true);
        pri_webview.getSettings().setJavaScriptEnabled(true);
        WebSettings  pri_webSettings1 = pri_webview.getSettings();

        pri_webview.setWebViewClient(new WebViewClient()
        {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon)
            {
                progressBar.setVisibility(View.VISIBLE);
                // callJavascript1();
                Log.e("<<<<< >>>>>>>>", "onPageStarted");
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url)
            {
                super.onPageFinished(view, url);
                Log.e("<<<<< >>>>>>>>", "onPageFinished");
                // callJavascript1();
                progressBar.setVisibility(View.GONE);
            }

        });

        String pdf = Consts.privacy_url;
        pri_webview.loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url=" + pdf);

        back_layout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i =new Intent(Privacy_webview.this,WebviewActivity.class);
                i.putExtra("URL", "legal");
                startActivity(i);

            }
        });



    }


    public boolean onKeyDown(int keyCode, KeyEvent event)
    {

                Log.e("<<<<< >>>>>>>>", "onKeyDown2");
                Intent i =new Intent(Privacy_webview.this,WebviewActivity.class);
                i.putExtra("URL", "legal");
                startActivity(i);

            return true;
        }



    }



