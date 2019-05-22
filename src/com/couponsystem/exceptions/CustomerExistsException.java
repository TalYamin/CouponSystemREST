package com.couponsystem.exceptions;

/**
 * @author Shay Ben Haroush
 *
 */

/*
 * CustomerExistsException is an exception used to indicates exception which
 * derived from trial to add customer which already exists to DB.
 */

public class CustomerExistsException extends Exception {

	/* Data Member which holds customer name that activated the exception */
	private String customerName;

	/*
	 * Full CTOR: there is using of super pattern in order to achieve message
	 * pattern from Exception superclass. there is using of string format in order
	 * to append more details to message.
	 */
	public CustomerExistsException(String message, String customerName) {
		super(String.format(message + "customerName: %s", customerName));
		this.customerName = customerName;
	}

	/* Getter method to receive the value of customer name */
	public String getCustomerName() {
		return this.customerName;
	}

}
