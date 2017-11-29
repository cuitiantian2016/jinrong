package com.honglu.future.ui.trade.presenter;

import android.util.Log;

import com.honglu.future.base.BasePresenter;
import com.honglu.future.config.Constant;
import com.honglu.future.http.HttpManager;
import com.honglu.future.http.HttpSubscriber;
import com.honglu.future.ui.trade.bean.ProductListBean;
import com.honglu.future.ui.trade.bean.SettlementInfoBean;
import com.honglu.future.ui.trade.contract.OpenTransactionContract;
import com.honglu.future.ui.usercenter.bean.AccountInfoBean;
import com.honglu.future.util.SpUtil;

import java.util.List;

/**
 * Created by zq on 2017/10/26.
 */

public class OpenTransactionPresenter extends BasePresenter<OpenTransactionContract.View> implements OpenTransactionContract.Presenter {

    @Override
    public void getProductList() {
        toSubscribe(HttpManager.getApi().getProductList(), new HttpSubscriber<List<ProductListBean>>() {
            @Override
            public void _onStart() {
                //mView.showLoading("查询产品列表中...");
            }

            @Override
            protected void _onNext(List<ProductListBean> bean) {
                mView.getProductListSuccess(bean);
                mView.finishRefreshView();
            }

            @Override
            protected void _onError(String message) {
                mView.showErrorMsg(message, null);
                mView.finishRefreshView();
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
                SpUtil.putString(Constant.CACHE_USER_ASSES,bean.getWithdrawQuota() + "");
                SpUtil.putString(Constant.CACHE_USER_AVAILABLE_MONEY,bean.getAvailable() + "");
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
