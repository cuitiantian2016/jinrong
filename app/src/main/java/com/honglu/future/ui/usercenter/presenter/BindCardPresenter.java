package com.honglu.future.ui.usercenter.presenter;

import com.honglu.future.base.BasePresenter;
import com.honglu.future.http.HttpManager;
import com.honglu.future.http.HttpSubscriber;
import com.honglu.future.ui.usercenter.bean.BindCardBean;
import com.honglu.future.ui.usercenter.contract.BindCardContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhuaibing on 2017/11/11
 */

public class BindCardPresenter extends BasePresenter<BindCardContract.View> implements BindCardContract.Presenter{

    @Override
    public void getBindCardInfo(String userId, String token) {
//        toSubscribe(HttpManager.getApi().geBindCardData(userId, token), new HttpSubscriber<List<BindCardBean>>() {
//            @Override
//            protected void _onStart() {
//                mView.showLoading("获取中...");
//            }
//
//            @Override
//            protected void _onError(String message) {
//                mView.stopLoading();
//                mView.showErrorMsg(message, null);
//            }
//
//            @Override
//            protected void _onNext(List<BindCardBean> bindCardBeen) {
//                mView.stopLoading();
//                mView.getBindCardInfo(bindCardBeen);
//            }
//        });

        List<BindCardBean> list = new ArrayList<>();
        for (int i = 0 ; i < 8 ; i ++){
            BindCardBean bean = new BindCardBean();
            bean.setBankName("测试银行");
            bean.setBankAccount("622123456784761");
            bean.setBankId(i+1+"");
            list.add(bean);
        }
        mView.getBindCardInfo(list);
    }
}
