package com.thank.user.model;

public class LoginException extends RuntimeException {
	private static final long serialVersionUID = 5090833100879147288L;
	public LoginException(String msg) {
		super(msg);
	}
}
