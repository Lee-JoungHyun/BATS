package com.cookandroid.bats;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface TradeAPI {
    @Multipart
    @POST("/users/logined/trade/")
    Call<ResponseBody> auto_trade(
            @Part("id") RequestBody param1,
            @Part("token") RequestBody param2,
            @Part("tr_unit") RequestBody param3
    );
}
