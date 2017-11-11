package com.honglu.future.ui.market.contract;

import com.honglu.future.base.BaseView;
import com.honglu.future.ui.market.bean.MarketnalysisBean;

/**
 * Created by zhuaibing on 2017/10/25
 */

public interface MarketItemContract {

    interface View extends BaseView {
          void getMarketData(MarketnalysisBean alysisBean);
    }

    interface Presenter{
        void getMarketData();
    }
}
