package com.honglu.future.events;

/**
 * Created by zq on 2017/11/3.
 */

public class CloseAccountEvent extends UIBaseEvent{
    private int tabIndex;

    public CloseAccountEvent(int loanType) {
        super();
        this.tabIndex = loanType;
    }

    public int getLoanType() {
        return tabIndex;
    }

    public void setLoanType(int loanType) {
        this.tabIndex = loanType;
    }
}
