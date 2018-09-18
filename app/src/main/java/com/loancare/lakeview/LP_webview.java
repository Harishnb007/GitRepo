package com.loancare.lakeview;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.loancare.lakeview.Utils.Consts;
import java.util.HashMap;
import java.util.Map;

public class LP_webview extends AppCompatActivity
{


    private WebView wv3;
    ProgressBar progressBar;
    final String mURLSchema1 = "toolbar://";
    final String KEY_CALLBACK1 = "callback";
    final String KEY_LOGOUT1 = "logout";
    final String REGEX_URL_SPLITER1 = "&|\\?";
    final String KEY_BACK2LOGIN_LP = "backtologin";
    final String KEY_BACK2LEGAL = "backtolegal";
    public String url = Consts.legalUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lp_webview);
        wv3 = (WebView) findViewById(R.id.lp_webView);
        progressBar = (ProgressBar) findViewById(R.id.lp_progressBar);

        wv3.setFocusable(false);
        wv3.setFocusableInTouchMode(true);
        wv3.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        wv3.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        wv3.setVerticalScrollBarEnabled(true);
        wv3.setHorizontalScrollBarEnabled(true);
        wv3.setScrollbarFadingEnabled(true);
        WebSettings webSettings1 = wv3.getSettings();
        webSettings1.setAppCacheEnabled(false);
        webSettings1.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings1.setDatabaseEnabled(true);
        webSettings1.setDomStorageEnabled(true);
        webSettings1.setJavaScriptEnabled(true);
        webSettings1.getJavaScriptCanOpenWindowsAutomatically();
        webSettings1.setAllowContentAccess(true);
        webSettings1.setLoadWithOverviewMode(true);
        webSettings1.setUseWideViewPort(true);
        webSettings1.setBlockNetworkLoads(false);
        webSettings1.setLoadsImagesAutomatically(true);
        webSettings1.setPluginState(WebSettings.PluginState.ON);
        webSettings1.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSettings1.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);


        if (Build.VERSION.SDK_INT >= 19)
        {
            wv3.setLayerType(View.LAYER_TYPE_HARDWARE, null);

        }
        else {
            wv3.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        }


        wv3.setWebViewClient(new WebViewClient()
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
            public void onLoadResource(WebView view, String url)
            {

                Log.i("Session", "call javascript");
                Log.e("<<<<< >>>>>>>>", "onLoadResource");

                super.onLoadResource(view, url);

                return;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                Log.d("WVIEW","shouldOverrideUrlLoading = " + url);

                return handleURL(url);
            }

            public boolean handleURL(String url)
            {
                boolean returnStatus = true;
                if (url.startsWith(mURLSchema1))
                {
                    String urlStr = url.replace(mURLSchema1, "");
                    String[] queryStrSet = urlStr.split(REGEX_URL_SPLITER1);
                    Map<String, String> map = new HashMap<String, String>();

                    for (int x=0; x<queryStrSet.length; x=x+2)
                    {
                        map.put(queryStrSet[x], queryStrSet[x +1]);
                    }

                    String val = map.get(KEY_CALLBACK1);
                    if (val != null && !val.trim().isEmpty())
                    {
                        Intent i = null;
                        switch (val)
                        {

                            case KEY_LOGOUT1:

                                i = new Intent(LP_webview.this, LoginActivity.class);
                                returnStatus = true;
                                Log.d("WView", "case : " + val);
                                break;

                            case KEY_BACK2LOGIN_LP:

                                i = new Intent(LP_webview.this, LoginActivity.class);
                                returnStatus = true;
                                Log.d("WView", "case : " + val);
                                break;

                            case KEY_BACK2LEGAL:

                                i = new Intent(LP_webview.this, LoginActivity.class);
                                returnStatus = true;
                                Log.d("WView", "case : " + val);
                                break;

                        }

                        if (i != null)
                            startActivity(i);
                    }

                }

                return returnStatus;
            }




            @Override
            public void onPageFinished(WebView view, String url)
            {
                super.onPageFinished(view, url);
                Log.e("<<<<< >>>>>>>>", "onPageFinished");
                // callJavascript1();
                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }

        });

        wv3.loadUrl(url);

    }


    public void onBackPressed()
    {
        Intent i = new Intent(LP_webview.this, LoginActivity.class);
        LP_webview.this.finish();
        startActivity(i);
    }
}