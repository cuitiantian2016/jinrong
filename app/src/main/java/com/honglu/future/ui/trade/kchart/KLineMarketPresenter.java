package com.honglu.future.ui.trade.kchart;

import com.honglu.future.base.BasePresenter;
import com.honglu.future.config.Constant;
import com.honglu.future.http.HttpManager;
import com.honglu.future.http.HttpSubscriber;
import com.honglu.future.ui.trade.bean.HoldPositionBean;
import com.honglu.future.ui.trade.bean.ProductListBean;
import com.honglu.future.ui.trade.bean.ProductListBean;
import com.honglu.future.ui.trade.bean.RealTimeBean;
import com.honglu.future.util.SpUtil;

import java.util.List;

/**
 * Created by zq on 2017/11/7.
 */

public class KLineMarketPresenter extends BasePresenter<KLineMarketContract.View> implements KLineMarketContract.Presenter {
    @Override
    public void getProductRealTime(String codes) {
        toSubscribe(HttpManager.getApi().getProductRealTime(codes.replace("|", "%7C")), new HttpSubscriber<RealTimeBean>() {
            @Override
            public void _onStart() {
                //mView.showLoading("查询行情中...");
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

    @Override
    public void getProductDetail(String instrumentId) {
        toSubscribe(HttpManager.getApi().getProductDetail(instrumentId, SpUtil.getString(Constant.COMPANY_TYPE)), new HttpSubscriber<ProductListBean>() {
            @Override
            public void _onStart() {
               // mView.showLoading("获取产品详情中...");
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


    /**
     * 持仓列表
     * @param userId
     * @param token
     * @param company
     */
    @Override
    public void getHoldPositionList(String userId, String token, String company) {
        toSubscribe(HttpManager.getApi().getHoldPositionList(userId, token, company), new HttpSubscriber<List<HoldPositionBean>>() {
            @Override
            public void _onStart() {

            }

            @Override
            protected void _onError(String message) {
                super._onError(message);

            }

            @Override
            protected void _onNext(List<HoldPositionBean> list) {
                mView.getHoldPositionListSuccess(list);
            }
        });
    }
}
