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

    public static final String TYPE_HISTORY_TRADE="TYPE_HISTORY_TRADE";
    public static final String TYPE_HISTORY_Build="TYPE_HISTORY_Build";
    public static final String TYPE_HISTORY_MISS="TYPE_HISTORY_MISS";
    public static final String TYPE_HISTORY_cLOSE="TYPE_HISTORY_cLOSE";

    @Override
    public void getHistoryTradeBean(final String dayStart, final String userId, final String token, final String dayEnd) {
        HttpSubscriber<HistoryTradeBean> httpSubscriber = new HttpSubscriber<HistoryTradeBean>() {
            @Override
            protected void _onNext(HistoryTradeBean o) {
                super._onNext(o);
                mView.bindHistoryTradeBean(o);
            }

            @Override
            protected void _onError(String message) {
                super._onError(message);
                mView.showErrorMsg(message, TYPE_HISTORY_TRADE);
            }
        };
        toSubscribe(HttpManager.getApi().getHistoryTradeBean(dayStart, userId, token, dayEnd),httpSubscriber);
    }

    @Override
    public void getHistoryMissBean(final String dayStart, final String userId, final String token, final String dayEnd,
                                   final int page,
                                   final int pageSize) {
        HttpSubscriber<List<HistoryMissPositionBean>> httpSubscriber = new HttpSubscriber<List<HistoryMissPositionBean>>() {
            @Override
            protected void _onNext(List<HistoryMissPositionBean> o) {
                super._onNext(o);
                mView.bindHistoryMissBean(o);
            }
            @Override
            protected void _onError(String message, int code) {
                super._onError(message, code);
                mView.showErrorMsg(message, TYPE_HISTORY_MISS);
            }
        };
        toSubscribe(HttpManager.getApi().getHistoryMissBean(dayStart, userId, token, dayEnd,page,pageSize),httpSubscriber);
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
                mView.showErrorMsg(message,TYPE_HISTORY_cLOSE);
            }
        });

    }

    @Override
    public void getHistoryBuilderBean(final String dayStart, final String userId, final String token, final String dayEnd,
                                      final int page,
                                      final int pageSize) {
        HttpSubscriber<List<HistoryBuiderPositionBean>> httpSubscriber = new HttpSubscriber<List<HistoryBuiderPositionBean>>() {
            @Override
            protected void _onNext(List<HistoryBuiderPositionBean> o) {
                super._onNext(o);
                mView.bindHistoryBuilderBean(o);
            }
            @Override
            protected void _onError(String message, int code) {
                super._onError(message, code);
                mView.showErrorMsg(message, TYPE_HISTORY_Build);
            }
        };
        toSubscribe(HttpManager.getApi().getHistoryBuilderBean(dayStart, userId, token, dayEnd,page,pageSize),httpSubscriber);

    }
}
