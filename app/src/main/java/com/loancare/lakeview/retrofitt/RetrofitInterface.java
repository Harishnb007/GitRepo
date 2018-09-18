package com.loancare.lakeview.retrofitt;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RetrofitInterface

 {


     @FormUrlEncoded
     @POST("apiqa/authenticate")
     Call<ResponseDetails> getLogin (@Body Userinput input);
    /* Call<ResponseDetails> getLogin (@Field("username") String username,
                                     @Field("password") String password,
                                     @Field("resourcename") String resourcename,
                                     @Field("log") String log);*/


 }

