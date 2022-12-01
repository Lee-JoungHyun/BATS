package com.cookandroid.bats;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface MyAPI {
    @Multipart
    @POST("users/login/")
    Call<ResponseBody> post_check(
            @Part("id") RequestBody param1,
            @Part("password") RequestBody param2
    );
}
