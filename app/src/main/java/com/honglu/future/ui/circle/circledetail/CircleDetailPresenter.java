package com.honglu.future.ui.circle.circledetail;

import com.google.gson.JsonNull;
import com.honglu.future.base.BasePresenter;
import com.honglu.future.http.HttpManager;
import com.honglu.future.http.HttpSubscriber;
import com.honglu.future.ui.circle.bean.CircleDetailBean;
import com.honglu.future.ui.circle.bean.CommentAllBean;
import com.honglu.future.ui.circle.bean.CommentBean;

import java.util.List;

/**
 * Created by zhuaibing on 2017/12/7
 */

public class CircleDetailPresenter extends BasePresenter<CircleDetailContract.View> implements CircleDetailContract.Presenter{

    //帖子详情-head 数据
    @Override
    public void getDetailBean(String userId, String circleId) {
       toSubscribe(HttpManager.getApi().getClearDetailHead(userId, circleId), new HttpSubscriber<CircleDetailBean>() {
           @Override
           protected void _onStart() {

           }
           @Override
           protected void _onError(String message, int code) {

           }
           @Override
           protected void _onNext(CircleDetailBean bean) {
              mView.getDetailBean(bean);
           }
       });
    }


    //全部
    @Override
    public void getCirleComment(String userId, String circleId, String postUserId, int rows,String circleReplyId) {
        toSubscribe(HttpManager.getApi().getCirleCommentList(userId, circleId,postUserId,rows,circleReplyId), new HttpSubscriber<CommentAllBean>() {
            @Override
            protected void _onStart() {

            }
            @Override
            protected void _onError(String message, int code) {
                mView.showErrorMsg(message,null);
            }
            @Override
            protected void _onNext(CommentAllBean bean) {
                mView.getCirleComment(bean);
            }
        });
    }

    //楼主
    @Override
    public void getCirleCommentAuth(String userId, String circleId, String postUserId, int rows) {
        toSubscribe(HttpManager.getApi().getCirleCommentAuth(userId, circleId,postUserId,rows), new HttpSubscriber<List<CommentBean>>() {
            @Override
            protected void _onStart() {

            }
            @Override
            protected void _onError(String message, int code) {
                mView.showErrorMsg(message,null);
            }
            @Override
            protected void _onNext(List<CommentBean> list) {
                mView.getCirleCommentAuth(list);
            }
        });
    }

    //关注
    @Override
    public void getCirleFocus(String postUserId, String userId, final String attentionState) {
        toSubscribe(HttpManager.getApi().focus(postUserId,userId,attentionState), new HttpSubscriber<JsonNull>() {
            @Override
            protected void _onStart() {
               if ("0".equals(attentionState)){
                   mView.showLoading("取消中...");
               }else {
                   mView.showLoading("关注中...");
               }
            }
            @Override
            protected void _onError(String message, int code) {
                mView.showErrorMsg(message,null);
                mView.stopLoading();
            }
            @Override
            protected void _onNext(JsonNull jsonNull) {
                mView.stopLoading();
                mView.getCirleFocus(jsonNull);
            }
        });
    }


    //点赞
    @Override
    public void getCirlePraise(String postUserId, String userId, boolean praiseFlag, String circleId) {
        toSubscribe(HttpManager.getApi().praise(postUserId,userId,praiseFlag,circleId), new HttpSubscriber<JsonNull>() {
            @Override
            protected void _onStart() {
                mView.showLoading("点赞中...");
            }
            @Override
            protected void _onError(String message, int code) {
                mView.showErrorMsg(message,null);
                mView.stopLoading();
            }
            @Override
            protected void _onNext(JsonNull jsonNull) {
                mView.stopLoading();
                mView.getCirlePraise(jsonNull);
            }
        });
    }


    //评论回复
    @Override
    public void getCommentContent(String userId, String circleId, String content, String beReplyUserId, final int replyType ,String replyNickName,String postUserId) {
        toSubscribe(HttpManager.getApi().getCommentContent(userId,circleId,content,beReplyUserId,replyType,replyNickName,postUserId), new HttpSubscriber<JsonNull>() {
            @Override
            protected void _onStart() {
                mView.showLoading("发表中...");
            }
            @Override
            protected void _onError(String message, int code) {
                mView.showErrorMsg(message,null);
                mView.stopLoading();
                mView.getCommentContentError();
            }
            @Override
            protected void _onNext(JsonNull jsonNull) {
                mView.stopLoading();
                mView.getCommentContent(jsonNull,replyType);
            }
        });
    }
}
