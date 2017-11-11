package com.honglu.future.ui.trade.presenter;

import android.util.Log;

import com.honglu.future.base.BasePresenter;
import com.honglu.future.http.HttpManager;
import com.honglu.future.http.HttpSubscriber;
import com.honglu.future.ui.trade.bean.ProductListBean;
import com.honglu.future.ui.trade.bean.SettlementInfoBean;
import com.honglu.future.ui.trade.contract.OpenTransactionContract;
import com.honglu.future.ui.usercenter.bean.AccountInfoBean;

import java.util.List;

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
                mView.showLoading("查询产品列表中...");
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
                mView.showErrorMsg(message, null);
            }

            @Override
            protected void _onCompleted() {
                mView.stopLoading();
            }
        });
    }

}
