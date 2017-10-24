package com.honglu.future.ui.login.contract;


import com.honglu.future.base.BaseView;

/**
 * Created by Administrator on 2017/2/13 0013.
 */

public interface LoginOutContract {
    interface View extends BaseView {
        void loginOutSuccess();
    }
    interface Presenter{
        void loginOut();
    }
}
