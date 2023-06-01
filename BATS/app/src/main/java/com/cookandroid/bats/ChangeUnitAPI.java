package com.cookandroid.bats;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ChangeUnitAPI {
    @Multipart
    @POST("/users/logined/change_unit/")
    Call<ResponseBody> change_unit(
            @Part("id") RequestBody param1,
            @Part("tr_unit") RequestBody param2
    );
}
