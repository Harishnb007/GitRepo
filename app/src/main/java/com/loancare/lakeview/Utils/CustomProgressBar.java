package com.loancare.lakeview.Utils;

import android.app.ProgressDialog;
import android.content.Context;

import dmax.dialog.SpotsDialog;


public class CustomProgressBar
{


    static ProgressDialog progressDialog = null;
    static SpotsDialog dialog = null;

      //Style Progress Bar
    public static void showDialog(Context context)
    {
        dialog = new SpotsDialog(context);
        dialog.setCancelable(false);
        dialog.show();
    }

    public static void hideDialog()
    {
        if(dialog.isShowing() && dialog != null)
        {
            dialog.dismiss();
        }
    }
}
