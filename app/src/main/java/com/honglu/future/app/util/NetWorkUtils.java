package com.honglu.future.app.util;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import com.orhanobut.logger.Logger;

public class NetWorkUtils {

    public static final String NETWORK_2G = "2G";
    public static final String NETWORK_3G = "3G";
    public static final String NETWORK_4G = "4G";
    public static final String NETWORK_WIFI = "WIFI";
    public static final String NERWORK_ALL = "ALL";
    public static final String NETWORKTYPE_INVALID = "NONE";

    private static final String TAG = "NetWorkUtils";

    private NetWorkUtils() {

    }

    /**
     * 获取网络状态
     *
     * @param context
     * @return
     */
    public static String getNetWorkType(Context context) {

        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        String strNetworkType = NETWORKTYPE_INVALID;
        if (networkInfo != null && networkInfo.isConnected()) {
            String type = networkInfo.getTypeName();

            if (type.equalsIgnoreCase("WIFI")) {

                strNetworkType = NETWORK_WIFI;
            } else if (type.equalsIgnoreCase("MOBILE")) {
                String _strSubTypeName = networkInfo.getSubtypeName();

                int networkType = networkInfo.getSubtype();
                switch (networkType) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
                        strNetworkType = NETWORK_2G;
                        break;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
                    case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
                    case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
                        strNetworkType = NETWORK_3G;
                        break;
                    case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
                        strNetworkType = NETWORK_4G;
                        break;
                    default:
                        // http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
                        if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA") || _strSubTypeName.equalsIgnoreCase("WCDMA") || _strSubTypeName.equalsIgnoreCase("CDMA2000")) {
                            strNetworkType = NETWORK_3G;
                        } else {
                            strNetworkType = _strSubTypeName;
                        }

                        break;
                }
            }
        } else {
            strNetworkType = NETWORKTYPE_INVALID;
        }

        Logger.i(TAG, "networkType =========>" + strNetworkType);
        return strNetworkType;
    }

    public static boolean isNetworkEnable(Context context) {
        return getNetWorkType(context) != NETWORKTYPE_INVALID;
    }

    /**
     * is gps enable
     */
    public static boolean isGpsEnable(Context context) {
        try {
            LocationManager locationManager = ((LocationManager) context
                    .getSystemService(Context.LOCATION_SERVICE));
            return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            Logger.d("isGpsEnable", "获取gps状态失败!!!");
            e.printStackTrace();
        }
        return false;
    }
}
