package com.honglu.future.widget.kchart.fragment;

import com.honglu.future.base.BasePresenter;
import com.honglu.future.http.HttpManager;
import com.honglu.future.http.HttpSubscriber;
import com.honglu.future.ui.trade.bean.KLineBean;

/**
 * Created by zq on 2017/11/7.
 */

public class KLinePresenter extends BasePresenter<KLineContract.View> implements KLineContract.Presenter {
    @Override
    public void getKLineData(String excode, String code, String type) {
        toSubscribe(HttpManager.getApi().getKLineData(excode, code, type), new HttpSubscriber<KLineBean>() {
            @Override
            public void _onStart() {
                mView.showLoading("查询k线中...");
            }

            @Override
            protected void _onNext(KLineBean bean) {
                mView.getKLineDataSuccess(bean);
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
