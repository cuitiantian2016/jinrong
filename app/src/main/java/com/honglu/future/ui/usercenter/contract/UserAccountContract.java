package com.honglu.future.ui.usercenter.contract;

import com.honglu.future.base.BaseView;
import com.honglu.future.ui.usercenter.bean.AccountInfoBean;

/**
 * Created by zhuaibing on 2017/10/31
 */

public interface UserAccountContract {

    interface View extends BaseView {
        void getAccountInfoSuccess(AccountInfoBean bean);
        void accountLogoutSuccess();
    }

    interface Presenter {
        void getAccountInfo(String userId, String token, String company);
        void accountLogout(String userId, String token, String company);
    }
}
