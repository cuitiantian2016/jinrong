package com.honglu.future.util;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.honglu.future.ui.usercenter.bean.UserInfoBean;

/**
 * Created by hefei on 2017/8/18.
 * <p>
 * 获取用户信息的一些工具类
 */

public class UserUtil {

    private static final String KEY_USER_CUSTOMER_ID = "user_customerId";
    private static final String KEY_USER_MOBILE = "user_mobile";
    private static final String KEY_USER_TOKEN = "user_token";
    private static final String KEY_USER_INFO = "user_info";

    public static void setCustomerId(String customerId) {
        SpUtil.putString(KEY_USER_CUSTOMER_ID, customerId);
    }

    public static String getCustomerId() {
        return SpUtil.getString(KEY_USER_CUSTOMER_ID);
    }

    public static void setUserMobile(String userMobile) {
        SpUtil.putString(KEY_USER_MOBILE, userMobile);
    }

    public static String getUserMobile() {
        return SpUtil.getString(KEY_USER_MOBILE);
    }

    public static void setUserToken(String userToken) {
        SpUtil.putString(KEY_USER_TOKEN, userToken);
    }

    public static String getUserToken() {
        return SpUtil.getString(KEY_USER_TOKEN);
    }

    public static UserInfoBean getUserInfo() {
        String jsonUserInfo = SpUtil.getString(KEY_USER_INFO);
        if (TextUtils.isEmpty(jsonUserInfo)) {
            return null;
        }
        return new Gson().fromJson(jsonUserInfo, UserInfoBean.class);
    }

    public static void setUserInfo(UserInfoBean userInfo) {
        if (userInfo == null) {
            return;
        }
        String jsonUserInfo = new Gson().toJson(userInfo);
        SpUtil.putString(KEY_USER_INFO, jsonUserInfo);
    }
}
