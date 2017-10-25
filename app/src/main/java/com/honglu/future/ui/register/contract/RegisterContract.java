package com.honglu.future.ui.register.contract;

import com.honglu.future.base.BaseView;
import com.honglu.future.ui.usercenter.bean.UserInfoBean;

/**
 * Created by zq on 2017/10/24.
 */

public interface RegisterContract {
    interface View extends BaseView {
        void registerSuccess(UserInfoBean bean);
    }

    interface Presenter {
        void register(String code, String sourceId, String mobileNum, String password, String nickName);
        void getCode(String sourceId, String mobileNum);
    }
}
