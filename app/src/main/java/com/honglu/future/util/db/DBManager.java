package com.honglu.future.util.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * 数据存储操作管理类
 *
 */
public class DBManager {
	
	public static SQLiteDatabase db = null;
	
	
	
	
	/**
	 * 得到一个可写的数据库实例
	 * @param context
	 * @return
	 */
	public static SQLiteDatabase getDBconnection(Context context){
		if(db == null){
			 
			DBHelper dbHelper = new DBHelper(context);
			db = dbHelper.getWritableDatabase();
			 
		}
		return db;
	}
	
	/***
	 * 关闭数据库
	 */
	public static void dbClose(){
		if(db != null && db.isOpen()){
			db.close();
			db = null;
		}
	}
	
}
