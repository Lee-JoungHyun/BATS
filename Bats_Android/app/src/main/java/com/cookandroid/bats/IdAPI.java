package com.cookandroid.bats;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface IdAPI {
    @Multipart
    @POST("/users/sign_up/id_check/")
    Call<ResponseBody> id_check(
            @Part("id") RequestBody param1
    );
}
