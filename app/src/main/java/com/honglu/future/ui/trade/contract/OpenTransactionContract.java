package com.honglu.future.ui.trade.contract;

import com.honglu.future.base.BaseView;
import com.honglu.future.ui.trade.bean.AccountBean;
import com.honglu.future.ui.trade.bean.SettlementInfoBean;

/**
 * Created by zq on 2017/10/26.
 */

public interface OpenTransactionContract {
    interface View extends BaseView {
        void loginSuccess(AccountBean bean);
        void querySettlementSuccess(SettlementInfoBean bean);
    }
    interface Presenter{
        void login(String account, String password,String userId,String company);
        void querySettlementInfo(String userId, String token, String company);
    }
}
