package com.honglu.future.ui.trade.historybill;

import com.honglu.future.base.BaseView;
import com.honglu.future.ui.trade.bean.SettlementInfoBean;

/**
 * Created by zq on 2017/11/3.
 */

public interface HistoryBillContract {
    interface View extends BaseView {
        void querySettlementSuccess(SettlementInfoBean bean);
    }

    interface Presenter {
        void querySettlementInfoByDate(String userId, String token, String company, String day);
    }
}
