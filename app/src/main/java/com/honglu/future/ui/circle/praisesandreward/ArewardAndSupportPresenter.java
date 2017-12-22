package com.honglu.future.ui.circle.praisesandreward;

import android.text.TextUtils;

import com.google.gson.JsonNull;
import com.honglu.future.base.BasePresenter;
import com.honglu.future.http.HttpManager;
import com.honglu.future.http.HttpSubscriber;
import com.honglu.future.ui.circle.bean.ArewardListBean;
import com.honglu.future.util.ToastUtil;

import java.util.List;

/**
 * Created by zhuaibing on 2017/12/20
 */

public class ArewardAndSupportPresenter extends BasePresenter<ArewardAndSupportContract.View> implements ArewardAndSupportContract.Presenter{

    //打赏列表
    @Override
    public void getArewardList(String userId, String postId, int rows) {
        toSubscribe(HttpManager.getApi().getArewardList(userId, postId, rows), new HttpSubscriber<List<ArewardListBean>>() {
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
            public void onNext(List<ArewardListBean> listBeans) {
                mView.stopLoading();
                mView.getArewardList(listBeans);
            }
        });
    }


    //关注
    @Override
    public void focus(final String postUserId, final String userId, final String attentionState) {
        toSubscribe(HttpManager.getApi().focus(postUserId, userId, attentionState), new HttpSubscriber<JsonNull>() {
            @Override
            protected void _onStart() {
                mView.showLoading("关注中...");
            }

            @Override
            protected void _onError(String message, int code) {
                mView.stopLoading();
                mView.showErrorMsg(message,null);
            }

            @Override
            public void onNext(JsonNull jsonNull) {
                mView.stopLoading();
               if (TextUtils.equals("0",attentionState)){
                   ToastUtil.show("取消关注成功");
               } else{
                   ToastUtil.show("关注成功");
               }
               mView.focus(postUserId,attentionState);
            }
        });
    }
}
