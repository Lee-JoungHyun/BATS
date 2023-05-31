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
            @Part("private_key") RequestBody param3,
            @Part("access_key") RequestBody param4,
            @Part("password") RequestBody param5,
            @Part("email") RequestBody param6,
            @Part("phone") RequestBody param7,
            @Part("token") RequestBody param8
    );
}

