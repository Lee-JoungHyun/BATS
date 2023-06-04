package com.cookandroid.bats;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ShowLogPopup extends Activity {
    ListView showlog;
    transactionDBHelper mHelper;
    ArrayList<String> log = new ArrayList<>();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.showlog_popup);
        showlog = (ListView) findViewById(R.id.logList);
        mHelper = new transactionDBHelper(this);

        try {
            SQLiteDatabase sqLiteDatabase = mHelper.getReadableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM contact", null);
            while (cursor.moveToNext()) {
                log.add(cursor.getString(1));
            }
        }catch (SQLiteException e){
            //Toast myToast = Toast.makeText(getApplicationContext(), "출력 오류", Toast.LENGTH_SHORT);
            //myToast.show();
        }finally {

        }
        //String[] log = {"22/11/16/22:00 - 2.331(ETC)매수[22,342,000]", "22/11/16/22:30 - 2.331(ETC)매도[22,140,000]", "22/11/16/23:00 - 2.331(ETC)매수[22,642,000]", "22/11/16/23:30 - 2.331(ETC)매도[22,521,000]", "22/11/17/00:00 - 2.331(ETC)매수[22,241,000]", "22/11/17/01:00 - 2.331(ETC)매도[22,412,000]", "22/11/17/02:00 - 2.331(ETC)매수[22,381,000]", "22/11/17/02:30 - 2.331(ETC)매도[22,764,000]", "22/11/17/03:00 - 2.331(ETC)매수[22,736,000]"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, log);
        showlog.setAdapter(adapter);
    }

}
