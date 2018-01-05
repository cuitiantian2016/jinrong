package com.honglu.future.ui.msg.praisemsg;

import com.google.gson.JsonNull;
import com.honglu.future.base.BasePresenter;
import com.honglu.future.http.HttpManager;
import com.honglu.future.http.HttpSubscriber;
import com.honglu.future.ui.msg.bean.PraiseMsgListBean;

import java.util.List;

/**
 * Created by zhuaibing on 2018/1/2
 */

public class PraiseMsgPresenter extends BasePresenter<PraiseMsgContract.View> implements PraiseMsgContract.Presenter{


    @Override
    public void getPraiseList(String userId, int rows) {
        toSubscribe(HttpManager.getApi().getPraiseList(userId, rows), new HttpSubscriber<List<PraiseMsgListBean>>() {
            @Override
            protected void _onStart() {
               mView.showLoading("加载中...");
            }

            @Override
            protected void _onError(String message, int code) {
                mView.stopLoading();
                mView.showErrorMsg(message,null);
            }

            @Override
            protected void _onNext(List<PraiseMsgListBean> listBeans) {
                mView.stopLoading();
                 mView.getPraiseList(listBeans);
            }
        });
    }


    //清空赞列表
    @Override
    public void getClearPraiseMsg(String userId) {
        toSubscribe(HttpManager.getApi().getClearPraiseMsg(userId), new HttpSubscriber<JsonNull>() {
            @Override
            protected void _onStart() {
                mView.showLoading("加载中...");
            }

            @Override
            protected void _onError(String message, int code) {
                mView.stopLoading();
                mView.showErrorMsg(message,null);
            }

            @Override
            protected void _onNext(JsonNull jsonNull) {
                mView.stopLoading();
                mView.clearPraiseMsg();
            }
        });
    }
}
