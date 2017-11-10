package com.honglu.future.events;

import com.honglu.future.ui.market.bean.MarketnalysisBean;

/**
 * Created by zhuaibing on 2017/11/10
 */

public class MarketRefreshEvent {
    public String mTabSelectType;
    public String type;
    public MarketnalysisBean.ListBean.QuotationDataListBean bean;

    public MarketRefreshEvent(String type,String mTabSelectType
            ,MarketnalysisBean.ListBean.QuotationDataListBean bean){
        this.type = type;
        this.mTabSelectType = mTabSelectType;
        this.bean = bean;
    }
}
