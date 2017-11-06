package com.honglu.future.mpush;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.xulu.mpush.api.Logger;

/**
 * Created by ohun on 16/9/22.
 */

public class MyLog implements Logger {
    private static final String TAG = "MyLog";

    private MPushLog mPushLog;

    public MyLog() {
        this.mPushLog = new MPushLog();
    }

    @Override
    public void enable(boolean b) {
        this.mPushLog.enable(true);
    }

    @Override
    public void d(String s, Object... objects) {
        mPushLog.d(s, objects);
    }

    @Override
    public void i(String s, Object... objects) {
        mPushLog.i(s, objects);
    }

    @Override
    public void w(String s, Object... objects) {
        mPushLog.w(s, objects);
    }

    @Override
    public void e(Throwable throwable, String s, Object... objects) {
        mPushLog.e(throwable, s, objects);
    }

}
