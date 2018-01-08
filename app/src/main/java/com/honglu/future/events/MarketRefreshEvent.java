package com.honglu.future.events;
import com.honglu.future.ui.market.bean.QuotationDataListBean;

/**
 * Created by zhuaibing on 2017/11/10
 */

public class MarketRefreshEvent {
    public String mTabSelectType;
    public String mInstrumentID;
    public String type;
    public QuotationDataListBean bean;

    public MarketRefreshEvent(String type,String mTabSelectType
            ,QuotationDataListBean bean){
        this.type = type;
        this.mTabSelectType = mTabSelectType;
        this.bean = bean;
    }

    public MarketRefreshEvent(String type,String mTabSelectType,String instrumentID,QuotationDataListBean bean){
        this.type = type;
        this.mTabSelectType = mTabSelectType;
        this.mInstrumentID = instrumentID;
        this.bean = bean;
    }
}
