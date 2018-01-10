package com.honglu.future.ui.main.contract;

import com.honglu.future.base.BaseView;
import com.honglu.future.ui.trade.bean.ProductListBean;
import com.honglu.future.ui.usercenter.bean.AccountInfoBean;

/**
 * Created by zq on 2017/11/10.
 */

public interface BuildTransactionContract {
    interface View extends BaseView {
        void buildTransactionSuccess(String type,String hands);
        void getProductDetailSuccess(ProductListBean bean);
        void getAccountInfoSuccess(AccountInfoBean bean);
    }

    interface Presenter {
        void buildTransaction(boolean isFast,String orderNumber, String type, String price, String instrumentId, String userId, String token, String company);
        void getProductDetail(String instrumentId);
        void getAccountInfo();
    }
}
