package com.honglu.future.ui.login.presenter;

import com.honglu.future.base.BasePresenter;
import com.honglu.future.http.HttpManager;
import com.honglu.future.http.HttpSubscriber;
import com.honglu.future.ui.login.contract.ResetPwdContract;
import com.honglu.future.util.AESUtils;

/**
 * Created by zq on 2017/10/24.
 */

public class ResetPwdPresenter extends BasePresenter<ResetPwdContract.View> implements ResetPwdContract.Presenter {
    @Override
    public void getResetCode(String sourceId, String mobileNum) {
        toSubscribe(HttpManager.getApi().getResetCode(sourceId, mobileNum), new HttpSubscriber() {
            @Override
            public void _onStart() {
                mView.showLoading("正在获取 ...");
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

    @Override
    public void resetPwd(String mobileNum, String password,String code) {
        toSubscribe(HttpManager.getApi().resetPwd(mobileNum, AESUtils.encrypt(password),code), new HttpSubscriber() {
            @Override
            public void _onStart() {
                mView.showLoading("正在重置密码 ...");
            }

            @Override
            public void onNext(Object o) {
                mView.resetPwdSuccess();
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
