package com.honglu.future.ui.trade.contract;

import com.honglu.future.base.BaseView;
import com.honglu.future.ui.trade.bean.ProductListBean;
import com.honglu.future.ui.trade.bean.SettlementInfoBean;
import com.honglu.future.ui.usercenter.bean.AccountInfoBean;

import java.util.List;

/**
 * Created by zq on 2017/10/26.
 */

public interface OpenTransactionContract {
    interface View extends BaseView {
        void getProductListSuccess(List<ProductListBean> bean);
        void getAccountInfoSuccess(AccountInfoBean bean);
        void finishRefreshView();
    }
    interface Presenter{
        void getProductList();
        void getAccountInfo(String userId, String token, String company);
    }
}
