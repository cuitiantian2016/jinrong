package com.honglu.future.ui.trade.presenter;

import com.honglu.future.base.BasePresenter;
import com.honglu.future.http.HttpManager;
import com.honglu.future.http.HttpSubscriber;
import com.honglu.future.ui.trade.bean.HistoryBuiderPositionBean;
import com.honglu.future.ui.trade.bean.HistoryClosePositionBean;
import com.honglu.future.ui.trade.bean.HistoryMissPositionBean;
import com.honglu.future.ui.trade.bean.HistoryTradeBean;
import com.honglu.future.ui.trade.contract.TradeRecordContract;
import com.honglu.future.util.ToastUtil;

import java.util.List;

/**
 * Created by zhuaibing on 2017/10/26
 */

public class TradeRecordPresenter extends BasePresenter<TradeRecordContract.View> implements TradeRecordContract.Presenter{

    @Override
    public void getHistoryTradeBean(String dayStart, String userId, String token, String dayEnd) {
        toSubscribe(HttpManager.getApi().getHistoryTradeBean(dayStart, userId, token, dayEnd), new HttpSubscriber<HistoryTradeBean>() {
            @Override
            protected void _onNext(HistoryTradeBean o) {
                super._onNext(o);
                mView.bindHistoryTradeBean(o);
            }

            @Override
            protected void _onError(String message) {
                super._onError(message);
                ToastUtil.show(message);
                mView.showErrorMsg(message,null);
            }
        });
    }

    @Override
    public void getHistoryMissBean(String dayStart, String userId, String token, String dayEnd,
                                   int page,
                                   int pageSize) {
        toSubscribe(HttpManager.getApi().getHistoryMissBean(dayStart, userId, token, dayEnd,page,pageSize), new HttpSubscriber<List<HistoryMissPositionBean>>() {
            @Override
            protected void _onNext(List<HistoryMissPositionBean> o) {
                super._onNext(o);
                mView.bindHistoryMissBean(o);
            }

            @Override
            protected void _onError(String message, int code) {
                super._onError(message, code);
                ToastUtil.show(message);
                mView.showErrorMsg(message,null);
            }
        });
    }

    @Override
    public void getHistoryCloseBean(String dayStart, String userId, String token, String dayEnd,
                                    int page,
                                    int pageSize) {
        toSubscribe(HttpManager.getApi().getHistoryCloseBean(dayStart, userId, token, dayEnd,page,pageSize), new HttpSubscriber<List<HistoryClosePositionBean>>() {
            @Override
            protected void _onNext(List<HistoryClosePositionBean> o) {
                super._onNext(o);
                mView.bindHistoryCloseBean(o);
            }

            @Override
            protected void _onError(String message, int code) {
                super._onError(message, code);
                ToastUtil.show(message);
                mView.showErrorMsg(message,null);
            }
        });

    }

    @Override
    public void getHistoryBuilderBean(String dayStart, String userId, String token, String dayEnd,
                                      int page,
                                      int pageSize) {
        toSubscribe(HttpManager.getApi().getHistoryBuilderBean(dayStart, userId, token, dayEnd,page,pageSize), new HttpSubscriber<List<HistoryBuiderPositionBean>>() {
            @Override
            protected void _onNext(List<HistoryBuiderPositionBean> o) {
                super._onNext(o);
                mView.bindHistoryBuilderBean(o);
            }

            @Override
            protected void _onError(String message, int code) {
                super._onError(message, code);
                ToastUtil.show(message);
                mView.showErrorMsg(message,null);
            }
        });

    }
}
