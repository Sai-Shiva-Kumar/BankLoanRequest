package com.cg.banking.dto;

public class LoanSuccessMessage {

	private String message;

	public LoanSuccessMessage(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
