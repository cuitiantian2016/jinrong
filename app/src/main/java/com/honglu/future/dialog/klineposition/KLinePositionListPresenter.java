package com.honglu.future.dialog.klineposition;

import com.honglu.future.base.BasePresenter;
import com.honglu.future.config.Constant;
import com.honglu.future.http.HttpManager;
import com.honglu.future.http.HttpSubscriber;
import com.honglu.future.ui.trade.bean.ProductListBean;
import com.honglu.future.util.SpUtil;

/**
 * Created by zhuaibing on 2018/1/11
 */

public class KLinePositionListPresenter extends BasePresenter<KLinePositionListContract.View> implements KLinePositionListContract.Presenter{

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
}
