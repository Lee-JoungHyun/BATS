package com.cookandroid.bats;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.interfaces.datasets.ICandleDataSet;

import java.util.ArrayList;

public class PersonalMain extends AppCompatActivity {

    CandleStickChart candleStickChart;
    Button onBtn;
    TextView state;
    ArrayList candleList = new ArrayList();
    ArrayList yValsCandleStick = new ArrayList();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_main);
        candleStickChart = (CandleStickChart) findViewById(R.id.CandleChart);
        onBtn = (Button) findViewById(R.id.onBtn);
        state = (TextView) findViewById(R.id.stateTxt);

        ///////////////////// 필요 없을 부분(캔들 임시 생성) ////////////////////////
        CandleEntry tmp = new CandleEntry (0, 500F, 100F, 200F, 300F);
        CandleEntry tmp2 = new CandleEntry (1, 450F, 200F,250F, 400F);
        CandleEntry tmp3 = new CandleEntry (2, 350F, 50F,270F, 100F);
        CandleEntry tmp4 = new CandleEntry(3, 200F, 70F,100F, 150F);
        CandleEntry tmp5 = new CandleEntry(4, 220F, 30F, 170F, 80F);

        candleList.add(tmp);
        candleList.add(tmp2);
        candleList.add(tmp3);
        candleList.add(tmp4);
        candleList.add(tmp5);
        ///////////////////// 필요 없을 부분(캔들 임시 생성) ////////////////////////

        CandleDataSet candleDataSet = new CandleDataSet(candleList, "BTC markets");
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
    }
}