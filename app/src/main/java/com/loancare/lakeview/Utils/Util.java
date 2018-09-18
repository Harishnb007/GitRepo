package com.loancare.lakeview.Utils;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;


public class Util
{
    public static void setFont(int fontNo, Context ctx, TextView view, String text)
    {
        String fontpath = "";

        if(fontNo==1)
        {
            fontpath = "fonts/opensans-regular.ttf";
        }

        else if (fontNo==2)
        {
            fontpath = "fonts/opensans-extrabold.ttf";
        }

        else if (fontNo==3)
        {
            fontpath = "fonts/opensans-semibold.ttf";
        }

        Typeface tf2 = Typeface.createFromAsset(ctx.getAssets(),fontpath);
        view.setTypeface(tf2);
        view.setText(text);
    }
}
