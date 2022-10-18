package com.cookandroid.bats;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;



import androidx.appcompat.app.AppCompatActivity;

public class SignUp extends AppCompatActivity {
    Button EmailCheck;
    EditText Email;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        EmailCheck = (Button) findViewById(R.id.Emailcheckbutton);
        Email = (EditText) findViewById(R.id.signmail);




        //** 이메일 인증 확인 버튼 **//
        EmailCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 이메일 보내기
                //String mailAdd = (String)Email.getText();
                //

            }
        });

    }
}
