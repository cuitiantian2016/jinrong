package com.honglu.future.ui.trade.billconfirm;

import com.honglu.future.base.BasePresenter;
import com.honglu.future.http.HttpManager;
import com.honglu.future.http.HttpSubscriber;
import com.honglu.future.ui.trade.bean.ConfirmBean;

/**
 * Created by zq on 2017/11/2.
 */

public class BillConfirmPresenter extends BasePresenter<BillConfirmContract.View> implements BillConfirmContract.Presenter{
    @Override
    public void settlementConfirm(String userId, String token) {
        toSubscribe(HttpManager.getApi().settlementConfirm(userId, token), new HttpSubscriber<ConfirmBean>() {
            @Override
            public void _onStart() {
                mView.showLoading("结算单确认中...");
            }

            @Override
            protected void _onNext(ConfirmBean bean) {
                mView.queryConfirmSuccess(bean);
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
