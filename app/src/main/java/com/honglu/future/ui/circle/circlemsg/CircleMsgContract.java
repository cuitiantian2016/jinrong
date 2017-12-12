package com.honglu.future.ui.circle.circlemsg;

import com.google.gson.JsonNull;
import com.honglu.future.base.BaseView;
import com.honglu.future.ui.circle.bean.CircleMsgBean;

import java.util.List;

/**
 * Created by zhuaibing on 2017/12/7
 */

public interface CircleMsgContract {
    interface View extends BaseView {
        void circleMsgData(List<CircleMsgBean> list);

        void clearCircle();

        void  getCommentContent(JsonNull jsonNull, int replyType);//评论回复
    }

    interface Presenter {
        void getCircleRevert(int rows);//收到评论

        void getCircleCommentaries(int rows);//发出的评论

        void getClearReply();//清空收到的评论

        void getClearComments();//清空发出的评论

        void  getCommentContent(String userId, String circleId, String content,String beReplyUserId, int replyType,String replyNickName,String postUserId);//评论回复
    }
}
