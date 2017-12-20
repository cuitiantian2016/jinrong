package com.honglu.future.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.honglu.future.R;
import com.honglu.future.app.App;
import com.honglu.future.config.Constant;
import com.sobot.chat.SobotApi;
import com.sobot.chat.listener.HyperlinkListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.jpush.android.api.JPushInterface;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 * android系统相关常用操作
 * 说明：提供一些android系统常用操作：如系统版本，图片操作等
 */
public class AndroidUtil {


    private static int mScreenWidth, mScreenHeight;//

    public static int getScreenWidth(Activity act) {
        if (mScreenWidth < 1) {
            DisplayMetrics dm = new DisplayMetrics();
            act.getWindowManager().getDefaultDisplay().getMetrics(dm);
            mScreenWidth = dm.widthPixels;
            mScreenHeight = dm.heightPixels;
        }
        return mScreenWidth;

    }
    public static void setEmojiFilter(EditText et) {
        InputFilter emojiFilter = new InputFilter() {
            Pattern pattern = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|" +
                    "[\u2600-\u27ff]", Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);

            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                Matcher matcher = pattern.matcher(source);
                if (matcher.find()) {
                    ToastUtil.show("暂不支持表情输入!");
                    return "";
                }
                return null;
            }
        };
        et.setFilters(new InputFilter[]{emojiFilter});
    }
    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    public static int getScreenHeight(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }


    public static String getAssets(Context context, String fileName) {

        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public static GradientDrawable buildGradientDrawable() {
        int strokeWidth = 1; // 1dp 边框宽度
        int roundRadius = 100; // 2dp 圆角半径
        int strokeColor = 0xab000000;//边框颜色
        int fillColor = 0xab000000;//内部填充颜色
        GradientDrawable gradientDrawable = new GradientDrawable();//创建drawable
        gradientDrawable.setColor(fillColor);
        gradientDrawable.setCornerRadius(roundRadius);
        gradientDrawable.setStroke(strokeWidth, strokeColor);
        return gradientDrawable;
    }

    public static GradientDrawable buildQuickReportDrawable() {
        int strokeWidth = 1; // 1dp 边框宽度
        int roundRadius = 4; // 2dp 圆角半径
        int strokeColor = 0xb3ffffff;//边框颜色
        int fillColor = 0xb3ffffff;//内部填充颜色
        GradientDrawable gradientDrawable = new GradientDrawable();//创建drawable
        gradientDrawable.setColor(fillColor);
        gradientDrawable.setCornerRadius(roundRadius);
        gradientDrawable.setStroke(strokeWidth, strokeColor);
        return gradientDrawable;
    }

    public static int getScreenHeight(Activity act) {
        if (mScreenHeight == 0) {
            getScreenWidth(act);
        }
        return mScreenHeight;
    }

    /**
     * /2017-02-26 17:57:55.0 截取解析
     *
     * @return
     */
    public static String splitDateNew(String datestr) {
        //2017-02-26 17:57:55.0
        String strnewdate = "";
        if (datestr.length() > 16) {
            String datestr1 = datestr.substring(0, 10);
            String datestr2 = datestr.substring(11, 16);
            try {
                if (IsToday(datestr1)) {
                    strnewdate = datestr2;
                } else {
                    if (IsYesterday(datestr1)) {
                        strnewdate = " 昨天";
                    } else {
                        strnewdate = datestr.substring(5, 10);
                    }

                }

            } catch (Exception e) {

            }

        }
        return strnewdate;
    }

    /**
     * 获取sdk版本
     *
     * @return
     */
    public static int getAndroidSDKVersion() {
        return android.os.Build.VERSION.SDK_INT;
    }

    public static String getUA(Context context) {

        String ua = "feilu://" + AppUtils.getVersionName() + " (Android;android" + AndroidUtil.getAndroidSDKVersion() + ";zh_CN;ID:2-" + SpUtil.getString(Constant.CACHE_TAG_UID) + "-" + AppUtils.getMarketId(context) + "-" + JPushInterface.getRegistrationID(context) + ")";
        return ua;
    }


    /**
     * 判断当前日期是星期几
     *
     * @param pTime 设置的需要判断的时间  //格式如2012-09-08
     * @return dayForWeek 判断结果
     * @Exception 发生异常
     */
    public static int getWeek(String pTime) {
        int int_week = 0;
        String Week = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(format.parse(pTime));
            //Toast.makeText(this, c.getTime()+"", Toast.LENGTH_SHORT).show();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if ((c.get(Calendar.DAY_OF_WEEK)) == 1) {
            Week += "天";
            int_week = 0;
        }
        if ((c.get(Calendar.DAY_OF_WEEK)) == 2) {
            Week += "一";
            int_week = 1;
        }
        if ((c.get(Calendar.DAY_OF_WEEK)) == 3) {
            Week += "二";
            int_week = 2;
        }
        if ((c.get(Calendar.DAY_OF_WEEK)) == 4) {
            Week += "三";
            int_week = 3;
        }
        if ((c.get(Calendar.DAY_OF_WEEK)) == 5) {
            Week += "四";
            int_week = 4;
        }
        if ((c.get(Calendar.DAY_OF_WEEK)) == 6) {
            Week += "五";
            int_week = 5;
        }
        if ((c.get(Calendar.DAY_OF_WEEK)) == 7) {
            Week += "六";
            int_week = 6;
        }
        return int_week;
    }

    //2月转成二月
    public static String getNewMonth(String strmonth) {
        String newstr = "";
        if (strmonth.equals("01")) {
            newstr = "一";
        } else if (strmonth.equals("02")) {
            newstr = "二";
        } else if (strmonth.equals("03")) {
            newstr = "三";
        } else if (strmonth.equals("04")) {
            newstr = "四";
        } else if (strmonth.equals("05")) {
            newstr = "五";
        } else if (strmonth.equals("06")) {
            newstr = "六";
        } else if (strmonth.equals("07")) {
            newstr = "七";
        } else if (strmonth.equals("08")) {
            newstr = "八";
        } else if (strmonth.equals("09")) {
            newstr = "九";
        } else if (strmonth.equals("10")) {
            newstr = "十";
        } else if (strmonth.equals("11")) {
            newstr = "十一";
        } else if (strmonth.equals("12")) {
            newstr = "十二";
        }
        return newstr;
    }


    /**
     * 获取Imei信息
     *
     * @param context
     * @return
     */
    public static String getImei(Context context) {
        TelephonyManager TelephonyMgr = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
        String szImei = TelephonyMgr.getDeviceId();
        return szImei;
    }


    /**
     * /2017-02-26 17:57:55.0 截取解析
     *
     * @return
     */
    public static String splitDate(String datestr) {
//2017-02-26 17:57:55.0
        String strnewdate = "";
        if (!TextUtils.isEmpty(datestr) && datestr.length() > 16) {
            String datestr1 = datestr.substring(0, 10);
            String datestr2 = datestr.substring(11, 16);
            try {
                if (IsToday(datestr1)) {
                    strnewdate = datestr2;
                } else {
                    if (IsYesterday(datestr1)) {
                        strnewdate = datestr2 + " 昨天";
                    } else {
                        strnewdate = datestr2 + " " + datestr.substring(5, 10);
                    }

                }

            } catch (Exception e) {

            }

        }
        return strnewdate;
    }

    /**
     * /2017-02-26 17:57:55.0 截取解析
     *
     * @return
     */
    public static String splitRealNewsDate(String datestr) {
        String strnewdate = "";
        if (datestr.length() > 16) {
            String datestr1 = datestr.substring(0, 10);
            String datestr2 = datestr.substring(11, 16);
            try {
                if (IsToday(datestr1)) {
                    strnewdate = datestr2;
                } else {
                    if (IsYesterday(datestr1)) {
                        strnewdate = "昨天 " + datestr2;
                    } else {
                        strnewdate = datestr.substring(5, 10) + " " + datestr2;
                    }

                }

            } catch (Exception e) {

            }

        }
        return strnewdate;
    }

    /**
     * 判断是否为今天(效率比较高)
     *
     * @param day 传入的 时间  "2016-06-28 10:10:30" "2016-06-28" 都可以
     * @return true今天 false不是
     * @throws ParseException
     */
    public static boolean IsToday(String day) throws ParseException {

        Calendar pre = Calendar.getInstance();
        Date predate = new Date(System.currentTimeMillis());
        pre.setTime(predate);

        Calendar cal = Calendar.getInstance();
        Date date = getDateFormat().parse(day);
        cal.setTime(date);

        if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
            int diffDay = cal.get(Calendar.DAY_OF_YEAR)
                    - pre.get(Calendar.DAY_OF_YEAR);

            if (diffDay == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否为昨天(效率比较高)
     *
     * @param day 传入的 时间  "2016-06-28 10:10:30" "2016-06-28" 都可以
     * @return true今天 false不是
     * @throws ParseException
     */
    public static boolean IsYesterday(String day) throws ParseException {

        Calendar pre = Calendar.getInstance();
        Date predate = new Date(System.currentTimeMillis());
        pre.setTime(predate);

        Calendar cal = Calendar.getInstance();
        Date date = getDateFormat().parse(day);
        cal.setTime(date);

        if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
            int diffDay = cal.get(Calendar.DAY_OF_YEAR)
                    - pre.get(Calendar.DAY_OF_YEAR);

            if (diffDay == -1) {
                return true;
            }
        }
        return false;
    }

    public static SimpleDateFormat getDateFormat() {
        if (null == DateLocal.get()) {
            DateLocal.set(new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA));
        }
        return DateLocal.get();
    }

    private static ThreadLocal<SimpleDateFormat> DateLocal = new ThreadLocal<SimpleDateFormat>();


    /**
     * 获得设备识别认证码
     *
     * @return
     */
    public static String getIMEI(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
        if (tm == null) {
            return null;
        }
        return tm.getDeviceId();
    }

    private static String preMsg = "";


    private static void sendBroadCastLoginOut(Context activity) {
        Intent intent = new Intent("com.duobaodaka.broadcast.login.out");
        Bundle b = new Bundle();
        b.putString("loginout", "true");
        intent.putExtras(b);
        if (activity != null) {
            activity.sendBroadcast(intent);
        }
    }


    /**
     * 判断sd卡是否安装
     *
     * @return
     */
    public static boolean existSdcard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 获得屏幕密度
     *
     * @param
     * @return
     */
    public static float getDensity(Context context) {
        DisplayMetrics dm = context.getApplicationContext().getResources().getDisplayMetrics();
        return dm.density;
    }

    /**
     * 获得屏幕参数
     *
     * @param
     * @return
     */
    public static DisplayMetrics getDisplayMetrics(Context context) {
        DisplayMetrics dm = context.getApplicationContext().getResources().getDisplayMetrics();
        return dm;
    }

    /**
     * 像素转换成屏幕密度
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 屏幕密度转换成像素;
     *
     * @param context
     * @param dipValue
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 屏幕密度转换成像素;
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 获取透明度白色
     *
     * @param alpha
     * @return
     */
    public static int getWhiteAlpha(int alpha) {
        return Color.argb(alpha, 255, 255, 255);
    }


    /**
     * 获取手机ip地址
     *
     * @return
     */
    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            return null;
        }
        return null;
    }

    /**
     * 获取手机号码
     *
     * @return
     */
    public static String getPhoneNum(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
        if (tm == null) {
            return "";
        }
        return tm.getLine1Number();
    }

    /**
     * 设置View高
     *
     * @param view
     * @param
     * @param height
     */
    public static void resetViewHeight(View view, int height) {
        LayoutParams lp = view.getLayoutParams();
        lp.height = height;
        view.setLayoutParams(lp);
    }

    /**
     * 设置View高宽
     *
     * @param view
     * @param width
     * @param height
     */
    public static void resetViewSize(View view, int width,
                                     int height) {
        LayoutParams lp = view.getLayoutParams();
        lp.width = width;
        lp.height = height;
        view.setLayoutParams(lp);
    }


    /**
     * 隐藏软键盘
     */
    public static void hideInputKeyboard(Activity instance) {
        InputMethodManager imm = (InputMethodManager) instance.getSystemService(Context.INPUT_METHOD_SERVICE);

        if (imm.isActive()) {
            View focus = instance.getCurrentFocus();
            if (focus != null) {
                IBinder ibinder = focus.getWindowToken();
                if (ibinder != null) {
                    imm.hideSoftInputFromWindow(ibinder, InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }
    }

    /**
     * 获取屏幕宽度.
     *
     * @param context 上下文
     * @return 返回屏幕宽度  int类型
     */
    public static int getDeviceWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取屏幕高度.
     *
     * @param context 上下文
     * @return 返回屏幕高度  int类型
     */
    public static int getDeviceHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static File getStorage(String fileName) {
        if (existSdcard()) {
            return new File(Environment.getExternalStorageDirectory().toString() + File.separator + fileName);
        }
        return null;
    }

    public static String makeNum(String s) {
        String b = s;
        s = b.replace(",", "");
        return s;
    }

    /**
     * 获取Ip地址
     *
     * @param context
     * @return
     */
    public static String getIPAddress(Context context) {
        NetworkInfo info = ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {//当前使用2G/3G/4G网络
                try {
                    //Enumeration<NetworkInterface> en=NetworkInterface.getNetworkInterfaces();
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }

            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {//当前使用无线网络
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());//得到IPV4地址
                return ipAddress;
            }
        } else {
            //当前无网络连接,请在设置中打开网络
        }
        return null;
    }

    /**
     * 将得到的int类型的IP转换为String类型
     *
     * @param ip
     * @return
     */
    public static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }

    /**
     * EditText获取焦点并显示软键盘
     */
    public static void showSoftInputFromWindow(Activity activity, EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }




    /**
     * 获取Umeng渠道号
     *
     * @param context
     * @return 渠道号
     */
    public static String getChannel(Context context) {
        String channel = "official";
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if (info != null && info.metaData != null) {
                String metaData="";
                try {
                    metaData = info.metaData.getString("UMENG_CHANNEL");
                }catch (Exception e){
                    metaData="";
                }

                if (!TextUtils.isEmpty(metaData)) {
                    channel = metaData;
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return channel;
    }



    private static void writeInstallationFile(File installation)
            throws IOException {
        FileOutputStream out = new FileOutputStream(installation);
        String id = UUID.randomUUID().toString();
        out.write(id.getBytes());
        out.close();
    }

    private static String readInstallationFile(File installation)
            throws IOException {
        RandomAccessFile f = new RandomAccessFile(installation, "r");
        byte[] bytes = new byte[(int) f.length()];
        f.readFully(bytes);
        f.close();
        return new String(bytes);
    }

    /**
     * @return MAC or empty
     */
    public static String getMacAddress(Context con) {
        try {
            WifiManager mWifiManager = (WifiManager) con.getSystemService(Context.WIFI_SERVICE);
            if (mWifiManager != null) {
                WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
                if (wifiInfo != null) {
                    return wifiInfo.getMacAddress();
                }
            }
        } catch (Exception e) {
        }
        return "";
    }

    public static void startKF(Context context){
        if( App.info==null){
            App.initKFInfo();
        }
        /**

         * @param context 上下文对象

         * @param information 初始化参数

         */
        SobotApi.setNotificationFlag(context,true, R.mipmap.ic_logos,R.mipmap.ic_logos);
        SobotApi.startSobotChat(context, App.info);
    }

    public static void exitKF(Context context){
        SobotApi.exitSobotChat(context);
    }

    /**
     * 获取是否需要account登录
     */
    public static void putAccountMineLogin(boolean isLogin){
        SpUtil.putBoolean("SAVE_ACCOUNT_LOGIN",isLogin);
    }

    /**
     * 获取是否需要account登录
     */
    public static boolean getAccountMineLogin(){
       return SpUtil.getBoolean("SAVE_ACCOUNT_LOGIN",false);
    }
}
