package com.honglu.future.ui.main.presenter;


import com.honglu.future.base.BasePresenter;
import com.honglu.future.bean.UpdateBean;
import com.honglu.future.http.HttpManager;
import com.honglu.future.http.HttpSubscriber;
import com.honglu.future.ui.main.bean.ActivityBean;
import com.honglu.future.ui.main.contract.ActivityContract;
import com.honglu.future.ui.usercenter.bean.AccountInfoBean;
import com.honglu.future.util.AppUtils;

/**
 * Created by xiejingwen on 2017/3/31 0031.
 */

public class ActivityPresenter extends BasePresenter<ActivityContract.View> implements ActivityContract.Presenter {
    @Override
    public void loadActivity() {

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
                    /*原版本
                    //有新版本
                    if ("1".equals(data.getPopup())) {
                        showUpdate(response);
                    } else {
                        mView.showToast("已经是最新版本了");
                    }*/
                boolean isForced = false;
                if ("1".equals(bean.getChangeProperties())) {
                    isForced = true;
                }
                if ("1".equals(bean.getPopup())) {
                    String title = "当前版本:" + bean.getOldVersionNumber() + ",最新版本:" + bean.getVersionNumber();
                    ezy.boost.update.UpdateHelper.getInstance().update(
                            mContext,
                            isForced,
                            title,
                            bean.getChangeDesc(),
                            Integer.valueOf(bean.getAndVersion()),
                            bean.getVersionNumber(),
                            bean.getDownloadUrl(),
                            bean.getMd5());
                } else {
                    //mView.showLxLogin();
                }
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
