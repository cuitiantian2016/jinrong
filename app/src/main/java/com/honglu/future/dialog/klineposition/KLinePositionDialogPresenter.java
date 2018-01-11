package com.honglu.future.dialog.klineposition;

import com.honglu.future.base.BasePresenter;
import com.honglu.future.config.Constant;
import com.honglu.future.http.HttpManager;
import com.honglu.future.http.HttpSubscriber;
import com.honglu.future.ui.trade.bean.ProductListBean;
import com.honglu.future.util.SpUtil;

/**
 * Created by zhuaibing on 2017/11/15
 */

public class KLinePositionDialogPresenter extends BasePresenter<KLinePositionDialogContract.View> implements KLinePositionDialogContract.Presenter {

    //产品详情
    @Override
    public void getProductDetail(String instrumentId) {
        toSubscribe(HttpManager.getApi().getProductDetail(instrumentId, SpUtil.getString(Constant.COMPANY_TYPE)), new HttpSubscriber<ProductListBean>() {
            @Override
            public void _onStart() {
                mView.showLoading("获取产品详情中...");
            }

            @Override
            protected void _onNext(ProductListBean bean) {
                mView.getProductDetailSuccess(bean);
            }

            @Override
            protected void _onError(String message) {
                mView.getProductDetailSuccess(null);
                mView.showErrorMsg(message, null);
            }

            @Override
            protected void _onCompleted() {
                mView.stopLoading();
            }
        });

    }


    //委托平仓
    @Override
    public void closeOrder(String todayPosition, String userId, String token, String orderNumber, String type
            , String price, String instrumentId, String holdAvgPrice, String company) {

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
                mView.closeOrderSuccess();
            }
        });
    }


    //快速平仓
    @Override
    public void ksCloseOrder(String todayPosition, String userId, String token, String orderNumber, String type, String price,
                             String instrumentId, String holdAvgPrice, String company) {
        toSubscribe(HttpManager.getApi().ksCloseOrder(todayPosition, userId, token, orderNumber, type, price,
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
}
