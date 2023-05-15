package com.cookandroid.bats;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class transactionDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "transaction.db";
    private static final int DATABASE_VERSTION = 2;

    public transactionDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSTION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE contact ( _id INTEGER PRIMARY KEY AUTOINCREMENT, " + "recode TEXT);");
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS contact");
        onCreate(db);
    }
    public void onDelete(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS contact");
    }

}
