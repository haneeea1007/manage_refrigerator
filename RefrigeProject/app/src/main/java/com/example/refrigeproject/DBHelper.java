package com.example.refrigeproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "refrigeratorDB";
    private static final int VERSION = 1;

    //데이터베이스 생성
    public DBHelper(Context context) {
        super(context, DB_NAME,null, VERSION);
        Log.d("ShowFoodsFragment", "constructor");
    }

    //테이블 생성
    @Override
    public void onCreate(SQLiteDatabase db) {
        String refrigeratorTBL = "CREATE TABLE refrigeratorTBL (code VARCHAR(20) PRIMARY KEY, " +
                "name VARCHAR(15), " +
                "imgResource INTEGER);";

        String foodTBL = "CREATE TABLE foodTBL (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "category CHAR(20), " +
                "section INTEGER, " +
                "name CHAR(100), " +
                "imagePath VARCHAR(300), " +
                "memo CHAR(100), " +
                "purchaseDate VARCHAR(20), " +
                "expirationDate VARCHAR(20)," +
                "code VARCHAR(20), " +
                "place VARCHAR(10), " +
                "FOREIGN KEY(code) REFERENCES refrigeratorTBL(code) on delete cascade);";

        String checkListTBL = "CREATE TABLE checkListTBL (" +
                "text VARCHAR(30) PRIMARY KEY," +
                "checked CHAR(10));";


        db.execSQL(refrigeratorTBL);
        db.execSQL(foodTBL);
        db.execSQL(checkListTBL);
    }

    //테이블을 삭제하고 다시 생성함
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS refrigeratorTBL;");
        onCreate(db);
    }
}