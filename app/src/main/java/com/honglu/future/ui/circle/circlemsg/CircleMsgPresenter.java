package com.honglu.future.ui.circle.circlemsg;
import com.google.gson.JsonNull;
import com.honglu.future.base.BasePresenter;
import com.honglu.future.config.Constant;
import com.honglu.future.http.HttpManager;
import com.honglu.future.http.HttpSubscriber;
import com.honglu.future.ui.circle.bean.CircleMsgBean;
import com.honglu.future.util.SpUtil;

import java.util.List;

/**
 * Created by zhuaibing on 2017/12/7
 */

public class CircleMsgPresenter extends BasePresenter<CircleMsgContract.View> implements CircleMsgContract.Presenter{

    @Override
    public void getCircleRevert(int rows) {//收到评论
        //22
      toSubscribe(HttpManager.getApi().getCircleRevert(SpUtil.getString(Constant.CACHE_TAG_UID), rows), new HttpSubscriber<List<CircleMsgBean>>() {
          @Override
          protected void _onStart() {
              mView.showLoading("加载中...");
          }

          @Override
          protected void _onError(String message) {
              mView.stopLoading();
              mView.showErrorMsg(message,null);
          }
          @Override
          protected void _onNext(List<CircleMsgBean> list) {
              mView.stopLoading();
              mView.circleMsgData(list);
          }
      });
    }

    @Override
    public void getCircleCommentaries(int rows) {//发出的评论
        //22
        toSubscribe(HttpManager.getApi().getCircleCommentaries(SpUtil.getString(Constant.CACHE_TAG_UID),rows), new HttpSubscriber<List<CircleMsgBean>>() {
            @Override
            protected void _onStart() {
                mView.showLoading("加载中...");
            }

            @Override
            protected void _onError(String message) {
                mView.stopLoading();
                mView.showErrorMsg(message,null);
            }
            @Override
            protected void _onNext(List<CircleMsgBean> list) {
                mView.stopLoading();
                mView.circleMsgData(list);
            }
        });
    }

    @Override
    public void getClearReply() {
        //22
        toSubscribe(HttpManager.getApi().getClearReply(SpUtil.getString(Constant.CACHE_TAG_UID)), new HttpSubscriber<JsonNull>() {
            @Override
            protected void _onStart() {
                mView.showLoading("清空中...");
            }

            @Override
            protected void _onError(String message) {
                mView.stopLoading();
                mView.showErrorMsg(message,null);
            }
            @Override
            protected void _onNext(JsonNull jsonNull) {
                mView.stopLoading();
                mView.clearCircle();
            }
        });
    }

    @Override
    public void getClearComments() {
        //SpUtil.getString(Constant.CACHE_TAG_UID);
        toSubscribe(HttpManager.getApi().getClearComments("22"), new HttpSubscriber<JsonNull>() {
            @Override
            protected void _onStart() {
                mView.showLoading("清空中...");
            }

            @Override
            protected void _onError(String message) {
                mView.stopLoading();
                mView.showErrorMsg(message,null);
            }
            @Override
            protected void _onNext(JsonNull jsonNull) {
                mView.stopLoading();
                mView.clearCircle();
            }
        });
    }
}
