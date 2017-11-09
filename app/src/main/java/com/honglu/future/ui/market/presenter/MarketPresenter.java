package com.honglu.future.ui.market.presenter;

import com.honglu.future.base.BasePresenter;
import com.honglu.future.http.HttpManager;
import com.honglu.future.http.HttpSubscriber;
import com.honglu.future.ui.market.bean.MarketnalysisBean;
import com.honglu.future.ui.market.contract.MarketContract;

/**
 * Created by zhuaibing on 2017/10/25
 */

public class MarketPresenter extends BasePresenter<MarketContract.View> implements MarketContract.Presenter{

    @Override
    public void getMarketData() {
        toSubscribe(HttpManager.getApi().getMarketData(), new HttpSubscriber<MarketnalysisBean>() {
            @Override
            protected void _onStart() {

            }

            @Override
            protected void _onError(String message) {

            }

            @Override
            protected void _onNext(MarketnalysisBean alysisBean) {
                 mView.getMarketData(alysisBean);
            }
        });
    }
}
