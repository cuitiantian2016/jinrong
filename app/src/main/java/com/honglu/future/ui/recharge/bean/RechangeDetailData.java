package com.honglu.future.ui.recharge.bean;

/**
 * Created by hefei on 2017/11/14.
 * 出入金明细(充值明细)
 *
 * String type;(“充值”/“提现”)
 String date;交易日期
 String amount;交易金额
 String status;(“成功”/“失败”)
 String bankName;银行名称
 String bankAccount;银行卡号
 */

public class RechangeDetailData {
    public String type;//(“充值”/“提现”)
    public String date;//交易日期
    public String amount;//交易金额
    public String status;//(“成功”/“失败”)
    public String bankName;//银行名称
    public String bankAccount;//银行卡号
}
