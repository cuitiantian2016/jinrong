package com.honglu.future.ui.usercenter.contract;

import com.honglu.future.base.BaseView;

/**
 * Created by zq on 2017/10/31.
 */

public interface ModifyNicknameContract {
    interface View extends BaseView {
        void updateNickNameSuccess();
    }
    interface Presenter{
        void updateNickName(String nickName, String userId);
    }
}
