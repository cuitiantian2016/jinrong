package com.honglu.future.ARouter;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.callback.NavCallback;
import com.alibaba.android.arouter.launcher.ARouter;
import com.honglu.future.R;
import com.honglu.future.app.App;
import com.honglu.future.config.ConfigUtil;
import com.honglu.future.events.LogoutEvent;
import com.honglu.future.util.SpUtil;
import com.honglu.future.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by hefei on 2017/10/26.
 * 测试页面
 */

public class DebugActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acticity_debug);
    }
    public void goToH5(View view) {
        ARouter.getInstance()
                .build("/future/webview")
                .withString("url", "file:///android_asset/schame-test.html")
                .navigation(this);
    }
    public void goToHome(View view) {
        ARouter.getInstance()
                .build("/future/main")
                .withInt("select",0)
                .navigation(this);
    }
    public void goToMarket(View view) {
        ARouter.getInstance()
                .build("/future/main")
                .withInt("select",1)
                .navigation(this);
    }
    public void goToTrade(View view) {
        ARouter.getInstance()
                .build("/future/main")
                .withInt("select",2)
                .navigation(this);
    }
    public void goToMe(View view) {
        ARouter.getInstance()
                .build("/future/main")
                .withInt("select",3)
                .navigation(this);
    }
    public void goToReChange(View view) {
        String uri = "xn://future/trade/pay?isLogin=true";
        ARouter.getInstance()
                .build(Uri.parse(uri))
                .navigation(this);
    }
    public void goToCircleDetil(View view) {
        String uri = "xiaoniuqihuo://future/circle/Detail?isLogin=true&circleReplyId=nihao";
        ARouter.getInstance()
                .build(Uri.parse(uri))
                .navigation(this);
    }
    public void change126(View view) {
        if (ConfigUtil.baseUrl.equals(ConfigUtil.URL_126)){
            ToastUtil.show("当前环境已经是正式环境");
            return;
        }
        SpUtil.putString(ConfigUtil.KEY_URL,ConfigUtil.URL_126);
        EventBus.getDefault().post(new LogoutEvent(App.getContext(),0));
        EventBus.getDefault().post(new LogoutEvent(App.getContext(),1));
        Runtime.getRuntime().exit(0);
    }
    public void goToHttp(View view) {
        if (ConfigUtil.baseUrl.equals(ConfigUtil.URL_126_http)){
            ToastUtil.show("当前环境已经是正式环境http");
            return;
        }
        SpUtil.putString(ConfigUtil.KEY_URL,ConfigUtil.URL_126_http);
        EventBus.getDefault().post(new LogoutEvent(App.getContext(),0));
        EventBus.getDefault().post(new LogoutEvent(App.getContext(),1));
        Runtime.getRuntime().exit(0);
    }
    public void changeTest126(View view) {
        if (ConfigUtil.baseUrl.equals(ConfigUtil.URL_test_126)){
            ToastUtil.show("当前环境已经是126");
            return;
        }
        SpUtil.putString(ConfigUtil.KEY_URL,ConfigUtil.URL_test_126);
        EventBus.getDefault().post(new LogoutEvent(App.getContext(),0));
        EventBus.getDefault().post(new LogoutEvent(App.getContext(),1));
        Runtime.getRuntime().exit(0);
    }
    public void change85(View view) {
        if (ConfigUtil.baseUrl.equals(ConfigUtil.URL_85)){
            ToastUtil.show("当前环境已经是85");
            return;
        }
        SpUtil.putString(ConfigUtil.KEY_URL,ConfigUtil.URL_85);
        EventBus.getDefault().post(new LogoutEvent(App.getContext(),0));
        EventBus.getDefault().post(new LogoutEvent(App.getContext(),1));
        Runtime.getRuntime().exit(0);
    }
}
