package com.honglu.future.ui.msg.mainmsg;

import com.honglu.future.base.BaseView;
import com.honglu.future.ui.msg.bean.HasUnreadMsgBean;

/**
 * Created by zhuaibing on 2018/1/2
 */

public interface MainMsgContract {

    interface View extends BaseView {
           void hasUnreadMsg(HasUnreadMsgBean bean);
    }

    interface Presenter {
           void getHasUnreadMsg(String userId);
    }
}
