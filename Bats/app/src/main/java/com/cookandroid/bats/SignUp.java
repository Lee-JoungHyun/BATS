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
    EditText Name,Id,Pw,rPw,Key,Pn,Email;
    Button IdCheck,PwCheck,EmailCheck,SignUp;
    protected void onCreate(Bundle savedInstanceState) {
        /** 통신 설정 초기화(baseurl 항상 바꿔줘야함!!!) **/
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://bb08-116-47-197-38.ngrok-free.app")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        IdAPI id_api = retrofit.create(IdAPI.class);
        /** **/
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

        /** 아이디 중복 확인 버튼 **/
        IdCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /** 대충 서버에 값을 보내서 이미 있는 아이디면, 안된다는 대답을
                 * 없으면 된다는 대답을 보내는 처리
                 */
                RequestBody id_check = RequestBody.create(MediaType.parse("text/plain"),Id.getText().toString());
                AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.getContext());
                Retrofit.Builder builder3 = new Retrofit.Builder()
                        .baseUrl("https://bb08-116-47-197-38.ngrok-free.app")
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
            String pw,rpw;
            @Override
            public void onClick(View view) {
                pw = Pw.getText().toString();
                rpw = rPw.getText().toString();
                AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.getContext());
                /** 서로 다르다면 **/
                if(!pw.equals(rpw)){
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

            }
        });
    }
}
