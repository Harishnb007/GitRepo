package com.loancare.lakeview;


import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;

import java.io.File;


public class PDFActivity extends AppCompatActivity implements OnPageChangeListener, OnLoadCompleteListener, OnPageErrorListener
{
    PDFView pdfView;
    String pdfFileName;
    private static final String TAG = PDFActivity.class.getSimpleName();

    LinearLayout back_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);
        pdfView = (PDFView) findViewById(R.id.pdfView);

        pdfView.setVisibility(View.VISIBLE);

        back_layout=(LinearLayout) findViewById(R.id.back_layout);

        String pdfurl = getIntent().getStringExtra("pdf");


        back_layout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
               /* Intent i =new Intent(PDFActivity.this,WebActivity.class);
                startActivity(i);*/
                finish();

            }
        });


        String fileName = "estatement.pdf";
        String completePath = Environment.getDataDirectory() + "/Documents/" + fileName;


        File file = new File(pdfurl);
        Uri pdfURI = Uri.fromFile(file);
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



    @Override
    public void onPageChanged(int page, int pageCount) {
        setTitle(String.format("%s %s / %s", pdfFileName, page + 1, pageCount));
    }

    @Override
    public void loadComplete(int nbPages) {

    }



    @Override
    public void onPageError(int page, Throwable t) {
        Log.e(TAG, "Cannot load page " + page);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

      /*  Intent i =new Intent(PDFActivity.this,WebActivity.class);
        startActivity(i);*/
        finish();


    }

}
