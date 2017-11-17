package com.honglu.future.ARouter.Interceptor;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Interceptor;
import com.alibaba.android.arouter.facade.callback.InterceptorCallback;
import com.alibaba.android.arouter.facade.template.IInterceptor;
import com.alibaba.android.arouter.launcher.ARouter;
import com.honglu.future.app.App;
import com.honglu.future.config.Constant;
import com.honglu.future.events.LogoutEvent;
import com.honglu.future.util.SpUtil;

import org.greenrobot.eventbus.EventBus;

/**
 * TODO feature
 *
 * @author hefei
 * 路由跳转的拦截器。
 */
@Interceptor(priority = 1000)
public class ARouterInterceptor implements IInterceptor {
    Context mContext;

    /**
     * The operation of this interceptor.
     *
     * @param postcard meta
     * @param callback cb
     */
    @Override
    public void process(final Postcard postcard, final InterceptorCallback callback) {
        callback.onContinue(postcard);
    }

    /**
     * Do your init work in this method, it well be call when processor has been load.
     *
     * @param context ctx
     */
    @Override
    public void init(Context context) {
        mContext = context;
    }
}
