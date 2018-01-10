package com.honglu.future.ui.main.presenter;

import android.util.Log;

import com.honglu.future.app.AppManager;
import com.honglu.future.base.BasePresenter;
import com.honglu.future.config.Constant;
import com.honglu.future.dialog.AlertFragmentDialog;
import com.honglu.future.http.HttpManager;
import com.honglu.future.http.HttpSubscriber;
import com.honglu.future.ui.main.contract.BuildTransactionContract;
import com.honglu.future.ui.trade.bean.ProductListBean;
import com.honglu.future.ui.usercenter.bean.AccountInfoBean;
import com.honglu.future.util.SpUtil;

/**
 * Created by zq on 2017/11/10.
 */

public class BuildTransactionPresenter extends BasePresenter<BuildTransactionContract.View> implements BuildTransactionContract.Presenter {
    @Override
    public void buildTransaction(boolean isFast, final String orderNumber, final String type, String price, String instrumentId, String userId, String token, String company) {
        rx.Observable ob;
        if (isFast) {
            ob = HttpManager.getApi().fastTransaction(orderNumber, type, price, instrumentId, userId, token, company);
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
                mView.buildTransactionSuccess(type,orderNumber);
            }

            @Override
            protected void _onError(String message) {
                mView.showErrorMsg(message, "build");
            }

            @Override
            protected void _onCompleted() {
                mView.stopLoading();
            }
        });
    }

    @Override
    public void getProductDetail(String instrumentId) {
        toSubscribe(HttpManager.getApi().getProductDetail(instrumentId,SpUtil.getString(Constant.COMPANY_TYPE)), new HttpSubscriber<ProductListBean>() {
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

    @Override
    public void getAccountInfo() {
        toSubscribe(HttpManager.getApi().getAccountInfo(SpUtil.getString(Constant.CACHE_TAG_UID), SpUtil.getString(Constant.CACHE_ACCOUNT_TOKEN), SpUtil.getString(Constant.COMPANY_TYPE)), new HttpSubscriber<AccountInfoBean>() {
            @Override
            public void _onStart() {
                Log.d("xxxx","startRun");
                // mView.showLoading("获取中...");
            }

            @Override
            protected void _onNext(AccountInfoBean bean) {
                SpUtil.putString(Constant.CACHE_USER_ASSES,bean.getWithdrawQuota() + "");
                SpUtil.putString(Constant.CACHE_USER_AVAILABLE_MONEY,bean.getAvailable() + "");
                mView.getAccountInfoSuccess(bean);
            }

            @Override
            protected void _onError(String message) {
                  mView.showErrorMsg(message, Constant.CACHE_USER_AVAILABLE_MONEY);
            }

            @Override
            protected void _onCompleted() {
                mView.stopLoading();
            }
        });
    }
}
