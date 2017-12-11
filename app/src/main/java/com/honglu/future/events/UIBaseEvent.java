package com.honglu.future.events;

public class UIBaseEvent {
    public final static int EVENT_DEFAULT = -1;//默认
    public final static int EVENT_LOGIN = 0;//登录
    public final static int EVENT_LOGOUT = 1;//退出
    public final static int EVENT_LOAN_SUCCESS = 2;//申请成功(申请成功可能会被拒)
    public final static int EVENT_LOAN_FAILED = 3;//申请失败
    public final static int EVENT_SET_PAYPWD = 4;//设置交易密码
    public final static int EVENT_SET_PWD = 5;//设置登录密码
    public final static int EVENT_REPAY_SUCCESS = 6;//还款或续期成功
    public final static int EVENT_BANK_CARD_SUCCESS = 7;//银行卡绑定或修改成功
    public final static int EVENT_BORROW_MONEY_SUCCESS = 8;//借款成功(已打款,进入还款阶段)
    public final static int EVENT_REALNAME_AUTHENTICATION_SUCCESS = 8;//实名认证成功
    public final static int EVENT_UPDATE_AVATAR = 21;//修改头像成功
    public final static int EVENT_UPDATE_NICK_NAME = 22;//修改昵称成功
    public final static int EVENT_ACCOUNT_LOGOUT = 23;//退出期货账户成功
    public final static int EVENT_HOME_TO_MARKET_ZHULI = 24;//首页图标跳转行情主力合约
    public final static int EVENT_CLOSETRAD_REFRESH = 25; //平仓刷新持仓数据
    public final static int EVENT_CIRCLE_MSG_MAIN_RED = 26; //main 红点显示
    public final static int EVENT_CIRCLE_MSG_CIRCLE_RED = 27; //圈子红点显示
    public final static int EVENT_CIRCLE_MSG = 28; //圈子消息提醒 红点隐藏

    private String code;
    private String message;

    public UIBaseEvent() {
    }

    public UIBaseEvent(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
