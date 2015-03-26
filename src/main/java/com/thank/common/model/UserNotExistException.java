package com.thank.common.model;
/***
 * Login Exception
 * @author fenwang
 *
 */
public class UserNotExistException extends RuntimeException {

	private static final long serialVersionUID = 3037965827941381626L;

	public UserNotExistException(String msg) {
		super(msg);
	}
}
