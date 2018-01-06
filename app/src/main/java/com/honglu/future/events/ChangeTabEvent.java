package com.honglu.future.events;

import retrofit2.http.PUT;

public class ChangeTabEvent extends UIBaseEvent {
    private int loanType;
    public String redirect;
    public boolean isStick;
    public int tab;

    public ChangeTabEvent(int loanType) {
        super();
        this.loanType = loanType;
    }

    public int getLoanType() {
        return loanType;
    }

    public void setLoanType(int loanType) {
        this.loanType = loanType;
    }

}
