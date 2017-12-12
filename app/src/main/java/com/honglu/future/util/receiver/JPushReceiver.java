package com.honglu.future.util.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.honglu.future.config.Constant;
import com.honglu.future.ui.home.bean.JpushBean;
import com.honglu.future.ui.main.activity.MainActivity;
import com.honglu.future.ui.main.activity.WebViewActivity;
import com.honglu.future.util.SpUtil;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by admin on 2017/3/9.
 */

public class JPushReceiver extends BroadcastReceiver {
    private static final String TAG = "JPush";
    private static String url = "";
    private static String jump = "";

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.e(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            //processCustomMessage(context, bundle);
            /*String title = intent.getStringExtra(JPushInterface.EXTRA_TITLE);
            String content = intent.getStringExtra(JPushInterface.EXTRA_MESSAGE);
            UdeskHelper.onNewMsg(context, title, content);*/
        } else {
            if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
                Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
                int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
                Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
                Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));

                String json = bundle.getString(JPushInterface.EXTRA_EXTRA);
                Gson gson = new Gson();
                JpushBean jpushBean = gson.fromJson(json, JpushBean.class);
                url = jpushBean.getUrl();
                jump = jpushBean.getJump();
                Log.d(TAG, "=====onReceive: jump--->" + jump + "url--->" + url);
            } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {

                Log.d(TAG, "onReceive: jump--->" + jump + "url--->" + url);

                if (!TextUtils.isEmpty(jump)){
                    ARouter.getInstance()
                            .build(Uri.parse(jump))
                            .navigation(context);
                }else {
                    String uid = SpUtil.getString(Constant.CACHE_TAG_UID);
                    if (TextUtils.isEmpty(uid) || TextUtils.isEmpty(url) || !url.startsWith("http")){
                        //打开主Activity
                        Intent i = new Intent(context, MainActivity.class);
                        i.putExtras(bundle);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(i);
                    }else {
                        Intent intenth5 = new Intent(context, WebViewActivity.class);
                        Bundle bundleh5 = new Bundle();
                        bundleh5.putString("url", url);
                        bundleh5.putString("title", "消息");
                        bundleh5.putString("tag", "");
                        intenth5.putExtras(bundleh5);
                        intenth5.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intenth5);
                    }
                }

            } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
                Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
                //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..
            } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
                boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
                Log.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
            } else {
                Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
            }
        }
    }

}
