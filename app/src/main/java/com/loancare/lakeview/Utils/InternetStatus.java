package com.loancare.lakeview.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class InternetStatus
{

    private static Context _context;

    public static boolean InternetStatus(Context context)
    {
        _context=context;

        ConnectivityManager cm = (ConnectivityManager)_context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if(wifiNetwork!=null&&wifiNetwork.isConnected())
        {
            return true;
        }

        NetworkInfo mobilenetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if(mobilenetwork!= null && mobilenetwork.isConnected())
        {
            return true;
        }

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if(activeNetwork!=null&&activeNetwork.isConnected())
        {
            return true;
        }

        return false;
    }


}

