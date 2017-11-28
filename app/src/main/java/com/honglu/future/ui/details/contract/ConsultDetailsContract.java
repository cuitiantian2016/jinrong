package com.honglu.future.ui.details.contract;

import com.honglu.future.base.BaseView;
import com.honglu.future.ui.details.bean.ConsultDetailsBean;
import com.honglu.future.ui.details.bean.InformationCommentBean;

import java.util.List;

/**
 * Created by hc on 2017/10/24.
 */

public interface ConsultDetailsContract {

    interface View extends BaseView {
        /**
         * 获取数据
         * @param bean
         */
        void bindData(ConsultDetailsBean bean);
        /**
         * 点赞成功
         */
        void praiseSuccess(List<String> praiseUserList);
        /**
         * 评论回复成功
         */
        void commentSuccess();
        /**
         * 绑定消息返回消息的集合
         * @param list
         */
        void bindReplyList(List<InformationCommentBean> list);

    }
    interface Presenter{
        /**
         * 获取消息Id
         * @param messageId
         */
        void getMessageData(String messageId);
        /**
         * 点赞
         * @param messageId
         */
        void praiseMessage(String messageId,String userId);
        /**
         * 评论或者回复
         */
        void commentMessage(String userID,String messageId,String repayUserID,String ontent);
        /**
         * 获取帖子的评论列表
         * @param messageId 消息id
         */
        void getReplyList(String messageId);
    }
}
