package com.honglu.future.util;

import android.app.ActivityManager;
import android.content.Context;
import com.honglu.future.config.Constant;

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

}
