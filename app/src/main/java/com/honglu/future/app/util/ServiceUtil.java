package com.honglu.future.app.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;

import com.xulu.xnpacket.app.App;
import com.xulu.xnpacket.config.Constant;
import com.xulu.xnpacket.ui.my.service.UploadPOIService;

import java.util.List;

public class ServiceUtil {

    public static boolean isServiceRunning(Context context, String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceInfos = activityManager.getRunningServices(Constant
                .RETRIVE_SERVICE_COUNT);

        if (null == serviceInfos || serviceInfos.size() < 1) {
            return false;
        }
        for (int i = 0; i < serviceInfos.size(); i++) {
            if (serviceInfos.get(i).service.getClassName().contains(className)) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }

    /**
     * 启动service
     * @param context
     */
    public static void invokeTimerPOIService(Context context) {
        if (App.getConfig().getLoginStatus()) {
            Intent startIntent = new Intent(App.getContext(), UploadPOIService.class);
            startIntent.putExtra("isLogin", App.getConfig().getLoginStatus());
            context.startService(startIntent);
        }
    }

    /**
     * 取消service
     * @param context
     */
    public static void cancelAlarmManager(Context context) {
        if (!App.getConfig().getLoginStatus()) {
            Intent intent = new Intent(App.getContext(), UploadPOIService.class);
            intent.putExtra("isLogin", App.getConfig().getLoginStatus());
            context.startService(intent);
        }
    }
}
