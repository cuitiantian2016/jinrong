package com.honglu.future.ui.login.contract;

import com.honglu.future.base.BaseView;

/**
 * Created by zq on 2017/10/24.
 */

public interface ResetPwdContract {
    interface View extends BaseView {
        void resetPwdSuccess();
    }

    interface Presenter {
        void getResetCode(String sourceId, String mobileNum);

        void resetPwd(String mobileNum, String password, String code);
    }
}
