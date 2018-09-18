package com.loancare.lakeview.volley;



public class Status
{
    private String CustomErrorCode;

    private String Message;



    public String getCustomErrorCode ()
    {
        return CustomErrorCode;
    }

    public void setCustomErrorCode (String CustomErrorCode)
    {
        this.CustomErrorCode = CustomErrorCode;
    }

    public String getMessage ()
    {
        return Message;
    }

    public void setMessage (String Message)
    {
        this.Message = Message;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [CustomErrorCode = "+CustomErrorCode+", Message = "+Message+"]";
    }
}
