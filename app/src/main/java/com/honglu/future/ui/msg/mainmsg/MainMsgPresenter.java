package com.honglu.future.ui.msg.mainmsg;

import com.honglu.future.base.BasePresenter;
import com.honglu.future.http.HttpManager;
import com.honglu.future.http.HttpSubscriber;
import com.honglu.future.ui.msg.bean.HasUnreadMsgBean;

/**
 * Created by zhuaibing on 2018/1/2
 */

public class MainMsgPresenter extends BasePresenter<MainMsgContract.View> implements MainMsgContract.Presenter{


    @Override
    public void getHasUnreadMsg(String userId) {
        toSubscribe(HttpManager.getApi().getHasUnreadMsg(userId), new HttpSubscriber<HasUnreadMsgBean>() {

            @Override
            protected void _onStart() {
                mView.showLoading("加载中....");
            }

            @Override
            protected void _onError(String message, int code) {
                mView.stopLoading();
                mView.showErrorMsg(message,null);
            }

            @Override
            protected void _onNext(HasUnreadMsgBean bean) {
                mView.stopLoading();
                mView.hasUnreadMsg(bean);
            }
        });
    }
}
