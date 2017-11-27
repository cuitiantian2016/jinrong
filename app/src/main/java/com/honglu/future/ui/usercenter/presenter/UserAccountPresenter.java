package com.honglu.future.ui.usercenter.presenter;

import com.honglu.future.base.BasePresenter;
import com.honglu.future.config.Constant;
import com.honglu.future.http.HttpManager;
import com.honglu.future.http.HttpSubscriber;
import com.honglu.future.ui.usercenter.bean.AccountInfoBean;
import com.honglu.future.ui.usercenter.contract.UserAccountContract;
import com.honglu.future.util.SpUtil;

/**
 * Created by zhuaibing on 2017/10/31
 */

public class UserAccountPresenter extends BasePresenter<UserAccountContract.View> implements UserAccountContract.Presenter{
    @Override
    public void getAccountInfo(String userId, String token, String company) {
        toSubscribe(HttpManager.getApi().getAccountInfo(userId, token, company), new HttpSubscriber<AccountInfoBean>() {
            @Override
            public void _onStart() {
                //mView.showLoading("获取中...");
            }

            @Override
            protected void _onNext(AccountInfoBean bean) {
                SpUtil.putString(Constant.CACHE_USER_ASSES,bean.getWithdrawQuota() + "");
                SpUtil.putString(Constant.CACHE_USER_AVAILABLE_MONEY,bean.getAvailable() + "");
                mView.getAccountInfoSuccess(bean);
            }

            @Override
            protected void _onError(String message) {
                //mView.showErrorMsg(message, null);
            }

            @Override
            protected void _onCompleted() {
                //mView.stopLoading();
            }
        });
    }
}
