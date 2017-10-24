package com.honglu.future.util;

import android.content.Context;
import android.text.TextUtils;


import com.honglu.future.app.App;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import cn.udesk.PreferenceHelper;
import cn.udesk.UdeskConst;
import cn.udesk.UdeskSDKManager;

/**
 * Created by hc on 2017/9/5.
 */

public class UDeskUtils {

    //替换成你们注册生成的域名
    private String UDESK_DOMAIN = "xnsd.udesk.cn";
    //替换成你们生成应用产生的appid
    private String AppId = "dab5d211dac5a8a6";
    // 替换成你们在后台生成的密钥
    private String UDESK_SECRETKEY = "6eeaab1cfdb16e8b4ca66774c7ad15d9";

  /*  private String UDESK_DOMAIN = "xnsdnrd.udesk.cn";
    //替换成你们生成应用产生的appid
    private String AppId = "6a424855941db2d1";
    // 替换成你们在后台生成的密钥
    private String UDESK_SECRETKEY = "08919a2194e9844795c8f589854ad559";*/

    private UDeskUtils(){}
    private static UDeskUtils updateUtils;
    public static UDeskUtils getInstance(){
        if (updateUtils == null)
            updateUtils = new UDeskUtils();
        return updateUtils;
    }

    public void init(Context context){
        UdeskSDKManager.getInstance().initApiKey(App.getContext(), UDESK_DOMAIN, UDESK_SECRETKEY,AppId);

        String sdkToken = PreferenceHelper.readString(App.getContext(), "init_base_name", "sdktoken");
        if (TextUtils.isEmpty(sdkToken)) {
            sdkToken = UUID.randomUUID().toString();
        }

        Map<String, String> info = new HashMap<String, String>();
        info.put(UdeskConst.UdeskUserInfo.USER_SDK_TOKEN, sdkToken);
        info.put(UdeskConst.UdeskUserInfo.NICK_NAME, sdkToken);

        UdeskSDKManager.getInstance().setUserInfo(App.getContext(), sdkToken, info);
        PreferenceHelper.write(context, "init_base_name", "domain", UDESK_DOMAIN);
        PreferenceHelper.write(context, "init_base_name", "appkey", UDESK_SECRETKEY);
        PreferenceHelper.write(context, "init_base_name", "appid", AppId);
        PreferenceHelper.write(App.getContext(),"init_base_name","sdktoken",  sdkToken);
    }

    public void entryChat(Context context){
        //咨询会话
        UdeskSDKManager.getInstance().entryChat(context);
    }
}
