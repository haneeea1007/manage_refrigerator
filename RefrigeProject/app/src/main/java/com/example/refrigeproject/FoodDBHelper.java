package com.example.refrigeproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FoodDBHelper extends SQLiteOpenHelper {

    private static final  String DB_NAME = "refrigeDB";
    private static final  int VERSION = 1;

    //데이터베이스 생성
    public FoodDBHelper(Context context) {
        super(context,DB_NAME,null,VERSION);
    }

    //테이블 생성
    @Override
    public void onCreate(SQLiteDatabase db) {
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
                "FOREIGN KEY(code) REFERENCES refrigeratorTBL(code) on delete cascade)  ;";

        db.execSQL(foodTBL);
    }

    //테이블을 삭제하고 다시 생성함
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS foodTBL;");
        onCreate(db);
    }
}