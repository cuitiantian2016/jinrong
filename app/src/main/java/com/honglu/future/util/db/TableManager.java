package com.honglu.future.util.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.honglu.future.config.Constant;
import com.honglu.future.util.SpUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableManager {
    // 添加极光消息
    public static void AddJPush(Context context, Map<String, String> map) {

        SimpleDateFormat dataFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String timeString = dataFormat.format(date);

        ContentValues values = new ContentValues();
        values.put("id", map.get("id"));
        values.put("phone", map.get("phone"));
        values.put("tag", "0");
        values.put("message", map.get("message"));
        DBManager.getDBconnection(context).insert("jpush_table", null, values);
    }

    /**
     * @param context
     * @param phone   根据手机号删除所有该手机号对应的未读消息
     */
    public static void DeleteByPhone(Context context, String phone) {
        DBManager.getDBconnection(context).delete("jpush_table", "phone=?", new String[]{phone});
    }

    public static void deleteTable(Context context){
        DBManager.getDBconnection(context).delete("jpush_table",null,null);
    }

    // 更新推送消息是否读过状态  传入id是需要修改id
    public static void UpdateJpush(Context context, Map<String, Object> mapupdate) {
        SQLiteDatabase db = DBManager.getDBconnection(context);
        ContentValues values = new ContentValues();
        values.put("id", mapupdate.get("id").toString());
        values.put("message", mapupdate.get("message").toString());
        values.put("tag", "1");
        values.put("phone", mapupdate.get("phone").toString());

        Log.e("判断更新ID", mapupdate.get("id").toString() + "");
        String[] args = {String.valueOf(mapupdate.get("id").toString())};
        db.update("jpush_table", values, "id=?", args);
    }


    // 查询推送消息封装成list0
    public static List<Map<String, Object>> queryAllJpush(Context context) {
        List<Map<String, Object>> jpush_list = new ArrayList<Map<String, Object>>();
        Cursor cursor = getAllJpushDb(context);
        if (cursor == null) {
            return jpush_list;
        } else {
            if (cursor.moveToFirst() == true) {
                for (int index = 0; index < cursor.getCount(); index++) {

                    Map<String, Object> map = new HashMap<String, Object>();
                    String str_id = cursor.getString(cursor
                            .getColumnIndex("id"));
                    String str_phone = cursor.getString(cursor
                            .getColumnIndex("phone"));
                    String str_tag = cursor.getString(cursor
                            .getColumnIndex("tag"));
                    String str_msg = cursor.getString(cursor
                            .getColumnIndex("message"));
                    map.put("id", str_id);
                    map.put("phone", str_phone);
                    map.put("tag", str_tag);
                    map.put("message", str_msg);
                    if (SpUtil.getString(Constant.CACHE_TAG_MOBILE).trim().equals(str_phone)) {
                        jpush_list.add(map);
                    }
                    cursor.moveToNext();
                }
                cursor.close();

            }
            return jpush_list;
        }
    }


    // 查询推送消息封装成list0
    public static List<Map<String, Object>> queryNoReadMsgJpush(Context context) {
        List<Map<String, Object>> jpush_list = new ArrayList<Map<String, Object>>();
        Cursor cursor = getAllJpushDb(context);
        if (cursor == null) {
            return jpush_list;
        } else {
            if (cursor.moveToFirst() == true) {
                for (int index = 0; index < cursor.getCount(); index++) {
                    String str_id = cursor.getString(cursor
                            .getColumnIndex("id"));
                    String str_phone = cursor.getString(cursor
                            .getColumnIndex("phone"));
                    String str_tag = cursor.getString(cursor
                            .getColumnIndex("tag"));
                    String str_msg = cursor.getString(cursor
                            .getColumnIndex("message"));

                    if (!TextUtils.isEmpty(str_phone) && SpUtil.getString(Constant.CACHE_TAG_MOBILE).trim().equals(str_phone)
                            && !TextUtils.isEmpty(str_tag) && str_tag.equals("0")){
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("id", str_id);
                        map.put("phone", str_phone);
                        map.put("tag", str_tag);
                        map.put("message", str_msg);
                        jpush_list.add(map);
                    }
                    cursor.moveToNext();
                }
                cursor.close();

            }
            return jpush_list;
        }
    }

    // 查询所有消息
    public static Cursor getAllJpushDb(Context context) {
        try {
            return DBManager.getDBconnection(context).query("jpush_table",
                    new String[]{"id", "phone", "tag", "message"}, null,
                    null, null, null, null);
        } catch (Exception e) {

            return null;
        }
    }


    public static String getTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        String strtime = formatter.format(curDate);
        return strtime;
    }


}

