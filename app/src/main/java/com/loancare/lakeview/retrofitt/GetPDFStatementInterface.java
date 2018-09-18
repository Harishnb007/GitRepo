package com.loancare.lakeview.retrofitt;

import com.loancare.lakeview.GetSet.ForgetPasswordDetails;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by dinesh on 13/03/18.
 */

public interface GetPDFStatementInterface {




    @Headers("Content-Type:application/json")
    @POST("api/user/loan/getstatementspdf")
    Call<ResponseBody> getPDF(@Header("AuthorizationToken") String token, @Body GetStatement statement);

    @Headers("Content-Type:application/json")
    @POST("user/loan/getstatementspdf")
    Call<ForgetPasswordDetails> Forget(@Body ForgetPasswordInput statement);

}



