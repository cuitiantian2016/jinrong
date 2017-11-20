package com.honglu.future.ui.trade.presenter;

import com.honglu.future.base.BasePresenter;
import com.honglu.future.http.HttpManager;
import com.honglu.future.http.HttpSubscriber;
import com.honglu.future.ui.trade.bean.HistoryClosePositionBean;
import com.honglu.future.ui.trade.contract.ClosePositionContract;

import java.util.List;

/**
 * Created by zq on 2017/10/26.
 */

public class ClosePositionPresenter extends BasePresenter<ClosePositionContract.View> implements ClosePositionContract.Presenter {
    @Override
    public void getCloseList(String dayStart, String dayEnd, String userId, String token,int page,int pageSize) {
        toSubscribe(HttpManager.getApi().getHistoryCloseBean(dayStart, userId, token, dayEnd,page,pageSize), new HttpSubscriber<List<HistoryClosePositionBean>>() {
            @Override
            public void _onStart() {
                mView.showLoading("查询已平仓列表...");
            }

            @Override
            protected void _onNext(List<HistoryClosePositionBean> list) {
                mView.getCloseListSuccess(list);
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
