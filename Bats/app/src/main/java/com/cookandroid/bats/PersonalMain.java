package com.cookandroid.bats;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.interfaces.datasets.ICandleDataSet;

import java.util.ArrayList;

public class PersonalMain extends AppCompatActivity {

    //-- 차트 그리기 위한 하나의 캔들 클래스 --//
    class Candle {
        Candle(float createAt, float open, float close, float shadowHigh, float shadowLow) {
            this.createAt = createAt;
            this.open = open;
            this.close = close;
            this.shadowHigh = shadowHigh;
            this.shadowLow = shadowLow;
            if(open > close)
                this.red = false;
            else
                this.red = true;
        }
        float createAt;
        float open;
        float close;
        float shadowHigh;
        float shadowLow;
        boolean red;
    }
    CandleStickChart candleStickChart;
    Button onBtn;
    TextView state;
    ArrayList candleList = new ArrayList();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_main);
        candleStickChart = (CandleStickChart) findViewById(R.id.CandleChart);
        onBtn = (Button) findViewById(R.id.onBtn);
        state = (TextView) findViewById(R.id.stateTxt);

        ///////////////////// 필요 없을 부분(캔들 임시 생성) ////////////////////////
        Candle tmp = new Candle(0, 200F, 300F, 500F, 100F);
        Candle tmp2 = new Candle(1, 250F, 400F, 450F, 200F);
        Candle tmp3 = new Candle(2, 270F, 100F, 3500F, 50F);
        Candle tmp4 = new Candle(3, 100F, 150F, 200F, 70F);
        Candle tmp5 = new Candle(4, 170F, 80F, 220F, 30F);

        candleList.add(tmp);
        candleList.add(tmp2);
        candleList.add(tmp3);
        candleList.add(tmp4);
        candleList.add(tmp5);
        ///////////////////// 필요 없을 부분(캔들 임시 생성) ////////////////////////
        candleStickChart.setHighlightPerDragEnabled(true);

        candleStickChart.setDrawBorders(true);
        candleStickChart.setBorderColor(getResources().getColor(R.color.black));
        YAxis yAxis = candleStickChart.getAxisLeft();
        YAxis rightAxis = candleStickChart.getAxisRight();
        yAxis.setDrawGridLines(false);
        rightAxis.setDrawGridLines(false);
        candleStickChart.requestDisallowInterceptTouchEvent(true);
        XAxis xAxis = candleStickChart.getXAxis();
        xAxis.setDrawGridLines(false);// disable x axis grid lines
        xAxis.setDrawLabels(false);
        rightAxis.setTextColor(android.R.color.white);
        yAxis.setDrawLabels(false);
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setAvoidFirstLastClipping(true);
        Legend l = candleStickChart.getLegend();
        l.setEnabled(false);



        for (int i=0; i<candleList.size(); i++) {

        }

    }
}