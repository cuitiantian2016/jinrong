package com.honglu.future.ui.circle.circledetail;

import com.google.gson.JsonNull;
import com.honglu.future.base.BaseView;
import com.honglu.future.ui.circle.bean.CircleDetailBean;
import com.honglu.future.ui.circle.bean.CommentAllBean;
import com.honglu.future.ui.circle.bean.CommentBean;

import java.util.List;

/**
 * Created by zhuaibing on 2017/12/7
 */

public interface CircleDetailContract {
    interface View extends BaseView {
        void  getDetailBean(CircleDetailBean bean);
        void  getCirleComment(CommentAllBean bean);//全部
        void  getCirleCommentAuth(List<CommentBean> list); //楼主
        void  getCirleFocus(JsonNull jsonNull);//关注
        void  getCirlePraise(JsonNull jsonNull); //点赞
        void  getCommentContent(JsonNull jsonNull,int replyType);//评论回复
        void  getCommentContentError();
    }

    interface Presenter {
        void  getDetailBean(String userId,String circleId);
        void  getCirleComment(String userId,String circleId,String postUserId,int rows,String circleReplyId);//全部
        void  getCirleCommentAuth(String userId,String circleId,String postUserId,int rows); //楼主
        void  getCirleFocus(String postUserId,String userId,String attentionState);//关注
        void  getCirlePraise(String postUserId,String userId,boolean praiseFlag,String circleId);//点赞
        void  getCommentContent(String userId, String circleId, String content,String beReplyUserId, int replyType ,String replyNickName,String postUserId);//评论回复
    }
}
