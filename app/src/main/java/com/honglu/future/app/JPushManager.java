package com.honglu.future.app;

import android.app.Notification;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.honglu.future.BuildConfig;
import com.honglu.future.R;
import com.honglu.future.config.Constant;
import com.honglu.future.util.NetWorkUtils;
import com.honglu.future.util.SpUtil;
import com.honglu.future.util.ViewUtil;
import com.orhanobut.logger.Logger;

import java.util.Set;

import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Created by fangmingdong on 2017/3/10.
 */

public class JPushManager {
    private static final String TAG = "JPushManager";
    private String mAlias;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            JPushInterface.setAliasAndTags(App.getContext(), mAlias, null, mAliasCallback);
        }
    };

    public static JPushManager get(){
        return new JPushManager();
    }

    public static void init(Context context){
        JPushInterface.setDebugMode(BuildConfig.DEBUG); // 设置开启 JPush 日志,发布时请关闭日志
        JPushInterface.init(context);//初始化JPush

        BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(context);
        builder.statusBarDrawable = R.mipmap.ic_launcher;
        builder.notificationFlags = Notification.FLAG_AUTO_CANCEL;  //设置为点击后自动消失
        builder.notificationDefaults = Notification.DEFAULT_SOUND;  //设置为铃声（ Notification.DEFAULT_SOUND）或者震动（ Notification.DEFAULT_VIBRATE）
        JPushInterface.setPushNotificationBuilder(1, builder);
    }
    public void registerJPushAlias(){
        if(!(String.valueOf(SpUtil.getInt(Constant.CACHE_TAG_UID)).equals("0"))){
            mAlias = "0_" + String.valueOf(SpUtil.getInt(Constant.CACHE_TAG_UID));
        }else {
            mAlias = "1_" + String.valueOf(ViewUtil.getDeviceId(App.getContext()));
        }
        if(mAlias == null || mAlias.length() <= 2)return;

        mHandler.sendMessage(mHandler.obtainMessage(2));
    }

    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    Logger.i(TAG, logs);
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    Logger.i(TAG, logs);
                    if (NetWorkUtils.isNetworkEnable(App.getContext())) {
                        mHandler.sendMessageDelayed(mHandler.obtainMessage(2), 1000 * 60);
                    } else {
                        Logger.i(TAG, "No network");
                    }
                    break;
                default:
                    logs = "Failed with errorCode = " + code;
                    Log.e(TAG, logs);
            }
        }
    };
}
