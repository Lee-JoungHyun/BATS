package com.cookandroid.bats;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import androidx.appcompat.app.AlertDialog;
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
    /** 위젯 변수 **/
    EditText Name,Id,Pw,rPw,Key,Pn,Email;
    Button IdCheck,PwCheck,EmailCheck,SignUp;
    /** 필드 **/

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        /** EditText **/
        Name = (EditText)findViewById(R.id.signName);
        Id = (EditText)findViewById(R.id.signID);
        Pw = (EditText)findViewById(R.id.signPW);
        rPw = (EditText)findViewById(R.id.signPW2);
        Key = (EditText)findViewById(R.id.signBirth);
        Email = (EditText)findViewById(R.id.signmail);
        Pn = (EditText)findViewById(R.id.signPnum);
        /** Button **/
        IdCheck = (Button)findViewById(R.id.IDcheckbutton);
        PwCheck = (Button)findViewById(R.id.pwcheckbutton);
        EmailCheck = (Button) findViewById(R.id.Emailcheckbutton);
        SignUp = (Button)findViewById(R.id.signupbutton);
        /** Url 받아오기 **/
        String Url = getIntent().getStringExtra("url");
        /** 아이디 중복 확인 버튼 **/
        IdCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /** 서버에 값을 보내서 이미 있는 아이디면, 안된다는 대답을
                 * 없으면 된다는 대답을 보내는 처리
                 */
                RequestBody id_check = RequestBody.create(MediaType.parse("text/plain"),Id.getText().toString());
                AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.getContext());
                Retrofit.Builder builder3 = new Retrofit.Builder()
                        .baseUrl(Url)
                        .addConverterFactory(GsonConverterFactory.create());
                Retrofit retrofit3 = builder3.build();
                IdAPI id_api = retrofit3.create(IdAPI.class);
                Call<ResponseBody> call = id_api.id_check(id_check);
                call.enqueue(new Callback<ResponseBody>(){
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        // 전송 성공시 처리할 코드
                        int status_code = response.code();
                        if(status_code == 200){
                            builder.setMessage("사용 가능한 아이디입니다!");
                            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Do something when the OK button is clicked
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                        else if(status_code == 400){
                            builder.setMessage("이미 있는 아이디입니다!");
                            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Do something when the OK button is clicked
                                    Id.setText("");
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    }
                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        // 전송 실패시 처리할 코드
                    }
                });
            }
        });
        /** 비밀번호 확인 버튼 **/
        PwCheck.setOnClickListener(new View.OnClickListener() {
            String pw = "",rpw = "";
            @Override
            public void onClick(View view) {
                pw = Pw.getText().toString();
                rpw = rPw.getText().toString();
                //AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.getContext());
                /** 서로 다르다면 **/
                if(!pw.equals(rpw)){

                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.getContext());

                    builder.setMessage("비밀번호가 서로 다릅니다.");
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Do something when the OK button is clicked
                            Pw.setText("");
                            rPw.setText("");
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                /** 같다면 **/
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.getContext());

                    builder.setMessage("비밀번호 사용가능!");
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Do something when the OK button is clicked
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
        /** 이메일 인증 확인 버튼 **/
        EmailCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 이메일 보내기
                //String mailAdd = (String)Email.getText();
                //

            }
        });
        /** 회원가입 버튼 **/
        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /** 각 Emulator의 FCM 토큰 **/
                String token = getIntent().getStringExtra("token");
                /** 보낼 데이터 종류 **/
                RequestBody[] requestBodyArray = {
                        RequestBody.create(MediaType.parse("text/plain"), Name.getText().toString()),
                        RequestBody.create(MediaType.parse("text/plain"), Id.getText().toString()),
                        RequestBody.create(MediaType.parse("text/plain"), Pw.getText().toString()),
                        RequestBody.create(MediaType.parse("text/plain"), Key.getText().toString()),
                        RequestBody.create(MediaType.parse("text/plain"), Email.getText().toString()),
                        RequestBody.create(MediaType.parse("text/plain"), Pn.getText().toString()),
                        RequestBody.create(MediaType.parse("text/plain"), token)
                };
                /** 서버로 데이터 전송하는 코드 **/
                AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.getContext());
                Retrofit.Builder builder3 = new Retrofit.Builder()
                        .baseUrl(Url)
                        .addConverterFactory(GsonConverterFactory.create());
                Retrofit retrofit3 = builder3.build();
                SignUpAPI sign_api = retrofit3.create(SignUpAPI.class);
                Call<ResponseBody> call = sign_api.sign_up(requestBodyArray[0],requestBodyArray[1],
                        requestBodyArray[2],requestBodyArray[3],requestBodyArray[4],
                        requestBodyArray[5],requestBodyArray[6]);
                call.enqueue(new Callback<ResponseBody>(){
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        // 전송 성공시 처리할 코드
                        int status_code = response.code();
                        if(status_code == 200){
                            builder.setMessage("회원가입 완료!");
                            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Do something when the OK button is clicked
                                    /** 대충 확인 누르면 다시 로그인 화면으로 돌아감 **/
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                        else if(status_code == 400){
                            builder.setMessage("회원가입 실패! 이메일 입력란 오류");
                            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Do something when the OK button is clicked
                                    Key.setText("");
                                    Email.setText("");
                                    Pn.setText("");
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    }
                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        // 전송 실패시 처리할 코드
                    }
                });
            }
        });
        /** **/
    }
}
