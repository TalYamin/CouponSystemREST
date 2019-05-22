package com.couponsystem.exceptions;

/**
 * @author Shay Ben Haroush
 *
 */

/*
 * ConnectionException is an exception used to indicates exception which derived
 * from connection pool errors when try to receive or return connection to pool.
 */

public class ConnectionException extends Exception {

	/*
	 * Data Member which holds number of available connections when exception has
	 * been activated
	 */
	private int availableConnections;

	/*
	 * Full CTOR: there is using of super pattern in order to achieve message
	 * pattern from Exception superclass. there is using of string format in order
	 * to append more details to message.
	 */
	public ConnectionException(String message, int availableConnections) {
		super(String.format(message + "available Connections: %d", availableConnections));
		this.availableConnections = availableConnections;
	}

	/* Getter method to receive the value of available connections */
	public int getAvailableConnections() {
		return availableConnections;
	}

}
