package com.loancare.lakeview.GetSet;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 886016 on 3/14/2018.
 */

public class ForgetPass {

    @SerializedName("status")
    public Status status;


    @SerializedName("data")
    public String datastring;

    public class Status
    {
        @SerializedName("CustomErrorCode")
        public String CustomErrorCode;

        @SerializedName("Message")
        public String Message;

    }
}
