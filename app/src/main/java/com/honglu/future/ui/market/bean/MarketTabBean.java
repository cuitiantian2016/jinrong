package com.honglu.future.ui.market.bean;

/**
 * Created by zhuaibing on 2018/1/5
 */

public class MarketTabBean {
    public String title;
    public String type;

    public MarketTabBean(String title,String type){
        this.title = title;
        this.type = type;
    }

    public String getTitle() {
        return title;
    }


    public String getType() {
        return type;
    }
}
