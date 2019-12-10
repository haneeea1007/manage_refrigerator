package com.example.refrigeproject.checklist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CheckListDBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "refrigeDB";
    private static final int VERSION = 1;

    // 데이터베이스 생성
    public CheckListDBHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    // 테이블 생성
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String str = "CREATE TABLE CheckListTBL (" +
                "text VARCHAR(30) PRIMARY KEY," +
                "checked CHAR(10));";
        sqLiteDatabase.execSQL(str);
    }

    // 테이블 삭제 후 다시 생성
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS CheckListTBL");
        onCreate(sqLiteDatabase);
    }
}
