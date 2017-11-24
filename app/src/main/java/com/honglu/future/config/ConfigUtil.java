package com.honglu.future.config;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.honglu.future.BuildConfig;
import com.honglu.future.ui.usercenter.bean.UserInfoBean;
import com.honglu.future.util.ConvertUtil;
import com.honglu.future.util.SpUtil;
import com.orhanobut.logger.Logger;

import retrofit2.http.PUT;

public class ConfigUtil {

    private boolean isDebug = BuildConfig.DEBUG;//是否调试模式,上线必须改为false
    public static final String KEY_URL = "KEY_URL";
    public static String URL_85 = "http://106.15.158.85:800/";
    public static String URL_85_AllotServer = "http://106.15.158.85:9999";
    public static String URL_126_test_AllotServer= "http://192.168.85.126:9999";
    public static String URL_126_AllotServer= "http://qihuo.xnsudai.com:9999";//外网
     public static String URL_test_126 = "http://192.168.85.126/";
    public static String URL_126_http = "http://qihuo.xnsudai.com/";//外网
    public static String URL_126 = "https://qihuo.xnsudai.com/";//外网
    //public static String baseUrl = "http://106.15.158.85:800/";
    public static String baseUrl = TextUtils.isEmpty(SpUtil.getString(KEY_URL))?URL_126:SpUtil.getString(KEY_URL);

    public static String baseImageUserUrl = "http://qihuo-product.oss-cn-shanghai.aliyuncs.com/";

    public static String baseH5Url = "https://qihuo.xnsudai.com/";
    /*public String baseUrl = "http://testqb.xnsudai8.com/";*/


    public String[] urls = {
            "http://192.168.1.145:81/",
            "http://testqb.xnsudai8.com/"
    };

    //新手入门
    public static String NEW_USER_TEACH = baseH5Url + "newSchool";

    //关于我们
    public static String ABOUT_US = baseH5Url + "aboutUs";
    //出入金规则
    public static String SCHOOLDETAILS = baseH5Url + "schoolDetails/14";

    //交易规则
    public static String TRADE_RULE = baseH5Url + "rule";

    //开户入口
    public static String OPEN_ACCOUNT_HOME = baseH5Url + "initAccount";

    //绑卡教程
    public static String BIND_CARD_TEACH = baseH5Url + "addCard";

    //期货术语
    public static String FUTURE_SHUYU = baseH5Url + "jargon/deal";

    //资金术语
    public static String ZIJIN_SHUYU = baseH5Url + "jargon/fund";

    //保证金监控中心查询的链接
    public static String QUERY_FUTURE = "https://investorservice.cfmmc.com";

    //用户协议
    public static String USER_PROTPCOL = baseH5Url + "protocol";


    //爬取支付宝数据js
    public String GET_ALIPAY_JS = baseUrl + "resources/js/alipay.js";

    //我的邀请码H5
    public String INVITATION_CODE = baseUrl + "page/detail";

    //活动中心H5
    public String ACTIVITY_CENTER = baseUrl + "content/activity";

    //帮助中心H5
    public String HELP = baseUrl + "help";

    //注册协议
    public String REGISTER_AGREEMENT = baseUrl + "act/light-loan-lyb/agreement";

    //信用授权协议
    public String CREDIT_AUTHORIZATION_AGREEMENT = baseUrl + "agreement/creditExtension";

    //数据使用授权书
    public String DATA_USE_AUTH_INFO = baseUrl + "agreement/shiYongShouQuan";

    //关于我们
//    public String ABOUT_US = baseUrl + "page/detailAbout";


    private boolean isLogin = false;//用户的登录状态

    private String channelName = "com.xulu.xnpacket-MySelf";//默认渠道号

    public ConfigUtil() {
        setUserInfo(getUserInfo());
    }

    public boolean isDebug() {
        return isDebug;
    }

    /*  public boolean isCeshi() {
          return TextUtils.equals("ceshi", BuildConfig.PRODUCT_FLAVOR);
      }
  */
    public void setDebug(boolean isDebug) {
        this.isDebug = isDebug;
    }


    public String getChannelName() {
        return channelName;
    }


    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public UserInfoBean getUserInfo() {
        return ConvertUtil.toObject(SpUtil.getString(Constant.CACHE_USER_INFO), UserInfoBean.class);
    }

    public static String getMarketId(Context context) {
        String appType = null;
        try {
            ApplicationInfo appInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            appType = String.valueOf(appInfo.metaData.get("UMENG_CHANNEL"));
            if (TextUtils.isEmpty(appType)) {
                appType = "unknown";
            }
        } catch (Exception e) {
            appType = "unknown";
            Logger.e(e.getMessage());
        }
        return appType;
    }


    public void setUserInfo(UserInfoBean userInfo) {
        isLogin = userInfo == null ? false : true;
        SpUtil.putString(Constant.CACHE_USER_INFO, ConvertUtil.toJsonString(userInfo));
    }

    //获取用户当前登录状态
    public boolean getLoginStatus() {
        return isLogin;
        //        return true;
    }

    //获取用户当前登录状态
    public boolean getAccountLoginStatus() {
        return !TextUtils.isEmpty(SpUtil.getString(Constant.CACHE_ACCOUNT_TOKEN));
    }

    public String getBaseUrl() {
        if (isDebug() && !TextUtils.isEmpty(SpUtil.getString(Constant.URL_KEY))) {
            baseUrl = SpUtil.getString(Constant.URL_KEY);
        }
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        GET_ALIPAY_JS = baseUrl + "resources/js/alipay.js";

        //我的邀请码H5
        INVITATION_CODE = baseUrl + "page/detail";

        //活动中心H5
        ACTIVITY_CENTER = baseUrl + "content/activity";

        //帮助中心H5
        HELP = baseUrl + "help";

        //注册协议
        REGISTER_AGREEMENT = baseUrl + "act/light-loan-lyb/agreement";

        //数据使用授权书
        DATA_USE_AUTH_INFO = baseUrl + "agreement/shiYongShouQuan";

        //关于我们
        ABOUT_US = baseUrl + "page/detailAbout";

        CREDIT_AUTHORIZATION_AGREEMENT = baseUrl + "agreement/creditExtension";
    }
}
