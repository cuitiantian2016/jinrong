package com.honglu.future.ui.usercenter.presenter;

import com.honglu.future.base.BasePresenter;
import com.honglu.future.http.HttpManager;
import com.honglu.future.http.HttpSubscriber;
import com.honglu.future.ui.usercenter.contract.ModifyNicknameContract;

/**
 * Created by zq on 2017/10/31.
 */

public class ModifyNicknamePresenter extends BasePresenter<ModifyNicknameContract.View> implements ModifyNicknameContract.Presenter{
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
}
