package com.cookandroid.bats;

import static android.service.controls.ControlsProviderService.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignUp extends AppCompatActivity {
    Button sign_up;
    EditText ID,PW;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        sign_up = (Button)findViewById(R.id.signupbutton);
        ID = (EditText)findViewById(R.id.signID);
        PW = (EditText)findViewById(R.id.signPW);

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerAccount(ID.getText().toString(),PW.getText().toString());
            }
        });
    }
    //회원가입 담당 메소드
    private void registerAccount(String userId, String userPass) {
        RequestBody identify = RequestBody.create(MediaType.parse("text/plain"), "1234");
        RequestBody password = RequestBody.create(MediaType.parse("text/plain"), "12345");


        // Retrofit 객체 생성
        Retrofit.Builder builder2 = new Retrofit.Builder()
                .baseUrl("https://087a-210-105-182-220.jp.ngrok.io")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit2 = builder2.build();

        MyAPI myAPI2 = retrofit2.create(MyAPI.class);

        // post 한다는 request를 보내는 부분.
        Call<ResponseBody> call = myAPI2.post_accounts(identify, password);
        // 만약 서버로 부터 response를 받는다면.
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Log.d(TAG,"계정 등록");
                    Toast.makeText(getApplicationContext(),"계정 등록에 성공하였습니다!",Toast.LENGTH_SHORT).show();
                }
                else {
                    Log.d(TAG,"Post Status Code : " + response.code());
                    Log.d(TAG,response.errorBody().toString());
                    Log.d(TAG,call.request().body().toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG,"Fail msg : " + t.getMessage());

            }
        });
    }
}
