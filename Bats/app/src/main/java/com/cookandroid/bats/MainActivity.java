package com.cookandroid.bats;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.security.keystore.KeyGenParameterSpec;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
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
    /** 위젯 변수 **/
    Button SignUp, Login, FindAcc;
    Boolean autologin;
    EditText ID, PW;
    /** 필드 **/
    public static Context main;
    String token;
    String BaseUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /** 위젯 변수 초기화 **/
        SignUp = (Button) findViewById(R.id.btn_signup);
        Login = (Button) findViewById(R.id.btn_login);
        FindAcc = (Button) findViewById(R.id.btn_find);
        ID = (EditText) findViewById(R.id.edit_id);
        PW = (EditText) findViewById(R.id.edit_pw);
        autologin = true;
        main = this;
        /** 필드 초기화 **/
        BaseUrl = "http://13.125.51.94:8000";

        /** 등록 토큰을 가져오는 설정 **/
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String tk = task.getResult();
                        token = tk;
                        // Log and use the token as needed
                        Log.d(TAG, tk+":This is a token!");
                    }
                });

        /** 인터넷 사용 설정 **/
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());

        /** 자동 로그인 저장 되어있을 경우 실행 루트 **/

        if (checkNotification(getApplicationContext()) == false) {

            AlertDialog.Builder msgBuilder = new AlertDialog.Builder(MainActivity.this)
                    .setTitle("알람 설정")
                    .setMessage("알람 설정이 되어있지 않습니다")
                    .setPositiveButton("권한 설정", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            goNotiSetting();
                        }
                    })
                    .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
            AlertDialog msgDlg = msgBuilder.create();
            msgDlg.show();


        }
        else if (checkInternet() == false) {
            Toast.makeText(getApplicationContext(),"인터넷 연결이 되어있지 않습니다",Toast.LENGTH_SHORT).show();
        }
        else {

            try {
                MasterKey masterkey = new MasterKey.Builder(getApplicationContext(), MasterKey.DEFAULT_MASTER_KEY_ALIAS)
                        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                        .build();

                SharedPreferences sharedPreferences = EncryptedSharedPreferences
                        .create(getApplicationContext(),
                                "autoLogin",
                                masterkey,
                                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);

                String text_id = sharedPreferences.getString("userId", null);
                String text_pw = sharedPreferences.getString("userPw", null);
                if (text_id != null || text_pw != null)
                    checkAccount(text_id, text_pw);
            } catch (Exception ex) {

            }
        }



        /** 회원 가입 창 **/
        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUp.class);
                intent.putExtra("token",token);
                intent.putExtra("url",BaseUrl);
                startActivity(intent);
            }
        });
        /** **/
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkNotification(getApplicationContext()) == false) {
                    AlertDialog.Builder msgBuilder = new AlertDialog.Builder(MainActivity.this)
                            .setTitle("알람 설정")
                            .setMessage("알람 설정이 되어있지 않습니다")
                            .setPositiveButton("권한 설정", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    goNotiSetting();
                                }
                            })
                            .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    return;
                                }
                            });
                    AlertDialog msgDlg = msgBuilder.create();
                    msgDlg.show();
                    return;
                }
                if (checkInternet() == false) {
                    Toast.makeText(getApplicationContext(),"인터넷 연결이 되어있지 않습니다",Toast.LENGTH_SHORT).show();
                    return;
                }



                String text_id = ID.getText().toString();
                String text_pw = PW.getText().toString();
                /** 자동로그인 실행 시 **/
                if(autologin) {

                    try {
                        MasterKey masterkey = new MasterKey.Builder(getApplicationContext(), MasterKey.DEFAULT_MASTER_KEY_ALIAS)
                                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                                .build();

                        SharedPreferences sharedPreferences = EncryptedSharedPreferences
                                .create(getApplicationContext(),
                                        "autoLogin",
                                        masterkey,
                                        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                                        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);

                        SharedPreferences.Editor spfEditor = sharedPreferences.edit();
                        spfEditor.clear();
                        spfEditor.commit();
                        //finish();

                        spfEditor.putString("userId", text_id);
                        spfEditor.putString("userPw", text_pw);
                        spfEditor.commit();

                    } catch (Exception ex) {

                    }

                }

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
                    autologin = true;
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

        RequestBody checkID = RequestBody.create(MediaType.parse("text/plain"), userId);
        RequestBody checkPW = RequestBody.create(MediaType.parse("text/plain"), userPass);

        // Retrofit 객체 생성
        Retrofit.Builder builder3 = new Retrofit.Builder()
                .baseUrl(BaseUrl)
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
                    intent.putExtra("url",BaseUrl);
                    intent.putExtra("id",userId);
                    intent.putExtra("token", token);
                    String[] data = {"name", "id", "token", "on_trade", "tr_unit", "krw_bal", "coin_bal","profit"};

                    try {
                        String responseData = response.body().string();
                        JSONObject json = new JSONObject(responseData);
                          for (int i=0; i<data.length; i++){
                              intent.putExtra(data[i],json.getString(data[i]));
                          }
                        //Log.d(TAG,"정보:"+json.getString("on_trade"));
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

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
    public boolean checkNotification(Context context) {
        return NotificationManagerCompat.from(context).areNotificationsEnabled();
    }
    public boolean checkInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            // 연결 On
            return true;
        } else {
            return false;
        }
    }
    public void goNotiSetting() {
        Intent intent = new Intent();
        intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
        // 알림 설정으로 이동할 패키지 이름 설정
        intent.putExtra("android.provider.extra.APP_PACKAGE", getPackageName());

        // 알림 설정 화면 열기
        startActivity(intent);

    }
    public void logout() {
        autologin = false;
        PW.setText("");
    }
}
