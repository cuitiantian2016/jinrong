package com.honglu.future.ui.market.contract;

import com.honglu.future.base.BaseView;
import com.honglu.future.ui.market.bean.MarketnalysisBean;

import java.util.List;

/**
 * Created by zhuaibing on 2017/10/25
 */

public interface MarketContract {

    interface View extends BaseView {
          void getMarketData(MarketnalysisBean alysisBean);
    }

    interface Presenter{
        void getMarketData();
    }
}
