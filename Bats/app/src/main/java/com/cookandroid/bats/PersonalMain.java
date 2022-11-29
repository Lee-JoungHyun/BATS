package com.cookandroid.bats;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.CandleStickChart;
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
    Button onBtn, showlog;
    TextView state;
    ArrayList<CandleEntry> candleList = new ArrayList();
    private RequestQueue rQ;

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

            CandleDataSet candleDataSet = new CandleDataSet(tmp, "BTC markets");
            //candleDataSet.setColor();
            candleDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
            candleDataSet.setShadowColor(Color.BLACK);
            candleDataSet.setShadowWidth(2f);
            candleDataSet.setDecreasingColor(Color.BLUE);
            candleDataSet.setDecreasingPaintStyle(Paint.Style.FILL);
            candleDataSet.setIncreasingPaintStyle(Paint.Style.FILL);
            candleDataSet.setIncreasingColor(Color.RED);
            candleDataSet.setNeutralColor(Color.BLACK);

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
        onBtn = (Button) findViewById(R.id.onBtn);
        state = (TextView) findViewById(R.id.stateTxt);
        showlog = (Button) findViewById(R.id.btn_log);

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
    } //Timer 실행

}