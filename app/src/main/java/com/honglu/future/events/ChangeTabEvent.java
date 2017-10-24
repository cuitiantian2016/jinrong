package com.honglu.future.events;

public class ChangeTabEvent extends UIBaseEvent {
	private int loanType;
	
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
