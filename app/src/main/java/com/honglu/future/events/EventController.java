package com.honglu.future.events;


import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.text.TextUtils;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.honglu.future.app.App;
import com.honglu.future.app.JPushManager;
import com.honglu.future.bean.AppInfo;
import com.honglu.future.config.Constant;
import com.honglu.future.ui.login.activity.LoginActivity;
import com.honglu.future.ui.login.contract.LoginOutContract;
import com.honglu.future.ui.login.presenter.LoginOutPresenter;
import com.honglu.future.ui.main.activity.MainActivity;
import com.honglu.future.ui.register.activity.RegisterActivity;
import com.honglu.future.ui.usercenter.bean.UserInfoBean;
import com.honglu.future.util.ConvertUtil;
import com.honglu.future.util.SpUtil;
import com.honglu.future.util.StringUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;



public class EventController {

    private static volatile EventController instance = null;
    private Context mContext;

    private EventController() {

    }

    /******
     * 获取单例
     *
     * @return
     */
    public static EventController getInstance() {
        if (instance == null) {
            synchronized (EventController.class) {

                if (instance == null) {
                    instance = new EventController();
                }

            }
        }
        return instance;
    }


    /**********
     * eventBus 事件派发
     *
     * @param event
     */
    public void handleMessage(final BaseEvent event) {
        if (event.getUiEvent() == null) {
            //登录
            if (event instanceof LoginEvent) {
                //登陆成功后，更新极光推送的别名
                JPushManager.get().registerJPushAlias();
                saveUserInfo(((LoginEvent) event).getBean(), ((LoginEvent) event).getContext());
                if (((LoginEvent) event).isToNext()) {
                    EventBus.getDefault().post(new RefreshUIEvent(UIBaseEvent.EVENT_LOGIN));//是点哪个页面跳转的登录，登录后就跳转到点击的页面
                }
            } else if (event instanceof LogoutEvent)//退出
            {
                logOut((LogoutEvent) event);
            } else if (event instanceof LoginNoRefreshUIEvent) {//启动app时保存用户数据
                saveUserInfo(((LoginNoRefreshUIEvent) event).getBean(), ((LoginNoRefreshUIEvent) event).getContext());
            }
        }
    }


