package com.honglu.future.ui.trade.bean;

/**
 * Created by zq on 2017/10/26.
 */

public class OpenTransactionListBean {
    private String productName;//产品名称
    private String num;//产品成交量
    private String riseNum;//买涨人数
    private String downNum;//买跌人数

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getRiseNum() {
        return riseNum;
    }

    public void setRiseNum(String riseNum) {
        this.riseNum = riseNum;
    }

    public String getDownNum() {
        return downNum;
    }

    public void setDownNum(String downNum) {
        this.downNum = downNum;
    }

    public String getRiseRadio() {
        return riseRadio;
    }

    public void setRiseRadio(String riseRadio) {
        this.riseRadio = riseRadio;
    }

    public String getDownRadio() {
        return downRadio;
    }

    public void setDownRadio(String downRadio) {
        this.downRadio = downRadio;
    }

    public String getIsRest() {
        return isRest;
    }

    public void setIsRest(String isRest) {
        this.isRest = isRest;
    }

    private String riseRadio;//买涨比例
    private String downRadio;//买跌比例
    private String isRest;//是否休市
}
