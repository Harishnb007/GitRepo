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
import java.util.Iterator;
import java.util.Map;

public class ForgotPassword_Screen extends AppCompatActivity
{

    private WebView wv4;
    ProgressBar progressBar;
    final String h_mURLSchema1 = "toolbar://";
    final String H_KEY_CALLBACK1 = "callback";
    final String H_KEY_LOGOUT1 = "logout";
    final String H_REGEX_URL_SPLITER1 = "&|\\?";
    final String H_KEY_BACK2LOGIN = "backtologin";
    final String H_KEY_BACK2LEGAL = "backtolegal";
    public String url = Consts.pwdsecurityUrl;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password__screen);

        wv4 = (WebView) findViewById(R.id.fp_webView);
        progressBar = (ProgressBar) findViewById(R.id.fp_progressBar);
        wv4.setWebContentsDebuggingEnabled(true);

        wv4.setFocusable(false);
        wv4.setFocusableInTouchMode(true);
        wv4.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        wv4.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        wv4.setVerticalScrollBarEnabled(true);
        wv4.setHorizontalScrollBarEnabled(true);
        wv4.setScrollbarFadingEnabled(true);
        WebSettings webSettings1 = wv4.getSettings();
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
            wv4.setLayerType(View.LAYER_TYPE_HARDWARE, null);

        }
        else
            {
            wv4.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            }


        wv4.setWebViewClient(new WebViewClient()
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
                Log.d("WVIEW", "shouldOverrideUrlLoading = " + url);

                return handleURL(url);
            }

            public boolean handleURL(String url)
            {
                boolean returnStatus = true;
                if (url.startsWith(h_mURLSchema1))
                {
                    String urlStr = url.replace(h_mURLSchema1, "");
                    String[] queryStrSet = urlStr.split(H_REGEX_URL_SPLITER1);
                    Map<String, String> map = new HashMap<String, String>();

                    for (int x = 0; x < queryStrSet.length; x = x + 2)
                    {
                        map.put(queryStrSet[x], queryStrSet[x + 1]);
                    }


                    Iterator iterator = map.keySet().iterator();

                    while (iterator.hasNext())
                    {
                        String key = iterator.next().toString();
                        String value = map.get(key).toString();
                        // System.out.println(key + " " + value);
                        Log.e("map", "mapValue==>" + key + " " + value);
                    }


                    String val = map.get(H_KEY_CALLBACK1);
                    if (val != null && !val.trim().isEmpty())
                    {
                        Intent i = null;
                        switch (val)
                        {

                            case H_KEY_LOGOUT1:

                                i = new Intent(ForgotPassword_Screen.this, LoginActivity.class);
                                returnStatus = true;
                                Log.d("WView", "case : " + val);
                                break;

                            case H_KEY_BACK2LOGIN:

                                i = new Intent(ForgotPassword_Screen.this, LoginActivity.class);
                                returnStatus = true;
                                Log.d("WView", "case : " + val);
                                break;

                            case H_KEY_BACK2LEGAL:

                                i = new Intent(ForgotPassword_Screen.this, LoginActivity.class);
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
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error)
            {
                super.onReceivedSslError(view, handler, error);
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }

        });

        wv4.loadUrl(url);

    }


    public void onBackPressed()
    {
        Intent i = new Intent(ForgotPassword_Screen.this, LoginActivity.class);
        ForgotPassword_Screen.this.finish();
        startActivity(i);
    }
}
