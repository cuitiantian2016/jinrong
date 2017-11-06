package com.honglu.future.ui.trade.presenter;

import com.honglu.future.base.BasePresenter;
import com.honglu.future.http.HttpManager;
import com.honglu.future.http.HttpSubscriber;
import com.honglu.future.ui.trade.bean.AccountBean;
import com.honglu.future.ui.trade.bean.ProductListBean;
import com.honglu.future.ui.trade.bean.SettlementInfoBean;
import com.honglu.future.ui.trade.contract.OpenTransactionContract;
import com.honglu.future.util.AESUtils;

import java.util.List;
import java.util.Objects;

/**
 * Created by zq on 2017/10/26.
 */

public class OpenTransactionPresenter extends BasePresenter<OpenTransactionContract.View> implements OpenTransactionContract.Presenter {


    @Override
    public void querySettlementInfo(String userId, String token, String company) {
        toSubscribe(HttpManager.getApi().querySettlementInfo(userId, token, company), new HttpSubscriber<SettlementInfoBean>() {
            @Override
            public void _onStart() {
                mView.showLoading("查询结算单中...");
            }

            @Override
            protected void _onNext(SettlementInfoBean bean) {
                mView.querySettlementSuccess(bean);
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
    public void getProductList() {
        toSubscribe(HttpManager.getApi().getProductList(), new HttpSubscriber<List<ProductListBean>>() {
            @Override
            public void _onStart() {
                mView.showLoading("查询结算单中...");
            }

            @Override
            protected void _onNext(List<ProductListBean> bean) {
                mView.getProductListSuccess(bean);
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
    public void buildTransaction(String orderNumber, String type, String price, String instrumentId, String userId, String token, String company) {
        toSubscribe(HttpManager.getApi().buildTransaction(orderNumber, type, price, instrumentId, userId, token, company), new HttpSubscriber() {
            @Override
            public void _onStart() {
                mView.showLoading("建仓中...");
            }

            @Override
            protected void _onNext(Object o) {
                mView.buildTransactionSuccess();
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
