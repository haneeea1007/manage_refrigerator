package com.example.refrigeproject.show_foods;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AddFoodDBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "foodDB";
    private static final int VERSION = 1;

    // 데이터베이스 생성
    public AddFoodDBHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    // 테이블 생성
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String str = "CREATE TABLE addFoodTBL ("+
                "category VARCHAR(10) ," +
                "section VARCHAR(10)," +
                "image INTEGER," +
                "PRIMARY KEY(category, section));";
        sqLiteDatabase.execSQL(str);
    }

    // 테이블 삭제 후 다시 생성
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS addFoodTBL");
        onCreate(sqLiteDatabase);
    }

}
