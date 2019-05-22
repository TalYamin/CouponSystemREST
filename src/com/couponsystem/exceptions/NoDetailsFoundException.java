package com.couponsystem.exceptions;

import com.couponsystem.utils.ClientType;

/**
 * @author Shay Ben Haroush
 *
 */

/*
 * NoDetailsFoundException is an exception used to indicates exception which
 * derived from trial to get data from DB when records don't exist.
 */

public class NoDetailsFoundException extends Exception {

	/*
	 * Data Members which hold client id and client type that activated the
	 * exception
	 */
	private long clientId;
	private ClientType clientType;

	/*
	 * Full CTOR: there is using of super pattern in order to achieve message
	 * pattern from Exception superclass.
	 */
	public NoDetailsFoundException(String message, long clientId, ClientType clientType) {
		super(message);
		this.clientId = clientId;
		this.clientType = clientType;
	}

	/* Getter method to receive the value of client id */
	public long getClientId() {
		return clientId;
	}

	/* Getter method to receive the value of client type */
	public ClientType getClientType() {
		return clientType;
	}

}
