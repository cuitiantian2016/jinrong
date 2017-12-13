package com.honglu.future.ui.market.presenter;

import com.honglu.future.base.BasePresenter;
import com.honglu.future.http.HttpManager;
import com.honglu.future.http.HttpSubscriber;
import com.honglu.future.ui.market.bean.MarketnalysisBean;
import com.honglu.future.ui.market.contract.MarketItemContract;
import com.honglu.future.ui.trade.bean.RealTimeBean;

/**
 * Created by zhuaibing on 2017/10/25
 */

public class MarketItemPresenter extends BasePresenter<MarketItemContract.View> implements MarketItemContract.Presenter{

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

    //是否休市接口
    @Override
    public void getRealTimeData(String codes) {
        //接口需要 所以替换成%7C
        toSubscribe(HttpManager.getApi().getProductRealTime(codes.replaceAll("\\|","%7C")), new HttpSubscriber<RealTimeBean>() {
            @Override
            protected void _onStart() {}
            @Override
            protected void _onError(String message) {}
            @Override
            protected void _onNext(RealTimeBean alysisBean) {
                mView.getRealTimeData(alysisBean);
            }
        });
    }
}
