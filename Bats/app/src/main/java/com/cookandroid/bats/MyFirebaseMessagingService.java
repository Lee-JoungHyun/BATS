package com.cookandroid.bats;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // 데이터 메시지 처리 로직을 여기에 구현
        if (remoteMessage.getData().size() > 0) {
            // 데이터 메시지에서 필요한 데이터 추출
            String key1 = remoteMessage.getData().get("key1");
            String key2 = remoteMessage.getData().get("key2");
            String key3 = remoteMessage.getData().get("key3");
            String key4 = remoteMessage.getData().get("key4");
            // 수신된 데이터를 기반으로 필요한 작업 수행
            switch (key1) {
                // Notification 처리
                case "0":
                    changeNotification(key2);
                    Handler handler0 = new Handler(Looper.getMainLooper());
                    handler0.post(new Runnable() {
                        @Override
                        public void run() {
                            // UI 변경 작업 수행
                            ((PersonalMain)PersonalMain.mContext).changeTxtState(key2);
                        }
                    });

                    break;
                // 거래내역 SQLite 처리
                case "1":
                    insertSQLite(key2);
                    try {
                        Handler handler1 = new Handler(Looper.getMainLooper());
                        handler1.post(new Runnable() {
                            @Override
                            public void run() {
                                // UI 변경 작업 수행
                                ((PersonalMain) PersonalMain.mContext).changeLogBtn(key2);
                            }
                        });

                    }catch (NullPointerException e){

                    }

                    break;
                // 현재 보유 현금 바꾸기 (숫자)
                case "2":
                    Handler handler2 = new Handler(Looper.getMainLooper());
                    handler2.post(new Runnable() {
                        @Override
                        public void run() {
                            // UI 변경 작업 수행
                            ((PersonalMain)PersonalMain.mContext).changeTxtCash(key2);
                        }
                    });

                    break;
                // 현재 보유 코인량 바꾸기
                case "3":
                    Handler handler3 = new Handler(Looper.getMainLooper());
                    handler3.post(new Runnable() {
                        @Override
                        public void run() {
                            // UI 변경 작업 수행
                            ((PersonalMain)PersonalMain.mContext).changeTxtCoin(key2);

                            // 현재 보유 코인량 바꾸기
                            ((PersonalMain)PersonalMain.mContext).changeTxtCoin(key3);
                            // 현재 보유 현금 바꾸기 (숫자)
                            ((PersonalMain)PersonalMain.mContext).changeTxtCash(key4);
                        }
                    });
                    break;
                // 거래 단위 금액
                case "4":
                    Handler handler4 = new Handler(Looper.getMainLooper());
                    handler4.post(new Runnable() {
                        @Override
                        public void run() {
                            // UI 변경 작업 수행
                            ((PersonalMain)PersonalMain.mContext).changeBtnSet(key2);
                        }
                    });

            }
        }
    }
    private void changeNotification(String tmp) {
        Notification noti = new NotificationCompat.Builder(this, "Trading State")
                .setColor(Color.BLACK)
                .setSmallIcon(android.R.drawable.ic_notification_overlay)
                .setContentTitle("Bats 거래 상황")
                .setContentText(tmp + "%")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setOngoing(true)
                .build();
        final NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, noti);
    }
    private void insertSQLite(String tmp) {
        SQLiteDatabase db = null;
        try {
            transactionDBHelper mHelper = new transactionDBHelper(this);
            db = mHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("recode", tmp);
            db.insert("contact", null, values);
            mHelper.close();
        }catch (SQLiteException e) {
            Toast myToast = Toast.makeText(getApplicationContext(), "입력 오류", Toast.LENGTH_SHORT);
            myToast.show();
        } finally {
            if(db != null && db.isOpen()) {
                db.close();
            }
        }

    }

}

