package com.cookandroid.bats;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
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

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PersonalMain extends AppCompatActivity {
    public static Context mContext;
    CandleStickChart candleStickChart;
    Button showlog, logOut, btn_set;
    LabeledSwitch labeledSwitch;
    TextView state;
    ArrayList<CandleEntry> candleList = new ArrayList();
    private RequestQueue rQ;
    boolean flag;
    transactionDBHelper mHelper;
    TextView txt_coin, txt_cash;

    Timer timer = new Timer();
    TimerTask TT = new TimerTask() {
        @Override
        public void run() {
            drawChart();
        }
    };

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

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
        txt_cash = (TextView) findViewById(R.id.txt_cash);
        txt_coin = (TextView) findViewById(R.id.txt_coin);
        btn_set = (Button) findViewById(R.id.btn_set);
        flag = false;
        Description description = new Description();
        mHelper = new transactionDBHelper(this);
        mContext = this;


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

        changeShowLogoBtn();


        /** PersonalMain 켜질 때 서버에 보내는 메시지 **/
        // 켜질 때 서버에게 0, 2, 3, 4 메시지를 받아야 한다!
        txt_cash.setText(getIntent().getStringExtra("krw_bal"));
        txt_coin.setText(getIntent().getStringExtra("coin_bal"));
        btn_set.setText(getIntent().getStringExtra("tr_unit"));
        String OnOff = getIntent().getStringExtra("on_trade");
        changelabel(OnOff);
// Notification 이벤트
        Intent intent = new Intent(getApplicationContext(), PersonalMain.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);

// Notification 생성



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel notificationChannel =
                    new NotificationChannel(
                            "Trading State",
                            "거래 현재 상황",
                            NotificationManager.IMPORTANCE_DEFAULT
                    );
            notificationChannel.setDescription("거래현황");
            notificationManager.createNotificationChannel(notificationChannel);
        }

        Notification noti = new NotificationCompat.Builder(this, "Trading State")
                .setColor(Color.BLACK)
                .setSmallIcon(R.drawable.logoimg)
                .setContentTitle("Bats 거래 상황")
                .setContentText("+0.03%")
                .setContentIntent(pendingIntent)
                .setAutoCancel(false)
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
                    ((MainActivity)MainActivity.main).logout();
                    finish();

                }catch (Exception ex) {

                }
            }
        });
        labeledSwitch.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(ToggleableView toggleableView, boolean isOn) {
                // 거래 시작 부분
                if(isOn){
                    notificationManager.notify(0, noti);
                    Toast toast = Toast.makeText(getApplicationContext(), "On.",Toast.LENGTH_SHORT);
                    toast.show();
                    /** DB 삭제 후 생성하는 코드 **/
                    SQLiteDatabase db = null;
                    try{
                        deleteDatabase(mHelper.DATABASE_NAME);
                        db = mHelper.getWritableDatabase();

                    }catch (SQLiteException e){
                        Toast myToast = Toast.makeText(getApplicationContext(), "생성 오류", Toast.LENGTH_SHORT);
                        myToast.show();
                    }finally {
                        if (db != null && db.isOpen()) {
                            db.close();
                        }
                    }

/** 여기다가 서버에 요청 하는 코드 **/
                    String Url = getIntent().getStringExtra("url");
                    String id = getIntent().getStringExtra("id");
                    String token = getIntent().getStringExtra("token");
                    RequestBody Id = RequestBody.create(MediaType.parse("text/plain"),id);
                    RequestBody Token = RequestBody.create(MediaType.parse("text/plain"),token);
                    RequestBody Tr_unit = RequestBody.create(MediaType.parse("text/plain"),getIntent().getStringExtra("tr_unit"));
                    AlertDialog.Builder builder = new AlertDialog.Builder(labeledSwitch.getContext());
                    Retrofit.Builder builder3 = new Retrofit.Builder()
                            .baseUrl(Url)
                            .addConverterFactory(GsonConverterFactory.create());
                    Retrofit retrofit3 = builder3.build();
                    TradeAPI id_api = retrofit3.create(TradeAPI.class);
                    Call<ResponseBody> call = id_api.auto_trade(Id,Token,Tr_unit);
                    call.enqueue(new Callback<ResponseBody>(){
                        @Override
                        public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                            // 전송 성공시 처리할 코드
                            int status_code = response.code();
                            if(status_code == 200){
                                builder.setMessage("거래가 시작되었습니다!");
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
                                builder.setMessage("요청이 실패했습니다!");
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
                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            // 전송 실패시 처리할 코드
                        }
                    });
                    // 거래 종료 부분
                }else{
                    notificationManager.cancel(0);
                    Toast toast = Toast.makeText(getApplicationContext(), "Off.",Toast.LENGTH_SHORT);
                    toast.show();
                    SQLiteDatabase db = null;
                    // 서버에 거래 종료 신호 보내기
                    RequestBody Id = RequestBody.create(MediaType.parse("text/plain"),getIntent().getStringExtra("id"));
                    String Url = getIntent().getStringExtra("url");
                    AlertDialog.Builder builder = new AlertDialog.Builder(labeledSwitch.getContext());
                    Retrofit.Builder builder3 = new Retrofit.Builder()
                            .baseUrl(Url)
                            .addConverterFactory(GsonConverterFactory.create());
                    Retrofit retrofit3 = builder3.build();
                    CancelTradeAPI id_api = retrofit3.create(CancelTradeAPI.class);
                    Call<ResponseBody> call = id_api.cancel_trade(Id);
                    call.enqueue(new Callback<ResponseBody>(){
                        @Override
                        public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                            // 전송 성공시 처리할 코드
                            int status_code = response.code();
                            if(status_code == 200){
                                builder.setMessage("거래가 취소되었습니다!");
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
                                builder.setMessage("거래요청이 실패했습니다!");
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
                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            // 전송 실패시 처리할 코드
                        }
                    });

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
        /** 로그 버튼 위치**/
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

        btn_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder setM = new AlertDialog.Builder(btn_set.getContext());
                final EditText M = new EditText(btn_set.getContext());

                setM.setTitle("거래 단위 변경");
                setM.setMessage("거래할 단위(KWR) 을 입력하시오");

                setM.setView(M);
                setM.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String unit = M.getText().toString();

                        if(Integer.parseInt(unit) < 5000) {

                        }else {
                            changeBtnSet(unit);
                            /** 서버에 거래단위 보내는거 필요!! **/
                            /** Url 받아오기 **/
                            String Url = getIntent().getStringExtra("url");
                            RequestBody Id = RequestBody.create(MediaType.parse("text/plain"),getIntent().getStringExtra("id"));
                            RequestBody Unit = RequestBody.create(MediaType.parse("text/plain"),unit);
                            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(PersonalMain.mContext);
                            Retrofit.Builder builder3 = new Retrofit.Builder()
                                    .baseUrl(Url)
                                    .addConverterFactory(GsonConverterFactory.create());
                            Retrofit retrofit3 = builder3.build();
                            ChangeUnitAPI unit_api = retrofit3.create(ChangeUnitAPI.class);
                            Call<ResponseBody> call = unit_api.change_unit(Id,Unit);
                            call.enqueue(new Callback<ResponseBody>(){
                                @Override
                                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                                    // 전송 성공시 처리할 코드
                                    int status_code = response.code();
                                    if(status_code == 200){
                                        builder.setMessage("거래단위가 변경되었습니다!");
                                        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                // Do something when the OK button is clicked
                                            }
                                        });
                                        androidx.appcompat.app.AlertDialog dialog = builder.create();
                                        dialog.show();
                                    }
                                    else if(status_code == 404){
                                        builder.setMessage("변경 불가!");
                                        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                // Do something when the OK button is clicked
                                            }
                                        });
                                        androidx.appcompat.app.AlertDialog dialog = builder.create();
                                        dialog.show();
                                    }
                                }
                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    // 전송 실패시 처리할 코드
                                }
                            });
                        }


                    }
                });
                setM.setNegativeButton("no",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });
                setM.show();
            }
        });



    } //Timer 실행


    public void changeLogBtn(String tmp){
        showlog.setText(tmp);
    }
    /** showLogo 버튼 변경 **/
    public void changeShowLogoBtn() {
        try {
            SQLiteDatabase sqLiteDatabase = mHelper.getReadableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM contact WHERE _id = (SELECT MAX(_id) FROM contact)", null);
            while (cursor.moveToNext()) {
                showlog.setText(cursor.getString(1));
            }
        }catch (SQLiteException e){
            Toast myToast = Toast.makeText(getApplicationContext(), "출력 오류", Toast.LENGTH_SHORT);
            myToast.show();
        }finally {

        }
    }
    public void changeTxtCash(String tmp) { txt_cash.setText("현재보유 현금 (KRW) : " + tmp);}
    public void changeTxtCoin(String tmp) { txt_coin.setText("현재보유 코인 (ETC) : " + tmp);}
    public void changeBtnSet(String tmp) { btn_set.setText("거래 금액 단위 : " + tmp);}
    public void changeTxtState(String tmp) {

        if (Integer.parseInt(tmp) > 0) {
            state.setTextColor(Color.RED);
            state.setText("수익률 : +" + tmp + "%");
        }
        else {
            state.setTextColor(Color.BLUE);
            state.setText("수익률 : -" + tmp + "%");
        }

    }
    public void changelabel(String tmp){
        if (tmp.equals("true")) {
            labeledSwitch.setOn(true);

        }else{
            labeledSwitch.setOn(false);
        }
    }
}