package com.honglu.future.ui.home.bean;

/**
 * Created by zq on 2017/3/15.
 */

public class JpushBean {

    /**
     * url : http://www.baidu.com
     * from : JPush
     */

    private String url;
    private String from;
    public boolean type;//true为显示小红点
    private String jump;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getJump() {
        return jump;
    }
}
