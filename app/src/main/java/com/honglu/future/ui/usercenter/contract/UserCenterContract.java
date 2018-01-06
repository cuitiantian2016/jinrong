package com.honglu.future.ui.usercenter.contract;

import com.honglu.future.base.BaseView;
import com.honglu.future.ui.trade.bean.AccountBean;
import com.honglu.future.ui.usercenter.bean.AccountInfoBean;

/**
 * Created by zq on 2017/10/24.
 */

public interface UserCenterContract {
    interface View extends BaseView {
        void  getMsgRed(boolean readMsg);
    }
    interface Presenter{
        void  getMsgRed(String userId);
    }
}
