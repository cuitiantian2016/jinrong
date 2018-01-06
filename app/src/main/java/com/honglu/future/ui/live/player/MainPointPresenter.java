package com.honglu.future.ui.live.player;

import com.honglu.future.base.BasePresenter;
import com.honglu.future.http.HttpManager;
import com.honglu.future.http.HttpSubscriber;
import com.honglu.future.ui.live.bean.LivePointBean;

import java.util.List;

/**
 * Created by zq on 2018/1/6.
 */

public class MainPointPresenter extends BasePresenter<MainPointContract.View> implements MainPointContract.Presenter {

    @Override
    public void getLivePointList(String tbLiveRoomId) {
        toSubscribe(HttpManager.getApi().getLivePointList(tbLiveRoomId), new HttpSubscriber<List<LivePointBean>>() {
            @Override
            public void _onStart() {
                mView.showLoading("");
            }

            @Override
            protected void _onNext(List<LivePointBean> list) {
                mView.getLivePointListSuccess(list);
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
