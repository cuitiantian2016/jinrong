package com.honglu.future.app.util;

import android.util.Log;

import com.xulu.xnpacket.app.App;

/**
 * Created by hc on 2017/6/19.
 */

public class Logs {
    private static final boolean LOG = App.getConfig().isDebug();
    public static void d(String tag, String msg) {
        if(LOG) {
            Log.d(tag, msg);
        }
    }
    public static void v(String tag, String msg) {
        if(LOG) {
            Log.v(tag, msg);
        }
    }
    public static void e(String tag, String msg, Exception e) {
        if(LOG) {
            Log.e(tag, msg, e);
        }
    }
    public static void e(String tag, String msg) {
        if(LOG) {
            Log.e(tag, msg);
        }
    }
    public static void e(String msg){
        if(LOG) {
            Log.e("xiaoniu", msg);
        }

    }
    public static void i(String tag, String msg) {
        if(LOG) {
            Log.i(tag, msg);
        }
    }
}
