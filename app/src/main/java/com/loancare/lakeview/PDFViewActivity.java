package com.loancare.lakeview;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.google.gson.Gson;
import com.loancare.lakeview.GetSet.LoginDetails;
import com.loancare.lakeview.Utils.Consts;
import com.loancare.lakeview.Utils.CustomProgressBar;
import com.loancare.lakeview.Utils.FileDownloader;

import java.io.File;
import java.io.IOException;

/**
 * Created by 886016 on 3/15/2018.
 */

public class PDFViewActivity extends AppCompatActivity implements OnPageChangeListener, OnLoadCompleteListener, OnPageErrorListener {

    PDFView pdfView;
    String pdfFileName;
    private static final String TAG = PDFActivity.class.getSimpleName();
    int downloadedSize = 0, totalsize;
    // String download_file_url = "http://ilabs.uw.edu/sites/default/files/sample_0.pdf";
    float per = 0;
    String dest_file_path = "privacy.pdf";
    LinearLayout back_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);
        pdfView = (PDFView) findViewById(R.id.pdfView);
        pdfView.setVisibility(View.VISIBLE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        back_layout=(LinearLayout) findViewById(R.id.back_layout);

        TextView text =(TextView) findViewById(R.id.text_view_id);
        text.setText("PRIVACY NOTICE");

        File directory = getBaseContext().getCacheDir();
        File pdfFile = new File(directory, dest_file_path);
        String pdf = Consts.privacy_url;

        if(pdfFile.exists()){
            //DO STUFF
            LoadPDF(pdfFile.toString());
        }
        else
        {
            new DownloadFile().execute(pdf,dest_file_path);
        }

        back_layout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });

    }

    private void goBack(){
            Intent i =new Intent(PDFViewActivity.this,WebviewActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            Gson gson = new Gson();
            LoginDetails responseDetails = gson.fromJson(Consts.JSONResponse, LoginDetails.class);
            if (responseDetails != null && responseDetails.status != null) {

                if (responseDetails.status.CustomErrorCode != null) {
                    if (responseDetails.status.CustomErrorCode.equalsIgnoreCase("0")) {
                        if (Consts.is_term_privacy) {
                            i.putExtra("URL", "termslogin");
                        } else {
                            i.putExtra("URL", "legallogin");
                        }

                        startActivity(i);
                        finish();
                        return;
                    }
                }
            }
            if (Consts.is_term_privacy) {
                i.putExtra("URL", "terms");
            } else {
                i.putExtra("URL", "legal");
            }
            startActivity(i);
            finish();
        }


    @Override
    public void onPageChanged(int page, int pageCount) {
        setTitle(String.format("%s %s / %s", pdfFileName, page + 1, pageCount));
    }

    @Override
    public void loadComplete(int nbPages) {

    }


   @Override
    public void onBackPressed() {
        super.onBackPressed();
       goBack();
    }

    @Override
    public void onPageError(int page, Throwable t) {
        Log.e(TAG, "Cannot load page " + page);
    }


    private class DownloadFile extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            CustomProgressBar.showDialog(PDFViewActivity.this);

        }

        @Override
        protected String doInBackground(String... strings) {

            String fileUrl = strings[0];

           String fileName = strings[1];

            //String completePath="";
            File directory = getBaseContext().getCacheDir();
            //File folder = new File(directory, fileName);
           // completePath = file.getAbsolutePath();

            File pdfFile = new File(directory, fileName);


            try{
                pdfFile.createNewFile();
            }catch (IOException e){
              //  e.printStackTrace();
            }
            FileDownloader.downloadFile(fileUrl, pdfFile);
            return pdfFile.toString();

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            CustomProgressBar.hideDialog();

           // Toast.makeText(getApplicationContext(), "Download PDf successfully", Toast.LENGTH_SHORT).show();
            Log.d("Download complete", "----------");

            String pdfurlpath = result;
            // String fileName = "estatement.pdf";
            // String completePath = Environment.getDataDirectory() + "/Documents/" + fileName;


            File file = new File(pdfurlpath);
            Uri pdfURI = Uri.fromFile(file);
            //Uri pdfURI =  Uri.parse(pdfurl);
            Log.d("URI",pdfURI.toString());
            pdfView.fromUri(pdfURI)
                    .enableAnnotationRendering(true)
                    .enableSwipe(true)
                    .swipeHorizontal(false)
                    .enableDoubletap(true)
                    .defaultPage(0)
                    .password(null)
                    .scrollHandle(null)
                    .onLoad(new OnLoadCompleteListener() {
                        @Override
                        public void loadComplete(int nbPages) {
                            pdfView.enableAnnotationRendering(true);
                            pdfView.setPositionOffset(0);
                            pdfView.loadPages();
                        }
                    })
                    .load();



        }
    }


    private  void LoadPDF(String result)
    {
        String pdfurlpath = result;


        File file = new File(pdfurlpath);
        Uri pdfURI = Uri.fromFile(file);
        //Uri pdfURI =  Uri.parse(pdfurl);
        Log.d("URI",pdfURI.toString());
        pdfView.fromUri(pdfURI)
                .enableAnnotationRendering(true)
                .enableSwipe(true)
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .defaultPage(0)
                .password(null)
                .scrollHandle(null)
                .onLoad(new OnLoadCompleteListener() {
                    @Override
                    public void loadComplete(int nbPages) {
                        pdfView.enableAnnotationRendering(true);
                        pdfView.setPositionOffset(0);
                        pdfView.loadPages();
                    }
                })
                .load();

    }



}
