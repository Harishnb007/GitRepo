package com.loancare.lakeview.retrofitt;

import com.loancare.lakeview.Utils.Consts;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiClient
{

    //public static final String BASE_URL = "https://lcwebapi.test.servicelinkfnf.com/api/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient()
    {
        if (retrofit==null)
        {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Consts.loanCareUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
