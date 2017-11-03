package com.honglu.future.ui.trade.billconfirm;

import com.honglu.future.base.BaseView;
import com.honglu.future.ui.trade.bean.ConfirmBean;

/**
 * Created by zq on 2017/11/2.
 */

public interface BillConfirmContract {
    interface View extends BaseView {
        void queryConfirmSuccess(ConfirmBean bean);
    }

    interface Presenter {
        void settlementConfirm(String userId, String token);
    }
}
