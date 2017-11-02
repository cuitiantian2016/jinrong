package com.honglu.future.ui.usercenter.contract;

import com.honglu.future.base.BaseView;

import java.io.File;

/**
 * Created by zq on 2017/10/25.
 */

public interface ModifyUserContract {
    interface View extends BaseView {
        void updateUserAvatarSuccess();
    }
    interface Presenter{
        void updateUserAvatar(String url);
    }
}
