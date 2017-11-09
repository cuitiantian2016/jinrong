package com.honglu.future.widget;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;

import com.honglu.future.R;
import com.honglu.future.util.SpUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import cn.jpush.android.api.JPushInterface;
import cn.udesk.UdeskConst;
import cn.udesk.UdeskSDKManager;
import cn.udesk.activity.UdeskChatActivity;
import cn.udesk.config.UdeskConfig;
import cn.udesk.messagemanager.UdeskMessageManager;
import cn.udesk.model.MsgNotice;


/**
 * Created by AYM on 2017/1/11.
 */

public class UdeskHelper {


    private static final String UDESK_DOMAIN = "xnsd.udesk.cn";//'"13121380325.udesk.cn";

    private static final String UDESK_SECRET_KAY = "3fdd0b5dec9c75ef528ad84a1a23860b";//;"a3962c701541045eb901081c82bd5b8d"

    private static final String UDESK_APPID = "91e92330d36d83a0";//;"a3962c701541045eb901081c82bd5b8d"

    public static void init(Context context) {
        //Udesk相关初始化
        UdeskSDKManager.getInstance().initApiKey(context.getApplicationContext(), UDESK_DOMAIN, UDESK_SECRET_KAY, UDESK_APPID);

        UdeskMessageManager.getInstance().event_OnNewMsgNotice.bind(new NewMsgNotice(context), "OnNewMsgNotice");

//        UdeskConfig.udeskTitlebarBgResId = R.color.white;//标题背景色
//
//        UdeskConfig.udeskTitlebarTextLeftRightResId = R.color.black;//左侧标题文字颜色
//
//        UdeskConfig.udeskIMRightTextColorResId =  R.color.black;
//        UdeskConfig.udeskIMLeftTextColorResId =  R.color.black;

        UdeskConfig.udeskbackArrowIconResId = R.mipmap.iv_back_nomal;//返回按钮

        UdeskConfig.isUserSDkPush = true;
    }

    public static void goRobotOrConversation(Context context) {
        init(context);

        String sdkToken = loginInfo(context);
        UdeskSDKManager.getInstance().showRobotOrConversationByImGroup(context);

        String rid = UdeskSDKManager.getInstance().getRegisterId(context);
        if (!TextUtils.isEmpty(rid)) {
            UdeskSDKManager.getInstance().setSdkPushStatus(UDESK_DOMAIN, UDESK_SECRET_KAY, sdkToken, "on", rid, UDESK_APPID);
        }
    }

    @NonNull
    private static String loginInfo(Context context) {
        String sdkToken = JPushInterface.getRegistrationID(context);
        if (TextUtils.isEmpty(sdkToken)) {
            sdkToken = SpUtil.getString("_sdkToken");
            if (TextUtils.isEmpty(sdkToken)) {
                sdkToken = UUID.randomUUID().toString();
                SpUtil.putString("_sdkToken", sdkToken);
            }
        } else {
            UdeskSDKManager.getInstance().setRegisterId(context, sdkToken);
        }


        String name = SpUtil.getString("user_name");
        String phone = SpUtil.getString("user_mobile");

        sdkToken = phone + ":" + sdkToken;

        Map<String, String> info = new HashMap<String, String>();
        info.put(UdeskConst.UdeskUserInfo.USER_SDK_TOKEN, sdkToken);
        info.put(UdeskConst.UdeskUserInfo.NICK_NAME, name);
        info.put(UdeskConst.UdeskUserInfo.CELLPHONE, phone);
        UdeskSDKManager.getInstance().setUserInfo(
                context.getApplicationContext(), sdkToken, info);
        return sdkToken;
    }

    public static void disConnect() {
        UdeskSDKManager.getInstance().disConnectXmpp();
    }

    public static void onNewMsg(Context context, String title, String content) {
        if (context == null) return;
        if (TextUtils.isEmpty(title))
            title = context.getString(context.getApplicationInfo().labelRes);
        if (TextUtils.isEmpty(content)) return;

        if (UdeskMessageManager.getInstance().isConnected()) {
            return;
        } else {
            MsgNotice mn = new MsgNotice();
            mn.setMsgType("message");
            mn.setContent(content);

            loginInfo(context);

            new NewMsgNotice(context).OnNewMsgNotice(mn);
        }
    }

    public static class NewMsgNotice {

        Context context;

        public NewMsgNotice(Context context) {
            this.context = context.getApplicationContext();
        }

        public void OnNewMsgNotice(MsgNotice msgNotice) {
            if (msgNotice != null) {
                String message = msgNotice.getContent();
                String notify_serivice = Context.NOTIFICATION_SERVICE;
                NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(notify_serivice);
                int icon = context.getApplicationInfo().icon;
                CharSequence tickerText = "你有新消息了";
                long when = System.currentTimeMillis();
                CharSequence contentTitle = context.getString(context.getApplicationInfo().labelRes);
                CharSequence contentText = message;
                Intent notificationIntent = null;
                notificationIntent = new Intent(context, UdeskChatActivity.class);
                notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                        notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                builder.setSmallIcon(icon)
                        .setContentTitle(contentTitle)
                        .setContentText(contentText)
                        .setTicker(tickerText)
                        .setContentIntent(contentIntent)
                        .setWhen(when);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder.setFullScreenIntent(contentIntent, false);
                }
                Notification noti = builder.build();
                noti.flags = Notification.FLAG_AUTO_CANCEL;
                noti.defaults |= Notification.DEFAULT_VIBRATE;
                noti.defaults |= Notification.DEFAULT_LIGHTS;
                noti.defaults = Notification.DEFAULT_SOUND;
                mNotificationManager.notify(1, noti);
            }
        }
    }
}
