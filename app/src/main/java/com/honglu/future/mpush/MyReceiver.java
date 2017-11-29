package com.honglu.future.mpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.honglu.future.util.LogUtils;

public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "MyReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (MPushService.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG,"收到新的通知：");
        } else if (MPushService.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Notifications.I.clean(intent);
            Log.d(TAG,"通知被点击了：");
        } else if (MPushService.ACTION_KICK_USER.equals(intent.getAction())) {
            Log.d(TAG,"用户被踢下线了：");
        } else if (MPushService.ACTION_BIND_USER.equals(intent.getAction())) {
            Log.d(TAG,"绑定用户：");
        } else if (MPushService.ACTION_UNBIND_USER.equals(intent.getAction())) {
            Log.d(TAG, "解绑用户:"
                    + (intent.getBooleanExtra(MPushService.EXTRA_BIND_RET, false)
                    ? "成功"
                    : "失败"));
        } else if (MPushService.ACTION_CONNECTIVITY_CHANGE.equals(intent.getAction())) {
            Log.d(TAG, intent.getBooleanExtra(MPushService.EXTRA_CONNECT_STATE, false)
                    ? "MPUSH连接建立成功"
                    : "MPUSH连接断开");
        } else if (MPushService.ACTION_HANDSHAKE_OK.equals(intent.getAction())) {
            Log.d(TAG, "MPUSH握手成功, 心跳:" + intent.getIntExtra(MPushService.EXTRA_HEARTBEAT, 0));
            //MPushUtil.refershCode();
    }
    }
}
