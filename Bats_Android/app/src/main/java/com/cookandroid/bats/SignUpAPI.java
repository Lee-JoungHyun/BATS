package com.cookandroid.bats;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface SignUpAPI {
    @Multipart
    @POST("users/sign_up/")
    Call<ResponseBody> sign_up(
            @Part("name") RequestBody param1,
            @Part("id") RequestBody param2,
            @Part("access_key") RequestBody param3,
            @Part("password") RequestBody param4,
            @Part("email") RequestBody param5,
            @Part("phone") RequestBody param6,
            @Part("token") RequestBody param7
    );
}

