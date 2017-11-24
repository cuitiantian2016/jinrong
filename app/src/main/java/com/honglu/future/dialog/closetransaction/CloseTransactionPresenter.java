package com.honglu.future.dialog.closetransaction;

import com.honglu.future.base.BasePresenter;
import com.honglu.future.http.HttpManager;
import com.honglu.future.http.HttpSubscriber;

/**
 * Created by zhuaibing on 2017/11/24
 */

public class CloseTransactionPresenter extends BasePresenter<CloseTransactionContract.View> implements CloseTransactionContract.Presenter{


    //委托平仓
    @Override
    public void closeOrder(String todayPosition, String userId, String token, String orderNumber, String type, String price, String instrumentId, String holdAvgPrice, String company) {
        toSubscribe(HttpManager.getApi().closeOrder(todayPosition, userId, token, orderNumber, type, price,
                instrumentId, holdAvgPrice, company), new HttpSubscriber() {
            @Override
            public void _onStart() {
                mView.showLoading("平仓中...");
            }

            @Override
            protected void _onNext(Object o) {
                mView.closeOrderSuccess();
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


    //快速平仓
    @Override
    public void ksCloseOrder(String todayPosition, String userId, String token, String orderNumber, String type, String price, String instrumentId, String holdAvgPrice, String company) {
        toSubscribe(HttpManager.getApi().ksCloseOrder(todayPosition, userId, token, orderNumber, type, price,
                instrumentId, holdAvgPrice, company), new HttpSubscriber() {
            @Override
            public void _onStart() {
                mView.showLoading("平仓中...");
            }

            @Override
            protected void _onNext(Object o) {
                mView.closeOrderSuccess();
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
