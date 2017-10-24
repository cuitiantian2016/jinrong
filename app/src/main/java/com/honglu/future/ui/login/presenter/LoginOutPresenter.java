package com.honglu.future.ui.login.presenter;


import com.honglu.future.base.BasePresenter;
import com.honglu.future.http.HttpManager;
import com.honglu.future.http.HttpSubscriber;
import com.honglu.future.ui.login.contract.LoginOutContract;

/**
 * Created by xiejingwen at 2017/2/15 0015
 */

public class LoginOutPresenter extends BasePresenter<LoginOutContract.View> implements LoginOutContract.Presenter {
    public final String TYPE_LOGIN_OUT = "loginOut";
    @Override
    public void loginOut() {

    }
}
