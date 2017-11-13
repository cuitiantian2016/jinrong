package com.honglu.future.ui.trade.presenter;

import android.util.Log;

import com.honglu.future.base.BasePresenter;
import com.honglu.future.config.Constant;
import com.honglu.future.http.HttpManager;
import com.honglu.future.http.HttpSubscriber;
import com.honglu.future.ui.trade.bean.AccountBean;
import com.honglu.future.ui.trade.bean.HoldDetailBean;
import com.honglu.future.ui.trade.bean.HoldPositionBean;
import com.honglu.future.ui.trade.contract.PositionContract;
import com.honglu.future.ui.usercenter.bean.AccountInfoBean;
import com.honglu.future.util.AESUtils;
import com.honglu.future.util.SpUtil;

import java.util.List;
import java.util.Objects;

/**
 * Created by zq on 2017/10/26.
 */

public class PositionPresenter extends BasePresenter<PositionContract.View> implements PositionContract.Presenter {
    @Override
    public void getHoldPositionList(String userId, String token, String company) {
        toSubscribe(HttpManager.getApi().getHoldPositionList(userId, token, company), new HttpSubscriber<List<HoldPositionBean>>() {
            @Override
            public void _onStart() {
                mView.showLoading("查询持仓列表...");
            }

            @Override
            protected void _onNext(List<HoldPositionBean> list) {
                mView.getHoldPositionListSuccess(list);
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
    public void getHoldPositionDetail(String instrumentId, String type, String todayPosition, String userId, String token) {
        toSubscribe(HttpManager.getApi().getHoldPositionDetail(instrumentId, type, todayPosition, userId, token), new HttpSubscriber<List<HoldDetailBean>>() {
            @Override
            public void _onStart() {
                mView.showLoading("查询持仓详情...");
            }

            @Override
            protected void _onNext(List<HoldDetailBean> list) {
                mView.getHoldDetailListSuccess(list);
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
    public void closeOrder(String todayPosition, String userId, String token, String orderNumber, String type, String price,
                           String instrumentId, String holdAvgPrice, String company) {
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

    @Override
    public void getAccountInfo(String userId, String token, String company) {
        toSubscribe(HttpManager.getApi().getAccountInfo(userId, token, company), new HttpSubscriber<AccountInfoBean>() {
            @Override
            public void _onStart() {
                Log.d("xxxx","startRun");
                // mView.showLoading("获取中...");
            }

            @Override
            protected void _onNext(AccountInfoBean bean) {
                mView.getAccountInfoSuccess(bean);
            }

            @Override
            protected void _onError(String message) {
                //  mView.showErrorMsg(message, null);
            }

            @Override
            protected void _onCompleted() {
                mView.stopLoading();
            }
        });
    }
}
