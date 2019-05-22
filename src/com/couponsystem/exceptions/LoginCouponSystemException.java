package com.couponsystem.exceptions;

import com.couponsystem.utils.ClientType;

/**
 * @author Shay Ben Haroush
 *
 */

/*
 * LoginCouponSystemException is an exception used to indicates exception which
 * derived from trial to login to system with wrong details.
 */

public class LoginCouponSystemException extends Exception {

	/*
	 * Data Members which hold user name, password and client type that activated
	 * the exception
	 */
	private String userName;
	private String password;
	private ClientType clientType;

	/*
	 * Full CTOR: there is using of super pattern in order to achieve message
	 * pattern from Exception superclass. there is using of string format in order
	 * to append more details to message.
	 */
	public LoginCouponSystemException(String message, String userName, String password, ClientType clientType) {
		super(String.format(message + "userName: %s, password: %s, clientType: %s", userName, password,
				clientType.toString()));
		this.userName = userName;
		this.password = password;
		this.clientType = clientType;
	}

	/* Getter method to receive the value of user name */
	public String getUserName() {
		return userName;
	}

	/* Getter method to receive the value of password */
	public String getPassword() {
		return password;
	}

	/* Getter method to receive the value of client type */
	public ClientType getClientType() {
		return clientType;
	}

}
