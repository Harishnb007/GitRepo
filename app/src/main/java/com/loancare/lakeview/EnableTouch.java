package com.loancare.lakeview;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.loancare.lakeview.Utils.Consts;
import com.loancare.lakeview.Utils.Dbhelper;


public class EnableTouch extends AppCompatActivity
{

    ImageView tumph;
    TextView heading1, heading2, heading3, notnow;
    LinearLayout enable_touch;
    Dbhelper mHelper;
    SQLiteDatabase database;
    final Context context = this;
    Context ctx = EnableTouch.this;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enable_touch);
        mHelper = new Dbhelper(getApplicationContext());
        database = mHelper.getWritableDatabase();
        tumph = (ImageView) findViewById(R.id.scan_img);
        heading1 = (TextView) findViewById(R.id.txt_1);
        heading2 = (TextView) findViewById(R.id.txt_2);
        heading3 = (TextView) findViewById(R.id.txt_3);
        notnow = (TextView) findViewById(R.id.notnow_txt);
        enable_touch = (LinearLayout) findViewById(R.id.enable_touch);


        enable_touch.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(EnableTouch.this, WebActivity.class);
                EnableTouch.this.finish();
                Toast.makeText(EnableTouch.this, "Touch ID is Enabled !!", Toast.LENGTH_SHORT).show();
                startActivity(intent);

            }
        });


        notnow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {


                    Intent intent = new Intent(EnableTouch.this, WebActivity.class);
                    Consts.notnow = "true";
                    EnableTouch.this.finish();
                    startActivity(intent);

            }
        });


    }

    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i);
            finish();
        }

        return false;

    }


}
