package com.cookandroid.bats;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.angads25.toggle.interfaces.OnToggledListener;
import com.github.angads25.toggle.model.ToggleableView;
import com.github.angads25.toggle.widget.LabeledSwitch;
import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.interfaces.datasets.ICandleDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

public class PersonalMain extends AppCompatActivity {

    CandleStickChart candleStickChart;
    Button showlog, logOut;
    LabeledSwitch labeledSwitch;
    TextView state;
    ArrayList<CandleEntry> candleList = new ArrayList();
    private RequestQueue rQ;
    boolean flag;

    Timer timer = new Timer();
    TimerTask TT = new TimerTask() {
        @Override
        public void run() {
            drawChart();
        }
    };
    private void drawChart() {
        candleList.clear();
        String url = "https://api.upbit.com/v1/candles/minutes/1?market=KRW-BTC&count=15";
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        getTickerData(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Main", "onErrorResponse:" + error.getMessage());
                    }
                });
        request.setShouldCache(false);
        rQ.add(request);
    }
    private void getTickerData(String data) {
        try {
            JSONArray jsonArray = new JSONArray(data);

            for(int i=0; i<jsonArray.length(); i++) {

                JSONObject jsonObject = jsonArray.getJSONObject(i);

                int open = jsonObject.getInt("opening_price");
                int close = jsonObject.getInt("trade_price");
                int sHigh = jsonObject.getInt("high_price");
                int sLow = jsonObject.getInt("low_price");

                CandleEntry tmp = new CandleEntry((float)(jsonArray.length()-1-i), (float)sHigh, (float)sLow, (float)open, (float)close);
                candleList.add(tmp);
            }
            ArrayList<CandleEntry> tmp = new ArrayList();
            for(int i=candleList.size()-1; i>=0; i--) {
                tmp.add(candleList.get(i));
            }

            CandleDataSet candleDataSet = new CandleDataSet(tmp, "");
            //candleDataSet.setColor();
            candleDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
            candleDataSet.setShadowColor(Color.BLACK);
            candleDataSet.setShadowWidth(2f);
            candleDataSet.setDecreasingColor(Color.BLUE);
            candleDataSet.setDecreasingPaintStyle(Paint.Style.FILL);
            candleDataSet.setIncreasingPaintStyle(Paint.Style.FILL);
            candleDataSet.setIncreasingColor(Color.RED);
            candleDataSet.setNeutralColor(Color.BLACK);
            candleDataSet.setValueTextColor(Color.WHITE);
            //candleDataSet.setLabelTextColor(candleEntries, Color.WHITE);


            CandleData candleData = new CandleData(candleDataSet);
            candleStickChart.setData(candleData);
            candleStickChart.invalidate();

        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_main);
        candleStickChart = (CandleStickChart) findViewById(R.id.CandleChart);
        labeledSwitch = findViewById(R.id.onBtn);
        state = (TextView) findViewById(R.id.stateTxt);
        showlog = (Button) findViewById(R.id.btn_log);
        logOut = (Button) findViewById(R.id.logOut);
        flag = false;
        Description description = new Description();

        description.setText("BTC Price");
        description.setTextColor(Color.WHITE);
        //1a2436
        candleStickChart.setGridBackgroundColor(Color.WHITE);
        candleStickChart.setBackgroundColor(Color.parseColor("#000033"));
        candleStickChart.setDescription(description);
        YAxis leftAxis = candleStickChart.getAxisLeft();
        YAxis rightAxis = candleStickChart.getAxisRight();
        XAxis axis = candleStickChart.getXAxis();
        axis.setTextColor(Color.WHITE);
        rightAxis.setTextColor(Color.WHITE);
        leftAxis.setTextColor(Color.WHITE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel notificationChannel =
                    new NotificationChannel(
                            "Trading State",
                            "거래 현재 상황",
                            NotificationManager.IMPORTANCE_DEFAULT
                    );
            notificationChannel.setDescription("알람테스트");
            notificationManager.createNotificationChannel(notificationChannel);
        }

        Notification noti = new NotificationCompat.Builder(this, "Trading State")
                .setColor(Color.BLACK)
                .setSmallIcon(android.R.drawable.ic_notification_overlay)
                .setContentTitle("Bats 거래 상황")
                .setContentText("+0.03%")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setOngoing(true)
                .build();
        final NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    finish();

                }catch (Exception ex) {

                }
            }
        });
        labeledSwitch.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(ToggleableView toggleableView, boolean isOn) {
                if(isOn){
                    notificationManager.notify(0, noti);
                    Toast toast = Toast.makeText(getApplicationContext(), "On.",Toast.LENGTH_SHORT);
                    toast.show();
                }else{
                    notificationManager.cancel(0);
                    Toast toast = Toast.makeText(getApplicationContext(), "Off.",Toast.LENGTH_SHORT);
                    toast.show();
                }
            }

        });
        /**
        onBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    notificationManager.notify(0, noti);
                    buttonView.setText("On");
                    buttonView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_on, 0, 0, 0);
                }else{
                    notificationManager.cancel(0);
                    buttonView.setText("Off");
                    buttonView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_off, 0, 0, 0);
                }
            }


        });
         **/

        if (rQ == null) {
            //리퀘스트큐 생성 (MainActivit가 메모리에서 만들어질 때 같이 생성이 될것이다.
            rQ = Volley.newRequestQueue(getApplicationContext());
        }

        showlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ShowLogPopup.class);
                startActivity(intent);
            }
        });
        drawChart();
        (new Thread(new Runnable()
        {

            @Override
            public void run()
            {
                while (!Thread.interrupted())
                    try
                    {
                        Thread.sleep(60000);
                        runOnUiThread(new Runnable() // start actions in UI thread
                        {

                            @Override
                            public void run()
                            {
                                drawChart();
                            }
                        });
                    }
                    catch (InterruptedException e)
                    {
                        // ooops
                    }
            }
        })).start();



    }//Timer 실행
}