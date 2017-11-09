package com.honglu.future.ui.trade.kchart;

import com.honglu.future.base.BasePresenter;
import com.honglu.future.http.HttpManager;
import com.honglu.future.http.HttpSubscriber;
import com.honglu.future.ui.trade.bean.RealTimeBean;

/**
 * Created by zq on 2017/11/7.
 */

public class KLineMarketPresenter extends BasePresenter<KLineMarketContract.View> implements KLineMarketContract.Presenter {
    @Override
    public void getProductRealTime(String codes) {
        toSubscribe(HttpManager.getApi().getProductRealTime(codes.replace("|", "%7C")), new HttpSubscriber<RealTimeBean>() {
            @Override
            public void _onStart() {
                mView.showLoading("查询行情中...");
            }

            @Override
            protected void _onNext(RealTimeBean bean) {
                mView.getProductRealTimeSuccess(bean);
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
