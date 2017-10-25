package com.honglu.future.ui.login.contract;

import com.honglu.future.base.BaseView;
import com.honglu.future.ui.usercenter.bean.UserInfoBean;

/**
 * Created by zq on 2017/10/24.
 */

public interface LoginContract {
    interface View extends BaseView {
        void loginSuccess(UserInfoBean bean);
    }

    interface Presenter {
        void login(String mobileNum, String password);
    }
}
