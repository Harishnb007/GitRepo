package com.loancare.lakeview.Utils;

import android.app.ProgressDialog;
import android.content.Context;

/*** Created by user on 10/21/2016. */

public class ProgressBar
{
    static ProgressDialog  pDialog = null;
    //static SpotsDialog dialog = null;
    public static void showDialog(Context context) {
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Please wait ...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    public static void hideDialog()
    {
        if(pDialog.isShowing())
        {
            pDialog.dismiss();
        }
    }
}
