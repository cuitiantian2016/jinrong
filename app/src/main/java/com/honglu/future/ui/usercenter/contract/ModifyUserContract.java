package com.honglu.future.ui.usercenter.contract;

import com.honglu.future.base.BaseView;

/**
 * Created by zq on 2017/10/25.
 */

public interface ModifyUserContract {
    interface View extends BaseView {
        void updateNickNameSuccess();
        void updateUserAvatarSuccess();
    }
    interface Presenter{
        void updateNickName(String nickName, String userId);
        void updateUserAvatar(String url);
    }
}
