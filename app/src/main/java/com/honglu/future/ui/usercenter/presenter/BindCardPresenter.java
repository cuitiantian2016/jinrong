package com.honglu.future.ui.usercenter.presenter;

import com.honglu.future.base.BasePresenter;
import com.honglu.future.config.Constant;
import com.honglu.future.http.HttpManager;
import com.honglu.future.http.HttpSubscriber;
import com.honglu.future.ui.usercenter.bean.BindCardBean;
import com.honglu.future.ui.usercenter.contract.BindCardContract;
import com.honglu.future.util.SpUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhuaibing on 2017/11/11
 */

public class BindCardPresenter extends BasePresenter<BindCardContract.View> implements BindCardContract.Presenter{

    @Override
    public void getBindCardInfo(String userId, String token) {
        toSubscribe(HttpManager.getApi().geBindCardData(userId, token, SpUtil.getString(Constant.COMPANY_TYPE)), new HttpSubscriber<List<BindCardBean>>() {
            @Override
            protected void _onStart() {
                mView.showLoading("获取中...");
            }

            @Override
            protected void _onError(String message) {
                mView.stopLoading();
                mView.showErrorMsg(message, null);
            }

            @Override
            protected void _onNext(List<BindCardBean> bindCardBeen) {
                mView.stopLoading();
                mView.getBindCardInfo(bindCardBeen);
            }
        });
    }
}
