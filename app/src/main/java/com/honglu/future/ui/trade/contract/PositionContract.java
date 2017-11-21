package com.honglu.future.ui.trade.contract;

import com.honglu.future.base.BaseView;
import com.honglu.future.ui.trade.bean.HoldDetailBean;
import com.honglu.future.ui.trade.bean.HoldPositionBean;
import com.honglu.future.ui.trade.bean.ProductListBean;
import com.honglu.future.ui.usercenter.bean.AccountInfoBean;

import java.util.List;

/**
 * Created by zq on 2017/10/26.
 */

public interface PositionContract {
    interface View extends BaseView {
        void getHoldPositionListSuccess(List<HoldPositionBean> list);
        void getHoldDetailListSuccess(List<HoldDetailBean> list);
        void closeOrderSuccess();
        void getAccountInfoSuccess(AccountInfoBean bean);
        void getProductDetailSuccess(ProductListBean bean);
    }

    interface Presenter {
        void getHoldPositionList(String userId, String token, String company);
        void getHoldPositionDetail(String instrumentId, String type, String todayPosition, String userId, String token);
        void closeOrder(String todayPosition, String userId, String token, String orderNumber, String type, String price,
                        String instrumentId, String holdAvgPrice, String company);
        void getAccountInfo(String userId, String token, String company);
        void getProductDetail(String instrumentId);
    }
}
