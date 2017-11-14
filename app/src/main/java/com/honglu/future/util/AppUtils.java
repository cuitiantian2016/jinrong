package com.honglu.future.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.honglu.future.BuildConfig;

/**
 * Created by zq on 2017/8/3.
 */

public class AppUtils {
    public static String MARKET_ID;

    /**
     * 返回当前程序包名
     */
    public static String getPackageName() {
        return BuildConfig.APPLICATION_ID;
    }

    /**
     * 返回当前程序包名
     */
    public static boolean getDebug() {
        return BuildConfig.DEBUG;
    }

    /**
     * 返回当前程序版本名
     */
    public static String getVersionName() {
        return BuildConfig.VERSION_NAME;
    }

    /**
     * 返回当前程序版本号
     */
    public static int getVersionCode() {
        return BuildConfig.VERSION_CODE;
    }

    public static String getMarketId(Context context) {
        if (!TextUtils.isEmpty(MARKET_ID)) {
            return MARKET_ID;
        }
        try {
            ApplicationInfo appInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            MARKET_ID = String.valueOf(appInfo.metaData.get("UMENG_CHANNEL"));
        } catch (Exception e1) {
            e1.printStackTrace();
            MARKET_ID = "";
        }
        return MARKET_ID;
    }
}
