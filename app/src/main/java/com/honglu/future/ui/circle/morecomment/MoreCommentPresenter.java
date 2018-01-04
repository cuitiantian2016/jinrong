package com.honglu.future.ui.circle.morecomment;

import com.google.gson.JsonNull;
import com.honglu.future.base.BasePresenter;
import com.honglu.future.http.HttpManager;
import com.honglu.future.http.HttpSubscriber;
import com.honglu.future.ui.circle.bean.CommentBosAllBean;

import java.util.List;

/**
 * Created by zhuaibing on 2018/1/3
 */

public class MoreCommentPresenter extends BasePresenter<MoreCommentContract.View> implements MoreCommentContract.Presenter{

    /**
     * 查看更多回复
     * @param userId
     * @param fatherCircleReplyId
     * @param rows
     */
    @Override
    public void getLayComment(String userId, String fatherCircleReplyId, int rows) {
        toSubscribe(HttpManager.getApi().getLayComment(userId, fatherCircleReplyId, rows), new HttpSubscriber<List<CommentBosAllBean>>() {
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
            public void onNext(List<CommentBosAllBean> list) {
                mView.stopLoading();
                mView.getLayComment(list);
            }
        });
    }


    @Override
    public void getCommentContent(String userId, String circleId, String content, String beReplyUserId,final int replyType, String replyNickName, String postUserId, String fatherCircleReplyId, String layCircleReplyId) {
        toSubscribe(HttpManager.getApi().getCommentContent(userId,circleId,content,beReplyUserId,replyType,replyNickName,postUserId,fatherCircleReplyId,layCircleReplyId), new HttpSubscriber<String>() {
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
            protected void _onNext(String circleReplyId) {
                mView.stopLoading();
                mView.getCommentContent(circleReplyId,replyType);
            }
        });
    }
}