    /**********
     * 退出
     *
     * @param event
     */
    private void logOut(LogoutEvent event) {

        EventBus.getDefault().post(new RefreshUIEvent(UIBaseEvent.EVENT_LOGOUT));
        Intent intent = new Intent(event.getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        event.getContext().startActivity(intent);

        String uName = SpUtil.getString(Constant.CACHE_TAG_USERNAME);
//        Intent loginIntent = new Intent(App.getContext(), RegisterActivity.class);
//        intent.putExtra("phone", uName);
//        App.getContext().startActivity(intent);
       Intent loginIntent=new Intent(App.getContext(),LoginActivity.class);
        loginIntent.putExtra("tag", StringUtil.changeMobile(uName));
        loginIntent.putExtra("phone", uName);
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        App.getContext().startActivity(loginIntent);
        clearLoginStatus(App.getContext(), event.getTAG());
    }


    /************
     * 清除登录状态
     */
    public static void clearLoginStatus(Context context, int tag) {
        SpUtil.putString(Constant.CACHE_TAG_SESSIONID, "");
        SpUtil.putString(Constant.CACHE_TAG_UID, "");
        SpUtil.putString(Constant.CACHE_ACCOUNT_TOKEN, "");
        //SpUtil.putString(Constant.SHARE_TAG_USERNAME, "");
        App.getConfig().setUserInfo(null);
        //清除cookie
        CookieSyncManager.createInstance(context);
        CookieManager cm = CookieManager.getInstance();
        cm.removeAllCookie();
        CookieSyncManager.getInstance().sync();
        if (tag == 0) {
            LoginOutPresenter loginOutPresenter = new LoginOutPresenter();
            loginOutPresenter.init(new LoginOutContract.View() {
                @Override
                public void loginOutSuccess() {
                }

                @Override
                public void showLoading(String content) {
                }

                @Override
                public void stopLoading() {
                }

                @Override
                public void showErrorMsg(String msg, String type) {
                }
            });
            loginOutPresenter.loginOut();
        }
    }

    /********
     * 登录成功后保存用户信息
     *
     * @param userInfo
     */
    private void saveUserInfo(UserInfoBean userInfo, Context context) {
        if (userInfo != null) {
            SpUtil.putString(Constant.CACHE_TAG_MOBILE, userInfo.getMobileNum());
            SpUtil.putString(Constant.CACHE_TAG_USERNAME, userInfo.getNickName());
            SpUtil.putString(Constant.CACHE_TAG_UID, userInfo.getUserId() + "");
            SpUtil.putString(Constant.CACHE_TAG_SESSIONID, userInfo.getToken());
            SpUtil.putString(Constant.CACHE_USER_AVATAR, "user"+userInfo.getAvatar().split("user")[1]);
            App.getConfig().setUserInfo(userInfo);

            CookieSyncManager.createInstance(context);
            CookieManager cm = CookieManager.getInstance();
            String cookie = "SESSIONID=" + userInfo.getToken() + ";UID=" + userInfo.getUserId();
            cm.setCookie(App.getConfig().getBaseUrl(), cookie);
            CookieSyncManager.getInstance().sync();
            cacheUserInfo(userInfo, context);

            EventBus.getDefault().post(new FragmentRefreshEvent(UIBaseEvent.EVENT_LOGIN));
        }
    }

    /**
     * 获取手机安装的app列表
     *
     * @param context
     * @return
     */
    private List<AppInfo> getAppInfoList(Context context) {
        // 获取手机中所有已安装的应用，并判断是否系统应用
        ArrayList<AppInfo> appList = new ArrayList<>(); //用来存储获取的应用信息数据，手机上安装的应用数据都存在appList里
        List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(0);

        for (int i = 0; i < packages.size(); i++) {
            PackageInfo packageInfo = packages.get(i);
            //判断是否系统应用
            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                //非系统应用
                AppInfo tmpInfo = new AppInfo();
                tmpInfo.setAppName(packageInfo.applicationInfo.loadLabel(context.getPackageManager()).toString());
                tmpInfo.setPackageName(packageInfo.packageName);
                tmpInfo.setVersionCode(packageInfo.versionCode);
                tmpInfo.setVersionName(packageInfo.versionName);
                if (App.getConfig().getUserInfo() != null) {
                    tmpInfo.setUserId(App.getConfig().getUserInfo().getUserId());
                }
                appList.add(tmpInfo);
            } else {
                //系统应用　　　　　　　　
            }
        }
        return appList;
    }

    /**
     * 保存用户信息到本地缓存
     *
     * @param bean
     * @param context
     */
    private void cacheUserInfo(UserInfoBean bean, Context context) {
        if (TextUtils.isEmpty(SpUtil.getString(Constant.CACHE_TAG_MOBILE))) {
            SpUtil.putString(Constant.CACHE_TAG_MOBILE, bean.getMobileNum());
        }
        if (TextUtils.isEmpty(SpUtil.getString(Constant.CACHE_TAG_USERNAME))) {
            SpUtil.putString(Constant.CACHE_TAG_USERNAME, bean.getNickName());
        }
        String userListStr = SpUtil.getString(Constant.CACHE_USERLIST_KEY);
        List userList = null;
        if (StringUtil.isBlank(userListStr)) {
            userList = new ArrayList();
        } else {
            userList = ConvertUtil.StringToList(userListStr);
        }

        if (userList.indexOf(bean.getNickName()) < 0) {
            userList.add(bean.getNickName());
        }
        SpUtil.putString(Constant.CACHE_USERLIST_KEY, ConvertUtil.ListToString(userList));

    }
}
