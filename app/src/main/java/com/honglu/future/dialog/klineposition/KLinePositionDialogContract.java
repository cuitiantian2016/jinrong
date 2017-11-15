package com.honglu.future.dialog.klineposition;

import com.honglu.future.base.BaseView;
import com.honglu.future.ui.trade.bean.HoldPositionBean;
import com.honglu.future.ui.trade.bean.ProductListBean;
import com.honglu.future.ui.trade.bean.RealTimeBean;

import java.util.List;

/**
 * Created by zhuaibing on 2017/11/15
 */

public interface KLinePositionDialogContract {
    interface View extends BaseView {
        void getProductDetailSuccess(ProductListBean bean);
    }

    interface Presenter {
        void getProductDetail(String instrumentId);
    }
}
