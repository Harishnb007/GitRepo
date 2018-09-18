package com.loancare.lakeview.GetSet;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 886016 on 3/15/2018.
 */

public class ForgetPasswordDetails {

    @SerializedName("status")
    public Status status;

    @SerializedName("data")
    public Data data;


    public class Status
    {
        @SerializedName("CustomErrorCode")
        public String CustomErrorCode;

        @SerializedName("Message")
        public String Message;

    }

    public class Data
    {


        @SerializedName("secQuesstatus")
        public String secQuesstatus;

    }
}
