package com.honglu.future.util.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, "xn_db", null, 1);
    }

    //	//创建表
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TabSQL.JPUSH_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
    }


}
