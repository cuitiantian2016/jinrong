package com.honglu.future.dialog.closetransaction;

import com.honglu.future.base.BaseView;

/**
 * Created by zhuaibing on 2017/11/24
 */

public interface CloseTransactionContract {
    interface View extends BaseView {
        void closeOrderSuccess();
    }

    interface Presenter {
        void closeOrder(String todayPosition, String userId, String token, String orderNumber, String type, String price,
                        String instrumentId, String holdAvgPrice, String company);

        void ksCloseOrder(String todayPosition, String userId, String token, String orderNumber, String type, String price,
                          String instrumentId, String holdAvgPrice, String company);
    }
}
