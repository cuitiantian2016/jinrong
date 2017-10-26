package com.honglu.future.ui.usercenter.presenter;

import com.honglu.future.base.BasePresenter;
import com.honglu.future.config.Constant;
import com.honglu.future.http.HttpManager;
import com.honglu.future.http.HttpSubscriber;
import com.honglu.future.ui.usercenter.contract.ModifyUserContract;
import com.honglu.future.util.SpUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by zq on 2017/10/25.
 */

public class ModifyUserPresenter extends BasePresenter<ModifyUserContract.View> implements ModifyUserContract.Presenter {
    @Override
    public void updateNickName(String nickName, String userId) {
        toSubscribe(HttpManager.getApi().updateNickName(nickName, userId), new HttpSubscriber() {
            @Override
            public void _onStart() {
                mView.showLoading("正在修改昵称 ...");
            }

            @Override
            public void onNext(Object o) {
                mView.updateNickNameSuccess();
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

    @Override
    public void updateUserAvatar(String url) {
        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), SpUtil.getString(Constant.CACHE_TAG_UID));

        toSubscribe(HttpManager.getApi().uploadUserAvatar(putFile("file", url), userId), new HttpSubscriber() {
            @Override
            public void _onStart() {
                mView.showLoading("正在上传头像 ...");
            }

            @Override
            public void onNext(Object o) {
                mView.updateUserAvatarSuccess();
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
