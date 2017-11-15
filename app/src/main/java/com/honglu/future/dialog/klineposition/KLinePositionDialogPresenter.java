package com.honglu.future.dialog.klineposition;

import com.honglu.future.base.BasePresenter;
import com.honglu.future.http.HttpManager;
import com.honglu.future.http.HttpSubscriber;
import com.honglu.future.ui.trade.bean.ProductListBean;

/**
 * Created by zhuaibing on 2017/11/15
 */

public class KLinePositionDialogPresenter extends BasePresenter<KLinePositionDialogContract.View> implements KLinePositionDialogContract.Presenter {

    @Override
    public void getProductDetail(String instrumentId) {
        toSubscribe(HttpManager.getApi().getProductDetail(instrumentId), new HttpSubscriber<ProductListBean>() {
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
}
