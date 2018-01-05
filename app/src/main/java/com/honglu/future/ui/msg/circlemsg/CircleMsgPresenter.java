package com.honglu.future.ui.msg.circlemsg;
import com.google.gson.JsonNull;
import com.honglu.future.base.BasePresenter;
import com.honglu.future.config.Constant;
import com.honglu.future.http.HttpManager;
import com.honglu.future.http.HttpSubscriber;
import com.honglu.future.ui.msg.bean.CircleMsgBean;
import com.honglu.future.util.SpUtil;

import java.util.List;

/**
 * Created by zhuaibing on 2017/12/7
 */

public class CircleMsgPresenter extends BasePresenter<CircleMsgContract.View> implements CircleMsgContract.Presenter{

    @Override
    public void getCircleRevert(int rows) {//收到的回复
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
    public void getCircleCommentaries(int rows) {//收到的评论
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
        toSubscribe(HttpManager.getApi().getClearComments(SpUtil.getString(Constant.CACHE_TAG_UID)), new HttpSubscriber<JsonNull>() {
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


    //回复评论
    @Override
    public void getCommentContent(String userId, String circleId, String content, String beReplyUserId,final int replyType ,String replyNickName,String postUserId) {
//        toSubscribe(HttpManager.getApi().getCommentContent(userId,circleId,content,beReplyUserId,replyType,replyNickName,postUserId), new HttpSubscriber<JsonNull>() {
//            @Override
//            protected void _onStart() {
//                mView.showLoading("发表中...");
//            }
//            @Override
//            protected void _onError(String message, int code) {
//                mView.showErrorMsg(message,null);
//                mView.stopLoading();
//                mView.getCommentContentError();
//            }
//            @Override
//            protected void _onNext(JsonNull jsonNull) {
//                mView.stopLoading();
//                mView.getCommentContent(jsonNull,replyType);
//            }
//        });
    }
}
