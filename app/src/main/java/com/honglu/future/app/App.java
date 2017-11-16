package com.honglu.future.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.honglu.future.BuildConfig;
import com.honglu.future.R;
import com.honglu.future.config.ConfigUtil;
import com.honglu.future.config.Constant;
import com.honglu.future.config.KeyConfig;
import com.honglu.future.events.BaseEvent;
import com.honglu.future.events.EventController;
import com.honglu.future.events.LoginEvent;
import com.honglu.future.events.LogoutEvent;
import com.honglu.future.mpush.MPush;
import com.honglu.future.mpush.MyLog;
import com.honglu.future.ui.main.activity.MainActivity;
import com.honglu.future.ui.register.activity.RegisterActivity;
import com.honglu.future.util.LogUtils;
import com.honglu.future.util.SpUtil;
import com.honglu.future.util.StringUtil;
import com.honglu.future.util.ToastUtil;
import com.honglu.future.util.ViewUtil;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.PlatformConfig;
import com.xulu.mpush.client.ClientConfig;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import cn.jpush.android.api.JPushInterface;

import static com.tencent.bugly.beta.tinker.TinkerManager.getApplication;

/**
 * Created by zq on 2017/10/24.
 */

public class App extends Application implements Application.ActivityLifecycleCallbacks {
    public static App mApp;
    public Activity mActivity;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //突破Dex文件方法数不能超过最大值65536的上限
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //注册自己的Activity的生命周期回调接口。
        registerActivityLifecycleCallbacks(this);
        mApp = this;
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        //log初始化,根据configUtil的isDebug参数控制是否显示log
        LogUtils.logInit(getConfig().isDebug());
        //toast初始化
        ToastUtil.register(getContext());
        //注册eventbus
        EventBus.getDefault().register(this);
        //友盟相关功能初始化(统计与分享)
        initUM();
        initARouter();
        //bugly相关功能初始化(版本更新、bug检测)
        initBugly();
        initLoadingView(getContext());
       /* XGPushConfig.setAccessId(getContext(), KeyConfig.XG_ACCESS_ID);
        XGPushConfig.setAccessKey(getContext(), KeyConfig.XG_ACCESS_KEY);*/
    }

    /**
     * 开始请求
     *
     * @param userId 用户的userId
     */
    public void startPush(String userId) {
        //公钥有服务端提供和私钥对应
        ClientConfig cc = ClientConfig.build()
                .setAllotServer(ConfigUtil.baseUrl)
                .setOsName("android")
                .setClientVersion(BuildConfig.VERSION_NAME)
                .setLogger(new MyLog())
                .setUserId(userId)
                .setDeviceId(ViewUtil.getDeviceId(this))
                .setLogEnabled(BuildConfig.DEBUG);
        MPush.I.checkInit(getApplicationContext()).setClientConfig(cc);//配置
        MPush.I.startPush();
    }

    public void initBugly() {
        // 获取当前包名
        String packageName = getContext().getPackageName();
        // 获取当前进程名
        String processName = getProcessName(android.os.Process.myPid());
        // 设置是否为上报进程
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(getContext());
        //解决多进程多次上报问题
        strategy.setUploadProcess(processName == null || processName.equals(packageName));
        //设置渠道信息
        strategy.setAppChannel(getConfig().getChannelName());

        //设置版本更新相关
        //设置更新dialog只在MainActivity.class自动弹出
        Beta.canShowUpgradeActs.add(MainActivity.class);
        //设置弹出延时默认3000毫秒
        Beta.initDelay = 0;
        //设置更新dialog样式
        Beta.upgradeDialogLayoutId = R.layout.dialog_update;
        // 初始化Bugly
        Bugly.init(getContext(), KeyConfig.BUGLY_KEY, getConfig().isDebug(), strategy);

    }

    public void initUM() {
        /*获取market*/
        //String channel = WalleChannelReader.getChannel(getContext(), getConfig().getChannelName());
        String channel = ConfigUtil.getMarketId(getContext());
        LogUtils.loge("当前渠道:" + channel);
        /*注册UMENG*/
        MobclickAgent.startWithConfigure(new MobclickAgent.UMAnalyticsConfig(getContext(), KeyConfig.UM_KEY, channel));
        getConfig().setChannelName(channel);

        //关闭默认统计
        MobclickAgent.openActivityDurationTrack(false);
        MobclickAgent.setDebugMode(getConfig().isDebug());
       /* Config.DEBUG = true;
        UMShareAPI.get(this);*/
        //微信
        PlatformConfig.setWeixin(KeyConfig.WX_APP_KEY, KeyConfig.WX_APP_SECRET);
        //QQ
        PlatformConfig.setQQZone(KeyConfig.QQ_APP_ID, KeyConfig.QQ_APP_KEY);
    }

    public void initARouter(){
        if (BuildConfig.DEBUG){// 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(this); // 尽可能早，推荐在Application中初始化
    }

    public static void initLoadingView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_loan_loading, null);
        ViewUtil.setLoadingView(context, view);
    }

    public static void loadingDefault(Activity activity) {
        ViewUtil.setLoadingView(activity, null);
        ViewUtil.showLoading(activity, "");
    }

    public static void loadingContent(Activity activity, String content) {
        ViewUtil.setLoadingView(activity, null);
        ViewUtil.showLoading(activity, content);
    }

    public static void hideLoading() {
        ViewUtil.hideLoading();
    }


    public static String getAPPName() {
        return getContext().getResources().getString(R.string.app_name);
    }

    /*******
     * 将事件交给事件派发controller处理
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(BaseEvent event) {
        event.setApplicationContext(getContext());
        if (event instanceof LoginEvent) {//登录
            MPush.I.bindAccount(SpUtil.getString(Constant.CACHE_TAG_UID), "user");
        }
        if (event instanceof LogoutEvent) {//登出
            MPush.I.unbindAccount();
        }
        EventController.getInstance().handleMessage(event,this);
    }


    //保存一些常用的配置
    private static ConfigUtil configUtil = null;

    public static ConfigUtil getConfig() {
        if (configUtil == null) {
            configUtil = new ConfigUtil();
        }
        return configUtil;
    }

    @Override
    public void onTerminate() {
        //注销这个接口。
        unregisterActivityLifecycleCallbacks(this);
        super.onTerminate();
    }

    public static void toLogin(Context context) {
        String uName = SpUtil.getString(Constant.CACHE_TAG_USERNAME);
        if (!TextUtils.isEmpty(uName) && StringUtil.isMobileNO(uName)) {
            Intent intent = new Intent(context, RegisterActivity.class);
            intent.putExtra("phone", uName);
            context.startActivity(intent);
            /*Intent intent = new Intent(context, LoginActivity.class);
            intent.putExtra("tag", StringUtil.changeMobile(uName));
            intent.putExtra("phone", uName);
            context.startActivity(intent);*/
        } else {
            Intent intent = new Intent(context, RegisterActivity.class);
            context.startActivity(intent);
        }
        return;
    }

    public static Context getContext() {
        return mApp.getApplicationContext();
    }

    public void registerActivityLifecycleCallback(Application.ActivityLifecycleCallbacks callbacks) {
        getApplication().registerActivityLifecycleCallbacks(callbacks);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dip2px(float dpValue) {
        final float scale = this.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    public static boolean isBack = false;
    private int activityNum = 0;

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityStarted(Activity activity) {
        mActivity = activity;
        Log.d("XXX", "START");
        ++activityNum;
        isBack = false;
        startPush(SpUtil.getString(Constant.CACHE_TAG_UID));
    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {
        --activityNum;
        if (activityNum <= 0) {
            isBack = true;
            MPush.I.stopPush();
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }


    //当前渠道下的当前版本是否通过审核
    public boolean mIsAudited = true;

    public void setAudited(boolean isAudited) {
        this.mIsAudited = isAudited;
    }

    public boolean getAudited() {
        return mIsAudited;
    }
}
