package com.honglu.future.ui.trade.historybill;

import com.honglu.future.base.BasePresenter;
import com.honglu.future.http.HttpManager;
import com.honglu.future.http.HttpSubscriber;
import com.honglu.future.ui.trade.bean.SettlementInfoBean;

/**
 * Created by zq on 2017/11/3.
 */

public class HistoryBillPresenter extends BasePresenter<HistoryBillContract.View> implements HistoryBillContract.Presenter {
    @Override
    public void querySettlementInfoByDate(String userId, String token, String company, String day) {
        toSubscribe(HttpManager.getApi().querySettlementInfoByDate(userId, token, company, day), new HttpSubscriber<SettlementInfoBean>() {
            @Override
            public void _onStart() {
                //mView.showLoading("查询结算单中...");
            }

            @Override
            protected void _onNext(SettlementInfoBean bean) {
                mView.querySettlementSuccess(bean);
            }

            @Override
            protected void _onError(String message) {
               // mView.showErrorMsg(message, null);
            }

            @Override
            protected void _onCompleted() {
                //mView.stopLoading();
            }
        });
    }
}
