package com.honglu.future.ui.main.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.honglu.future.app.App;
import com.honglu.future.base.BasePresenter;
import com.honglu.future.config.ConfigUtil;
import com.honglu.future.config.Constant;
import com.honglu.future.http.HttpManager;
import com.honglu.future.http.HttpSubscriber;
import com.honglu.future.ui.circle.bean.TopicFilter;
import com.honglu.future.ui.main.bean.AuditedBean;
import com.honglu.future.ui.main.contract.SplashContract;
import com.honglu.future.util.ViewUtil;

import java.util.List;

/**
 * Created by zhuaibing on 2017/11/4
 */

public class SplashPresenter extends BasePresenter<SplashContract.View> implements SplashContract.Presenter{

    //获取该渠道是否过审
    @Override
    public void getAudited() {
        //app类型（0:android，1:ios，2:windows）
        String channel = ConfigUtil.getMarketId(App.getContext());
        toSubscribe(HttpManager.getApi().getAudited(0, channel, ViewUtil.getAppVersion(App.getContext())), new HttpSubscriber<AuditedBean>() {
            @Override
            protected void _onError(String message, int code) {
                if (App.mApp !=null){
                    App.mApp.setAudited(true);
                }
            }

            @Override
            protected void _onNext(AuditedBean bean) {
                //IsAudit 1过审  0 没过审
                if (bean !=null && !TextUtils.isEmpty(bean.getIsAudit()) && bean.getIsAudit().equals("0")){
                    if (App.mApp !=null){
                        App.mApp.setAudited(false);
                    }
                }else {
                    if (App.mApp !=null){
                        App.mApp.setAudited(true);
                    }
                }
            }
        });
    }

    @Override
    public void getTopicFilter() {
        toSubscribe(HttpManager.getApi().getTopicFilter(), new HttpSubscriber<List<TopicFilter>>() {
            @Override
            protected void _onError(String message, int code) {
            }
            @Override
            protected void _onNext(List<TopicFilter> bean) {
                Constant.topic_filter = bean;
            }
        });
    }
}
