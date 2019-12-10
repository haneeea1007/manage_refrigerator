package com.example.refrigeproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FridgeDBHelper extends SQLiteOpenHelper {

    private static final  String DB_NAME = "refrigeDB";
    private static final  int VERSION = 1;

    //데이터베이스 생성
    public FridgeDBHelper(Context context) {
        super(context,DB_NAME,null,VERSION);
    }

    //테이블 생성
    @Override
    public void onCreate(SQLiteDatabase db) {
        String refrigeratorTBL = "CREATE TABLE refrigeratorTBL (code VARCHAR(20) PRIMARY KEY, name VARCHAR(15), imgResource INTEGER);";
        db.execSQL(refrigeratorTBL);
    }

    //테이블을 삭제하고 다시 생성함
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS refrigeratorTBL;");
        onCreate(db);
    }
}