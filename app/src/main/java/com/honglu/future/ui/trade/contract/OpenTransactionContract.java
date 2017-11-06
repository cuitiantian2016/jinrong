package com.honglu.future.ui.trade.contract;

import com.honglu.future.base.BaseView;
import com.honglu.future.ui.trade.bean.AccountBean;
import com.honglu.future.ui.trade.bean.ProductListBean;
import com.honglu.future.ui.trade.bean.SettlementInfoBean;

import java.util.List;

/**
 * Created by zq on 2017/10/26.
 */

public interface OpenTransactionContract {
    interface View extends BaseView {
        void querySettlementSuccess(SettlementInfoBean bean);
        void getProductListSuccess(List<ProductListBean> bean);
        void buildTransactionSuccess();
    }
    interface Presenter{
        void querySettlementInfo(String userId, String token, String company);
        void getProductList();
        void buildTransaction(String orderNumber, String type, String price, String instrumentId, String userId, String token, String company);
    }
}
