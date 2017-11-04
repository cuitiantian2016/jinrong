package com.honglu.future.ui.usercenter.presenter;

import com.honglu.future.base.BasePresenter;
import com.honglu.future.http.HttpManager;
import com.honglu.future.http.HttpSubscriber;
import com.honglu.future.ui.trade.bean.AccountBean;
import com.honglu.future.ui.usercenter.bean.AccountInfoBean;
import com.honglu.future.ui.usercenter.contract.UserCenterContract;
import com.honglu.future.util.AESUtils;

/**
 * Created by zq on 2017/10/24.
 */

public class UserCenterPresenter extends BasePresenter<UserCenterContract.View> implements UserCenterContract.Presenter {

    @Override
    public void getAccountInfo(String userId, String token, String company) {
        toSubscribe(HttpManager.getApi().getAccountInfo(userId, token, company), new HttpSubscriber<AccountInfoBean>() {
            @Override
            public void _onStart() {
                mView.showLoading("获取中...");
            }

            @Override
            protected void _onNext(AccountInfoBean bean) {
                mView.getAccountInfoSuccess(bean);
            }

            @Override
            protected void _onError(String message) {
                mView.showErrorMsg(message, null);
            }

            @Override
            protected void _onCompleted() {
                mView.stopLoading();
            }
        });
    }
}
