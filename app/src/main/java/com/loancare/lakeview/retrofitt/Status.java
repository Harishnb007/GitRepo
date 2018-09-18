package com.loancare.lakeview.retrofitt;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Status
{
    @SerializedName("CustomErrorCode")
    @Expose
    private Integer customErrorCode;
    @SerializedName("Message")
    @Expose
    private String message;

    public Integer getCustomErrorCode()
    {
        return customErrorCode;
    }

    public void setCustomErrorCode(Integer customErrorCode)
    {
        this.customErrorCode = customErrorCode;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }


}
