package com.sena.parking.exception;

@SuppressWarnings("serial")
public class BusinessRuleException extends RuntimeException {

	public BusinessRuleException(String message) {
		super(message);
	}

}
