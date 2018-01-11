package com.honglu.future.dialog.klineposition;

import com.honglu.future.base.BaseView;
import com.honglu.future.ui.trade.bean.ProductListBean;

/**
 * Created by zhuaibing on 2018/1/11
 */

public interface KLinePositionListContract {
    interface View extends BaseView {
        void getProductDetailSuccess(ProductListBean bean);
    }

    interface Presenter {
        void getProductDetail(String instrumentId);
    }
}
