package com.couponsystem.exceptions;

import com.couponsystem.utils.ClientType;

/**
 * @author Shay Ben Haroush
 *
 */

/*
 * ObjectNotFoundException is an exception used to indicates exception which
 * derived from trial to get data of specific object from DB when records don't
 * exist.
 */

public class ObjectNotFoundException extends Exception {

	/*
	 * Data Members which hold client id, client type and object id that activated
	 * the exception
	 */
	private long clientId;
	private ClientType clientType;
	private long objectId;

	/*
	 * Full CTOR: there is using of super pattern in order to achieve message
	 * pattern from Exception superclass. there is using of string format in order
	 * to append more details to message.
	 */
	public ObjectNotFoundException(String message, long clientId, ClientType clientType, long objectId) {
		super(String.format(message + " objectId: %d", objectId));
		this.clientId = clientId;
		this.clientType = clientType;
		this.objectId = objectId;
	}

	/* Getter method to receive the value of client id */
	public long getClientId() {
		return clientId;
	}

	/* Getter method to receive the value of client type */
	public ClientType getClientType() {
		return clientType;
	}

	/* Getter method to receive the value of object id */
	public long getObjectId() {
		return objectId;
	}

}
