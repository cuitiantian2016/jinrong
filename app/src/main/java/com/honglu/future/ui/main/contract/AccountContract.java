package com.honglu.future.ui.main.contract;

import android.widget.TextView;

import com.honglu.future.base.BaseView;
import com.honglu.future.ui.trade.bean.AccountBean;
import com.honglu.future.ui.trade.bean.ProductListBean;
import com.honglu.future.ui.trade.bean.SettlementInfoBean;

import java.util.List;

/**
 * Created by zq on 2017/11/3.
 */

public interface AccountContract {
    interface View extends BaseView {
        void loginSuccess(AccountBean bean);
    }
    interface Presenter{
        void login(String account, String password, String userId, String company, TextView tv_pass);
    }
}
