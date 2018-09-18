package com.loancare.lakeview.GetSet;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class ResponseDetails {

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
        public secQuesstatus secQuesstatus;

        @SerializedName("secQuesCollection")
        public List<String> secQuesCollection ;

    }

    public class secQuesstatus
    {

        @SerializedName("SecurityStatus")
        public String SecurityStatus;

    }

}
