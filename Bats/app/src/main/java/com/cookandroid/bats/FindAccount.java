package com.cookandroid.bats;

import android.annotation.SuppressLint;
import android.app.TabActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.style.TabStopSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@SuppressWarnings("deprecation")
public class FindAccount extends TabActivity {

    Button id_email, pw_email, id_cbtn, pw_cbtn;
    int mindef = 3, secdef = 0;
    EditText id_check, pw_check;
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
                pw_check.setVisibility(View.GONE);
                mindef=3;
                secdef=0;
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
            }
        });

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
}
