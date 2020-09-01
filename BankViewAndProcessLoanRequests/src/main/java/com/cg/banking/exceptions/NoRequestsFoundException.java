package com.cg.banking.exceptions;

@SuppressWarnings("serial")
public class NoRequestsFoundException extends Exception {

	public NoRequestsFoundException() {
		super();
		
	}

	public NoRequestsFoundException(String message) {
		super(message);
			}

}
