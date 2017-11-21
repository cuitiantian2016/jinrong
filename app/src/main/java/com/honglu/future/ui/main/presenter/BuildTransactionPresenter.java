package com.honglu.future.ui.main.presenter;

import com.honglu.future.base.BasePresenter;
import com.honglu.future.http.HttpManager;
import com.honglu.future.http.HttpSubscriber;
import com.honglu.future.ui.main.contract.BuildTransactionContract;
import com.honglu.future.ui.trade.bean.ProductListBean;

/**
 * Created by zq on 2017/11/10.
 */

public class BuildTransactionPresenter extends BasePresenter<BuildTransactionContract.View> implements BuildTransactionContract.Presenter {
    @Override
    public void buildTransaction(boolean isFast, String orderNumber, String type, String price, String instrumentId, String userId, String token, String company) {
        rx.Observable ob;
        if (isFast) {
            ob = HttpManager.getApi().fastTransaction(orderNumber, type, price, instrumentId, userId, token);
        } else {
            ob = HttpManager.getApi().buildTransaction(orderNumber, type, price, instrumentId, userId, token, company);
        }
        toSubscribe(ob, new HttpSubscriber() {
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

    @Override
    public void getProductDetail(String instrumentId) {
        toSubscribe(HttpManager.getApi().getProductDetail(instrumentId), new HttpSubscriber<ProductListBean>() {
            @Override
            public void _onStart() {
                //mView.showLoading("获取产品详情中...");
            }

            @Override
            protected void _onNext(ProductListBean bean) {
                mView.getProductDetailSuccess(bean);
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
