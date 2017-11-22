package com.honglu.future.ui.login.presenter;

import com.honglu.future.base.BasePresenter;
import com.honglu.future.config.Constant;
import com.honglu.future.http.HttpManager;
import com.honglu.future.http.HttpSubscriber;
import com.honglu.future.ui.login.contract.LoginContract;
import com.honglu.future.ui.usercenter.bean.UserInfoBean;
import com.honglu.future.util.AESUtils;
import com.honglu.future.util.SpUtil;

/**
 * Created by zq on 2017/10/24.
 */

public class LoginPresenter extends BasePresenter<LoginContract.View> implements LoginContract.Presenter {
    @Override
    public void login(String mobileNum, String password) {
        toSubscribe(HttpManager.getApi().login(mobileNum, AESUtils.encrypt(password)), new HttpSubscriber<UserInfoBean>() {
            @Override
            public void _onStart() {
                mView.showLoading("登录中...");
            }

            @Override
            protected void _onNext(UserInfoBean login) {
                SpUtil.putString(Constant.CACHE_TAG_UID, login.getUserId());
                mView.loginSuccess(login);
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
