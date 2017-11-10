package com.honglu.future.ui.main.contract;

import com.honglu.future.base.BaseView;
import com.honglu.future.ui.trade.bean.ProductListBean;

/**
 * Created by zq on 2017/11/10.
 */

public interface BuildTransactionContract {
    interface View extends BaseView {
        void buildTransactionSuccess();
        void getProductDetailSuccess(ProductListBean bean);
    }

    interface Presenter {
        void buildTransaction(String orderNumber, String type, String price, String instrumentId, String userId, String token, String company);
        void getProductDetail(String instrumentId);
    }
}
