package com.cookandroid.bats;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    Button SignUp, Login, FindAcc;
    EditText ID, PW;
    String info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 인터넷 사용 설정 //
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());

        SignUp = (Button) findViewById(R.id.btn_signup);
        Login = (Button) findViewById(R.id.btn_login);
        FindAcc = (Button) findViewById(R.id.btn_find);
        ID = (EditText) findViewById(R.id.edit_id);
        PW = (EditText) findViewById(R.id.edit_pw);

        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUp.class);
                startActivity(intent);
            }
        });
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text_id = ID.getText().toString();
                String text_pw = PW.getText().toString();
                //로그인 정보 미입력 시
                if (text_id.trim().length() == 0 || text_pw.trim().length() == 0 || text_id == null || text_pw == null) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("알림")
                            .setMessage("로그인 정보를 입력바랍니다.")
                            .setPositiveButton("확인", null)
                            .create()
                            .show();
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
                else {
                    //로그인 성공시 오픈
                    checkAccount(text_id,text_pw);
                }

            }
        });
        FindAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FindAccount.class);
                startActivity(intent);
            }
        });
    }
    private void checkAccount(String userId, String userPass) {
        //
        RequestBody checkID = RequestBody.create(MediaType.parse("text/plain"), userId);
        RequestBody checkPW = RequestBody.create(MediaType.parse("text/plain"), userPass);

        // Retrofit 객체 생성
        Retrofit.Builder builder3 = new Retrofit.Builder()
                .baseUrl("https://672c-15-164-97-20.jp.ngrok.io/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit3 = builder3.build();

        MyAPI myAPI3 = retrofit3.create(MyAPI.class);

        // post 한다는 request를 보내는 부분.
        Call<ResponseBody> call = myAPI3.post_check(checkID, checkPW);
        // 만약 서버로 부터 response를 받는다면.
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Log.d(TAG,"계정 확인 완료용!!"+response.toString());
                    Toast.makeText(getApplicationContext(),userId+"님 환영합니다!",Toast.LENGTH_SHORT).show();

//                     로그인. Main Activity2 를 호출한다. (갤러리와 이미지 처리 버튼이 나오는 부분이다)
//                     text view 내의 값들이 db에 있는 경우
                    Intent intent = new Intent(getApplicationContext(), PersonalMain.class);
                    ResponseBody body = response.body();
                    try{
                        info = body.string();
                    }
                    catch(IOException e){
                        e.printStackTrace();
                    }
                    intent.putExtra("Info", info); // Verify된 경우 userId 다음 액티비티로 전달하기
                    startActivity(intent);
                }else {
                    Log.d(TAG,"Post Status Code ㅠㅠ : " + response.code());
                    Toast.makeText(getApplicationContext(),"계정 없어용!!!!!",Toast.LENGTH_SHORT).show();
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
