package com.loancare.lakeview;
import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loancare.lakeview.Utils.Consts;
import com.loancare.lakeview.Utils.CustomProgressBar;
import com.loancare.lakeview.Utils.InternetStatus;
import com.loancare.lakeview.Utils.KeyboardUtils;
import com.loancare.lakeview.retrofitt.ApiClient;
import com.loancare.lakeview.retrofitt.GetPDFStatementInterface;
import com.loancare.lakeview.retrofitt.GetStatement;
import com.loancare.lakeview.retrofitt.PdfEStatementDetails;


import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class WebActivity extends AppCompatActivity
{

    WebView webView;
    Double PIC_WIDTH;
    public String url = "";
    private static final String TAG = WebActivity.class.getSimpleName();
    SharedPreferences sharedpreferences;
    ProgressBar progressBar;
    boolean isSessionSet = false;
    final String mUrl = Consts.loginUrl;
    final String mURLSchema = "toolbar://";
    final String pdfURLSchema = "toolbar://pdfName?/Statements/EStatementHandler.Pdf?";
    final String KEY_CALLBACK = "callback";
    final String KEY_PDF_FILE = "pdfName";
    final String KEY_LOGOUT = "logout";
    final String KEY_ENABLE_TOUCH = "3";
    final String KEY_DISABLE_TOUCH = "4";
    final String KEY_PRIVACY = "privacy";
    final String KEY_TERMSPRIVACY = "termsprivacy";
    final String KEY_NMLS = "nmls";
    final String KEY_CAMERA = "1";
    final String KEY_DOWNLOAD_PDF = "2";
    final String REGEX_URL_SPLITER = "&|\\?";
    final String KEY_BACK2LOGIN = "backtologin";
    final String KEY_BACK2LEGAL = "backtolegal";
    final String KEY_CALL = "call";
    final String KEY_PHONE_CALL = "call&phoneno";
    final String KEY_TIME_OUT = "timeout";
    final String Touch_true = "true";
    final String Touch_false = "false";
    public String sessionCookie =  null;
    final String URI_PDF_FILE = "";
    private static final int PERMISSIONS_REQUEST_PHONE_CALL = 100;
    private static String[] PERMISSIONS_PHONECALL = {Manifest.permission.CALL_PHONE};
    public static final String mypreference = "mypref";
    public static final String TouchEnablement = "touch_enable_Key";
    public final String register = "register";
    public final String legal = "legal";
    public final String help = "help";
    private String urlToLoad;

    File file;


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        //  Consts.Touch_status="true";
        urlToLoad = getIntent().getStringExtra("URL");
        webView = (WebView) findViewById(R.id.webView1);
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        //finding Views
        webView.setWebContentsDebuggingEnabled(true);
        webView.setFocusable(false);
        webView.setFocusableInTouchMode(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setVerticalScrollBarEnabled(true);
        webView.setHorizontalScrollBarEnabled(true);
        webView.setScrollbarFadingEnabled(true);
        WebSettings webSettings = webView.getSettings();
        webSettings.setAppCacheEnabled(false);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setDatabaseEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.getJavaScriptCanOpenWindowsAutomatically();
        webSettings.setAllowContentAccess(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setBlockNetworkLoads(false);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        if (sharedpreferences.contains(TouchEnablement))
        {
            Consts.TouchEnablement = (sharedpreferences.getString(TouchEnablement, ""));
        }

        if (Build.VERSION.SDK_INT >= 19)
        {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);

        }
        else
            {
                webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            }


        if (InternetStatus.InternetStatus(WebActivity.this))
        {

            webView.setWebViewClient(new WebViewClient()

            {

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon)
                {
                    progressBar.setVisibility(View.VISIBLE);
                    Log.e("<<<<< >>>>>>>>","onPageStarted");
                    super.onPageStarted(view, url, favicon);
                }

                @Override
                public void onLoadResource(WebView view, String url)
                {
                    Log.i("Session","call javascript");
                    Log.e("<<<<< >>>>>>>>","onLoadResource");
                    callJavascript();
                    call_Touch_ID();
                    set_Touch_ID();
                    //callJavascript1();
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

                    if (url.startsWith(pdfURLSchema)) {
                        String query = url.replace(pdfURLSchema, "");


                        Map < String, String > data = new HashMap < String, String > ();
                        for (String q: query.split("&")) {
                            if (q.startsWith("callback")) {
                                String[] qa = q.split("\\?");
                                data.put(qa[0], qa[1]);
                            } else {
                                String[] qa = q.split("=");
                                data.put(qa[0], qa[1]);
                            }

                        }

                        CustomProgressBar.showDialog(WebActivity.this);

                        String loanNumber = data.get("loanNo");
                        String statementDate = data.get("statementDate");
                        String statementKey = data.get("statementKey");


                        Gson gsonObj = new Gson();
                        PdfEStatementDetails loginResponse = gsonObj.fromJson(Consts.JSONResponse, PdfEStatementDetails.class);
                        String authToken = loginResponse.data.getAuthorizationToken();


                        GetPDFStatementInterface service = ApiClient.getClient().create(GetPDFStatementInterface.class);

                        Call<ResponseBody> call = service.getPDF(authToken, new GetStatement(loanNumber, statementKey, statementDate));

                        call.enqueue(new Callback< ResponseBody >() {
                            @Override
                            public void onResponse(Call < ResponseBody > call, final Response< ResponseBody > response) {

                                try {

                                    Log.d("onResponse", "Response came from server" + response.code());

                                    //  boolean FileDownloaded = DownloadImage(response.body());
                                    String completePath="";
                                    String fileName = "estatement.pdf";
                                    File directory = getBaseContext().getCacheDir();
                                    File file = new File(directory, fileName);

                                /*
                                    File folder = new File(Environment.ge() + "/Documents/"+ fileName);
                                    */
                                    completePath = file.getAbsolutePath();

                                    FileOutputStream fileOutputStream = new FileOutputStream(completePath);
                                    IOUtils.write(response.body().bytes(), fileOutputStream);
                                    //webView.setVisibility(View.INVISIBLE);
                                   // toolbar.setVisibility(View.VISIBLE);
                                   // pri_webview.setVisibility(View.VISIBLE);
                                    // pri_webview.set

                                    CustomProgressBar.hideDialog();


                                    Intent intent = new Intent(WebActivity.this,PDFActivity.class);
                                    intent.putExtra("pdf",completePath);
                                    startActivity(intent);
                                    //finish();


                                } catch (Exception e) {
                                    Log.d("onResponse", "There is an error");
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(Call < ResponseBody > call, Throwable t) {

                                Log.d("onFailure", t.toString());
                                CustomProgressBar.hideDialog();


                            }


                        });


                        Log.d("WView", data.get("loanNo"));


                    }
                    else {


                    Log.e("WVIEW", "url==> = " + url);
                    Intent i = null;

                    String urlStr1 = url.replace(mURLSchema, "");
                    String[] queryStrSet1 = urlStr1.split("\\?");
                    String val1 = queryStrSet1[0];
                    String val2 = queryStrSet1[1];
                    Log.e("WVIEW", "Safari_Value1==> = " + val1);
                    Log.e("WVIEW", "Safari_Value2==> = " + val2);

                    if(val1.equals("safari"))
                    {
                        Log.e("WVIEW", "Safari_Value2==> = " + val2);
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(val2));
                        startActivity(intent);

                    }
                    else {
                        if (url.startsWith(mURLSchema)) {
                            String urlStr = url.replace(mURLSchema, "");
                            Log.e("url", "url==>" + url);
                            String[] queryStrSet = urlStr.split(REGEX_URL_SPLITER);
                            Log.e("queryStrSet", "queryStrSet==>" + queryStrSet);
                            Map<String, String> map = new HashMap<String, String>();

                            for (int x = 0; x < queryStrSet.length; x = x + 2) {
                                map.put(queryStrSet[x], queryStrSet[x + 1]);

                            }

                            Iterator iterator = map.keySet().iterator();

                            while (iterator.hasNext()) {
                                String key = iterator.next().toString();
                                String value = map.get(key).toString();
                                Log.e("map", "mapValue==>" + key + " " + value);
                            }

                            String val = map.get(KEY_CALLBACK);
                            if (val != null && !val.trim().isEmpty()) {
                                //Intent i = null;
                                Log.e("map", "mapValue1==>" + val);
                                switch (val) {
                                    case KEY_CAMERA:

                                        Toast.makeText(WebActivity.this, "MICR UNDER CONSTr", Toast.LENGTH_SHORT).show();
                                        returnStatus = true;
                                        Log.d("WView", "case : " + val);
                                        break;

                                    case KEY_ENABLE_TOUCH:

                                        Consts.enable_Enable1 = "true";
                                        Log.e("Touch_yes", "Touch_yes==>");
                                        set_Touch_ID();
                                        Toast.makeText(WebActivity.this, "Enable Touch", Toast.LENGTH_SHORT).show();
                                        returnStatus = true;
                                        Log.d("WView", "case : " + val);
                                        break;

                                    case KEY_DISABLE_TOUCH:

                                        Consts.enable_Enable1 = "false";
                                        Log.e("Touch_no", "Touch_no==>");
                                        set_Touch_ID();
                                        Toast.makeText(WebActivity.this, "Disable Touch", Toast.LENGTH_SHORT).show();
                                        returnStatus = true;
                                        Log.d("WView", "case : " + val);
                                        break;


                                    case KEY_DOWNLOAD_PDF:

                                        Toast.makeText(WebActivity.this, "E-Statement is Working", Toast.LENGTH_SHORT).show();


                                        break;


                                    case KEY_LOGOUT:

                                        i = new Intent(WebActivity.this, LoginActivity.class);
                                        webView.removeAllViews();
                                        webView.destroy();
                                        Consts.logout = "true";
                                        Consts.JSONResponse = "";
                                        Consts.JSONResponse_Fpwd = "";
                                        startActivity(i);
                                        WebActivity.this.finish();
                                        sharedpreferences.edit().clear().commit();
                                        returnStatus = true;
                                        Log.d("WView", "case : " + val);
                                        break;

                                    case KEY_BACK2LOGIN:

                                        i = new Intent(WebActivity.this, LoginActivity.class);
                                        //  Consts.Touch_status="false";
                                        Consts.JSONResponse = "";
                                        Consts.JSONResponse_Fpwd = "";
                                        returnStatus = true;
                                        Log.e("KEY_BACK2LOGIN", "KEY_BACK2LOGIN===>" + KEY_BACK2LOGIN);
                                        Log.d("WView", "case : " + val);
                                        break;

                                    case KEY_BACK2LEGAL:

                                        i = new Intent(WebActivity.this, LoginActivity.class);
                                        returnStatus = true;
                                        Log.d("WView", "case : " + val);
                                        break;

                                    case KEY_TIME_OUT:

                                        i = new Intent(WebActivity.this, LoginActivity.class);
                                        Toast.makeText(WebActivity.this, "SESSION TIME OUT", Toast.LENGTH_SHORT).show();
                                        startActivity(i);
                                        returnStatus = true;
                                        Log.d("WView", "case : " + val);
                                        break;


                                    case KEY_CALL:

                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT &&
                                                checkSelfPermission(Manifest.permission.CALL_PHONE)
                                                        != PackageManager.PERMISSION_GRANTED) {
                                            requestPermissions(new String[]{Manifest.permission.CALL_PHONE},
                                                    PERMISSIONS_REQUEST_PHONE_CALL);
                                        } else {

                                            String number = map.get("phoneno");
                                            Log.e("number", "number===>" + number);
                                            Intent intent = new Intent(Intent.ACTION_DIAL);
                                            intent.setData(Uri.parse("tel:" + number));
                                            startActivity(intent);
                                            returnStatus = true;
                                            progressBar.setVisibility(View.GONE);
                                        }


                                        break;


                                    case KEY_PRIVACY:
                                    case KEY_TERMSPRIVACY:
                                        Log.d("KEY_PRIVACY", "KEY_PRIVACY : " + KEY_PRIVACY);
                                        Consts.is_term_privacy = val.equals(KEY_TERMSPRIVACY);
                                        Consts.privacy_url = map.get("pdfName");
                                        i = new Intent(WebActivity.this, PDFViewActivity.class);
                                        startActivity(i);
                                        returnStatus = true;
                                        break;

                                    case KEY_NMLS:
                                        Log.d("KEY_NMLS", "KEY_NMLS : " + KEY_NMLS);
                                    i = new Intent(Intent.ACTION_VIEW, Uri.parse
                                            ("http://www.nmlsconsumeraccess.org"));
                                        startActivity(i);
                                    returnStatus = true;
                                    break;


                                }


                            }

                        }
                    }
                    }
                    if(returnStatus == true)
                    {
                        //finish();
                    }
                    return returnStatus;
                }

                @Override
                public void onPageFinished(WebView view, String url)
                {
                    Log.d("WVIEW","onPageFinished = " + url);
                    super.onPageFinished(view, url);
                    Log.e("<<<<< >>>>>>>>","onPageFinished");
                    progressBar.setVisibility(View.GONE);
                    Log.i(TAG, "WebActivity.getView() — Page finished ");
                }

                @Override
                public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error)
                {
                    super.onReceivedSslError(view, handler, error);
                    Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                }


            });


            if (savedInstanceState == null) {
                webView.loadUrl(mUrl);
            }




    }
    else
        {


            AlertDialog alertDialog = new AlertDialog.Builder(WebActivity.this).create();
            alertDialog.setTitle("Message");
            alertDialog.setMessage("Please check Internet Connection !!");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();


        }



        KeyboardUtils.addKeyboardToggleListener(this, new KeyboardUtils.SoftKeyboardToggleListener()
        {
            @Override
            public void onToggleSoftKeyboard(boolean isVisible)
            {

                Log.d("keyboard", "keyboard visible: "+isVisible);
                if(isVisible)
                {
                       addSpace();
                }
                else
                    {
                      hideSpace();
                    }
            }
        });


    }

    private void hideSpace()
    {
        Log.d("back","back pressed");
        String script = "if(lc!==undefined){lc.namespace.backPressed()}";
        webView.evaluateJavascript(script, new ValueCallback<String>()
        {
            @Override
            public void onReceiveValue(String s)
            {
                isSessionSet = true;
            }
        });
    }

    private void addSpace()
    {
        Log.d("back","keyboard open");
        String script = "if(lc!==undefined){lc.namespace.addSpace()}";
        webView.evaluateJavascript(script, new ValueCallback<String>()
        {
            @Override
            public void onReceiveValue(String s)
            {
                isSessionSet = true;
            }
        });
    }

    private void callJavascript()
    {
        String script = "var sessionString = '"
       + Consts.JSONResponse +  "';sessionStorage.setItem('userDetailsMobile',sessionString);console.log('session set')";
         webView.evaluateJavascript(script, new ValueCallback<String>()
                {
                    @Override
                    public void onReceiveValue(String s)
                    {
                        isSessionSet = true;
                    }
                });
    }

    private void call_Touch_ID()
    {
        String script = "var sessionString = '"
                + Consts.enable_Enable + "';sessionStorage.setItem('TouchIdEnablement',sessionString);console.log('session set')";
        webView.evaluateJavascript(script, new ValueCallback<String>()
        {
            @Override
            public void onReceiveValue(String s)
            {
                isSessionSet = true;
            }
        });

    }

    private void set_Touch_ID()
    {
        String script1 = "var sessionString1 = '"
                + Consts.enable_Enable1 + "';sessionStorage.setItem('UserEnableTouchID',sessionString1);console.log('session set')";
        webView.evaluateJavascript(script1, new ValueCallback<String>()
        {
            @Override
            public void onReceiveValue(String s)
            {
                isSessionSet = true;
            }
        });

        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("checked", Consts.enable_Enable1);
        editor.commit();
        editor.apply();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack())
        {
            webView.goBack();
           // webView.ca;
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        webView.onPause();
        webView.pauseTimers();
    }
    @Override
    public void onResume()
    {
        super.onResume();
        webView.resumeTimers();
        webView.onResume();
    }

}





