package com.honglu.future.ui.register.presenter;

import com.honglu.future.base.BasePresenter;
import com.honglu.future.http.HttpManager;
import com.honglu.future.http.HttpSubscriber;
import com.honglu.future.ui.register.bean.RegisterBean;
import com.honglu.future.ui.register.contract.RegisterContract;
import com.honglu.future.util.AESUtils;
import com.honglu.future.util.EncryptUtils;

/**
 * Created by zq on 2017/10/24.
 */

public class RegisterPresenter extends BasePresenter<RegisterContract.View> implements RegisterContract.Presenter {
    @Override
    public void register(String code, String sourceId, String mobileNum, String password, String nickName) {
        toSubscribe(HttpManager.getApi().register(code, sourceId, mobileNum, AESUtils.encrypt(password), nickName), new HttpSubscriber<RegisterBean>() {
            @Override
            public void _onStart() {
                mView.showLoading("注册中...");
            }

            @Override
            protected void _onNext(RegisterBean login) {
                mView.registerSuccess(login.getItem());
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
    public void getCode(String sourceId, String mobileNum) {
        toSubscribe(HttpManager.getApi().getCode(sourceId, mobileNum), new HttpSubscriber() {
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
}
