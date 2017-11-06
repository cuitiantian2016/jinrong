package com.honglu.future.ui.main.presenter;

import com.honglu.future.base.BasePresenter;
import com.honglu.future.config.Constant;
import com.honglu.future.http.HttpManager;
import com.honglu.future.http.HttpSubscriber;
import com.honglu.future.ui.main.contract.AccountContract;
import com.honglu.future.ui.trade.bean.AccountBean;
import com.honglu.future.util.AESUtils;
import com.honglu.future.util.SpUtil;

/**
 * Created by zq on 2017/11/3.
 */

public class AccountPresenter extends BasePresenter<AccountContract.View> implements AccountContract.Presenter{
    @Override
    public void login(final String account, String password, String userId, String company) {
        toSubscribe(HttpManager.getApi().loginAccount(account, AESUtils.encrypt(password), userId, company), new HttpSubscriber<AccountBean>() {
            @Override
            public void _onStart() {
                mView.showLoading("登录中...");
            }

            @Override
            protected void _onNext(AccountBean bean) {
                SpUtil.putString(Constant.CACHE_ACCOUNT_USER_NAME, account);
                SpUtil.putString(Constant.CACHE_ACCOUNT_TOKEN, bean.getToken());
                mView.loginSuccess(bean);
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