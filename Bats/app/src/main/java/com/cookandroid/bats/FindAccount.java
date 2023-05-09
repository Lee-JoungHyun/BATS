package com.cookandroid.bats;

import android.annotation.SuppressLint;
import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.se.omapi.Session;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.util.Properties;
import java.util.Random;

@SuppressWarnings("deprecation")
public class FindAccount extends TabActivity {

    Button id_email, pw_email, id_cbtn, pw_cbtn;
    int mindef = 3, secdef = 0;
    EditText id_check, pw_check, email_id, email_pw, pw_id;
    String id_key , pw_key;
    String ID = "", PW = "";

    @SuppressLint("MissingInflatedId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_acc);

        id_email = (Button) findViewById(R.id.id_btnEmail);
        pw_email = (Button) findViewById(R.id.pw_btnEmail);
        id_cbtn = (Button) findViewById(R.id.id_btn_check);
        pw_cbtn = (Button) findViewById(R.id.pw_btn_check);
        id_check = (EditText) findViewById(R.id.id_vis1);
        pw_check = (EditText) findViewById(R.id.pw_vis1);
        email_id = (EditText) findViewById(R.id.id_email);
        email_pw = (EditText) findViewById(R.id.pw_email);
        pw_id = (EditText) findViewById(R.id.pw_ID);

        TabHost tabHost = getTabHost();

        TabHost.TabSpec tabSpecID = tabHost.newTabSpec("ID").setIndicator("ID찾기");
        tabSpecID.setContent(R.id.layout_id);
        tabHost.addTab(tabSpecID);

        TabHost.TabSpec tabSpecPW = tabHost.newTabSpec("PW").setIndicator("PW찾기");
        tabSpecPW.setContent(R.id.layout_pw);
        tabHost.addTab(tabSpecPW);

        tabHost.setCurrentTab(0);

        CountDownTimer countDownTimerID = new CountDownTimer(180000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                id_email.setText(getDate());
            }
            @Override
            public void onFinish() {
                id_email.setText("이메일 인증");
                id_email.setEnabled(true);
                id_email.setBackgroundColor(Color.BLUE);
                id_cbtn.setVisibility(View.GONE);
                id_check.setVisibility(View.GONE);
                mindef=3;
                secdef=0;
                email_id.setFocusableInTouchMode (true);
                email_id.setFocusable(true);
            }
        };
        CountDownTimer countDownTimerPW = new CountDownTimer(180000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                pw_email.setText(getDate());

            }
            @Override
            public void onFinish() {
                pw_email.setText("이메일 인증");
                pw_email.setEnabled(true);
                pw_email.setBackgroundColor(Color.BLUE);
                pw_cbtn.setVisibility(View.GONE);
                pw_check.setVisibility(View.GONE);
                mindef=3;
                secdef=0;
                email_pw.setFocusableInTouchMode (true);
                email_pw.setFocusable(true);
                pw_id.setFocusableInTouchMode (true);
                pw_id.setFocusable(true);
            }
        };

        id_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id_email.setEnabled(false);
                id_email.setBackgroundColor(Color.GRAY);
                id_check.setVisibility(View.VISIBLE);
                id_cbtn.setVisibility(View.VISIBLE);
                countDownTimerID.start();
                email_id.setClickable(false);
                email_id.setFocusable(false);
                /////////////// 이메일 인증 시작 ///////////////

                id_key = makeKey();
                sendEmail(email_id.getText().toString(), id_key);
            }
        });
        pw_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pw_email.setEnabled(false);
                pw_email.setBackgroundColor(Color.GRAY);
                pw_check.setVisibility(View.VISIBLE);
                pw_cbtn.setVisibility(View.VISIBLE);
                countDownTimerPW.start();
                email_pw.setClickable(false);
                email_pw.setFocusable(false);
                pw_id.setClickable(false);
                pw_id.setFocusable(false);
                /////////////// 이메일 인증 시작 ///////////////

                pw_key = makeKey();
                sendEmail(email_pw.getText().toString(), pw_key);
            }
        });
        id_cbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (id_key.equals(id_check.getText().toString())) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(FindAccount.this);
                    builder.setTitle("인증번호 확인");
                    builder.setMessage("ID = "+"");
                    builder.setPositiveButton("예", null);
                    builder.setNegativeButton("아니오", null);
                    builder.setNeutralButton("취소", null);
                    builder.create().show();
                    countDownTimerID.cancel();
                    countDownTimerID.onFinish();

                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(FindAccount.this);
                    builder.setTitle("인증번호 확인");
                    builder.setMessage("인증번호가 다릅니다!");
                    builder.setPositiveButton("예", null);
                    builder.setNegativeButton("아니오", null);
                    builder.setNeutralButton("취소", null);
                    builder.create().show();
                }
            }
        });
        pw_cbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email_id == pw_id) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(FindAccount.this);
                    builder.setTitle("인증번호 확인");
                    builder.setMessage("PW = " + "");
                    builder.setPositiveButton("예", null);
                    builder.setNegativeButton("아니오", null);
                    builder.setNeutralButton("취소", null);
                    builder.create().show();
                    countDownTimerPW.onFinish();
                    countDownTimerPW.cancel();

                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(FindAccount.this);
                    builder.setTitle("인증번호 확인");
                    builder.setMessage("인증번호가 다릅니다!");
                    builder.setPositiveButton("예", null);
                    builder.setNegativeButton("아니오", null);
                    builder.setNeutralButton("취소", null);
                    builder.create().show();
                }
            }
        });

    }
    private String makeKey() {
        String key = "";
        int randomStrLen = 15;
        Random random = new Random();
        StringBuffer randomBuf = new StringBuffer();
        for (int i = 0; i < randomStrLen; i++) {
            // Random.nextBoolean() : 랜덤으로 true, false 리턴 (true : 랜덤 소문자 영어, false : 랜덤 숫자)
            if (random.nextBoolean()) {
                // 26 : a-z 알파벳 개수
                // 97 : letter 'a' 아스키코드
                // (int)(random.nextInt(26)) + 97 : 랜덤 소문자 아스키코드
                randomBuf.append((char)((int)(random.nextInt(26)) + 97));
            } else {
                randomBuf.append(random.nextInt(10));
            }
        }
        String randomStr = randomBuf.toString();
        Toast.makeText(getBaseContext(), randomStr, Toast.LENGTH_LONG).show();
        return randomStr;
        // [createRandomStrUsingRandomBoolean] randomStr : iok887yt6sa31m99e4d6

    }
    private String getDate() {

        String min = String.format("%02d", mindef);
        String sec = String.format("%02d", secdef);

        if(secdef == 0){
            if(mindef==0) {
                mindef=0;
                secdef=0;
            }
            else{
            mindef--;
            secdef=59;}
        }else{
            secdef--;
        }

        return min + " : " + sec;
    }
    private void sendEmail(String add, String Key) {
        Intent email = new Intent(Intent.ACTION_SEND);
        email.setType("plain/text");
        // email setting 배열로 해놔서 복수 발송 가능
        String[] address = { add };
        email.putExtra(Intent.EXTRA_EMAIL, address);
        email.putExtra(Intent.EXTRA_SUBJECT,"BATS 회원 인증 메일");
        email.putExtra(Intent.EXTRA_TEXT,"BATS 회원의 인증 정보 입니다\n" + Key);
        try {
            startActivity(email);
        }catch (Exception e) {
            Toast.makeText(getBaseContext(), "메일 전송 오류!", Toast.LENGTH_LONG);
        }
    }

}
