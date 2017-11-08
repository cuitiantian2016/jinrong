package com.honglu.future.ui.details.contract;

import com.honglu.future.base.BaseView;
import com.honglu.future.ui.details.bean.ConsultDetailsBean;

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
    }
}
