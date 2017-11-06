package com.honglu.future.ui.trade.presenter;

import com.honglu.future.base.BasePresenter;
import com.honglu.future.http.HttpManager;
import com.honglu.future.http.HttpSubscriber;
import com.honglu.future.ui.trade.bean.EntrustBean;
import com.honglu.future.ui.trade.bean.HoldPositionBean;
import com.honglu.future.ui.trade.contract.EntrustContract;

import java.util.List;

/**
 * Created by zq on 2017/10/26.
 */

public class EntrustPresenter extends BasePresenter<EntrustContract.View> implements EntrustContract.Presenter {
    @Override
    public void getEntrustList(String userId, String token) {
        toSubscribe(HttpManager.getApi().getEntrustList(userId, token), new HttpSubscriber<List<EntrustBean>>() {
            @Override
            public void _onStart() {
                mView.showLoading("查询委托中列表...");
            }

            @Override
            protected void _onNext(List<EntrustBean> list) {
                mView.getEntrustListSuccess(list);
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
    public void cancelOrder(String orderRef, String instrumentId, String sessionId, String frontId, String userId, String token) {
        toSubscribe(HttpManager.getApi().cancelOrder(orderRef, instrumentId, sessionId, frontId, userId, token), new HttpSubscriber() {
            @Override
            public void _onStart() {
                mView.showLoading("撤单中...");
            }

            @Override
            public void onNext(Object o) {
                mView.cancelOrderSuccess();
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
