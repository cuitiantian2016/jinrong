package com.honglu.future.ui.circle.morecomment;

import com.google.gson.JsonNull;
import com.honglu.future.base.BaseView;
import com.honglu.future.ui.circle.bean.CommentBosAllBean;

import java.util.List;

/**
 * Created by zhuaibing on 2018/1/3
 */

public interface MoreCommentContract {
    interface View extends BaseView {
        void getLayComment(List<CommentBosAllBean> list);
        void  getCommentContent(String circleReplyId, int replyType);//评论回复
        void  getCommentContentError();
    }

    interface Presenter {
       void getLayComment(String userId,String fatherCircleReplyId,int rows);
       void  getCommentContent(String userId, String circleId, String content,String beReplyUserId, int replyType ,String replyNickName,String postUserId,String fatherCircleReplyId,String layCircleReplyId);//评论回复
    }
}
