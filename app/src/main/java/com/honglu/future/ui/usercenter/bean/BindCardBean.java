package com.honglu.future.ui.usercenter.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by zhuaibing on 2017/11/13
 */

public class BindCardBean implements Serializable {
    private String bankId;  //银行编码
    private String bankBranchId; //银行分支机构编码
    private String bankAccount;//银行帐号
    private String brokerBranchId; //期货公司分支机构编码
    private String bankName; //银行名称
    private String bankIcon;//图片地址
    public int rechargeFlag;// 充值 1 代表需要 2 代表不需要
    public int cashoutFlag; //提现 1 代表需要 2 代表不需要
    public int balanceFlag; //查询 1 代表需要 2 代表不需要
    private String rechargeTime; //充值时间
    private String cashoutTime; //提现时间

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getBankBranchId() {
        return bankBranchId;
    }

    public void setBankBranchId(String bankBranchId) {
        this.bankBranchId = bankBranchId;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getBrokerBranchId() {
        return brokerBranchId;
    }

    public void setBrokerBranchId(String brokerBranchId) {
        this.brokerBranchId = brokerBranchId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankIcon() {
        return bankIcon;
    }

    public void setBankIcon(String bankIcon) {
        this.bankIcon = bankIcon;
    }

    public int getRechargeFlag() {
        return rechargeFlag;
    }

    public void setRechargeFlag(int rechargeFlag) {
        this.rechargeFlag = rechargeFlag;
    }

    public int getCashoutFlag() {
        return cashoutFlag;
    }

    public void setCashoutFlag(int cashoutFlag) {
        this.cashoutFlag = cashoutFlag;
    }

    public int getBalanceFlag() {
        return balanceFlag;
    }

    public void setBalanceFlag(int balanceFlag) {
        this.balanceFlag = balanceFlag;
    }

    public String getRechargeTime() {
        return rechargeTime;
    }

    public void setRechargeTime(String rechargeTime) {
        this.rechargeTime = rechargeTime;
    }

    public String getCashoutTime() {
        return cashoutTime;
    }

    public void setCashoutTime(String cashoutTime) {
        this.cashoutTime = cashoutTime;
    }
}
