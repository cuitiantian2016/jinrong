package com.honglu.future.ui.msg.praisemsg;

import com.honglu.future.base.BaseView;
import com.honglu.future.ui.msg.bean.PraiseMsgListBean;


import java.util.List;

/**
 * Created by zhuaibing on 2018/1/2
 */

public interface PraiseMsgContract {

    interface View extends BaseView {
        void getPraiseList(List<PraiseMsgListBean> list);
        void clearPraiseMsg();
    }

    interface Presenter {
        void getPraiseList(String userId,int rows);
        void getClearPraiseMsg(String userId);
    }
}
