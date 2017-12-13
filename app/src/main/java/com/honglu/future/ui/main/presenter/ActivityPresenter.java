package com.honglu.future.ui.main.presenter;


import com.honglu.future.base.BasePresenter;
import com.honglu.future.bean.ActivityPopupBean;
import com.honglu.future.bean.UpdateBean;
import com.honglu.future.config.Constant;
import com.honglu.future.http.HttpManager;
import com.honglu.future.http.HttpSubscriber;
import com.honglu.future.ui.main.bean.ActivityBean;
import com.honglu.future.ui.main.contract.ActivityContract;
import com.honglu.future.ui.usercenter.bean.AccountInfoBean;
import com.honglu.future.util.AppUtils;
import com.honglu.future.util.SpUtil;

/**
 * Created by xiejingwen on 2017/3/31 0031.
 */

public class ActivityPresenter extends BasePresenter<ActivityContract.View> implements ActivityContract.Presenter {
    @Override
    public void loadActivity() {
        toSubscribe(HttpManager.getApi().loadAppPopupWin("0", AppUtils.getVersionName(), SpUtil.getString(Constant.CACHE_TAG_MOBILE)), new HttpSubscriber<ActivityPopupBean>() {
            @Override
            public void _onStart() {
            }

            @Override
            protected void _onNext(ActivityPopupBean bean) {
                if (bean == null) {
                    return;
                }
                mView.loadActivitySuccess(bean);
            }

            @Override
            protected void _onError (String message){
            }

            @Override
            protected void _onCompleted () {

            }
        });
    }

    @Override
    public void getUpdateVersion() {
        String appType = "0";  //android 0  ios 1
        String versionNumber = AppUtils.getVersionName();
        toSubscribe(HttpManager.getApi().getUpdateVersion(appType, versionNumber), new HttpSubscriber<UpdateBean>() {
            @Override
            public void _onStart() {
                mView.showLoading("获取版本信息中...");
            }

            @Override
            protected void _onNext(UpdateBean bean) {
                if (bean == null) {
                    return;
                }
                mView.getUpdateVersionSuccess(bean);
        }

        @Override
        protected void _onError (String message){
            mView.showErrorMsg(message, null);
        }

        @Override
        protected void _onCompleted () {
            mView.stopLoading();
        }
    });
}
}
