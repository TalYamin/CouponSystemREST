package com.couponsystem.exceptions;

/**
 * @author Shay Ben Haroush
 *
 */

/*
 * CompanyExistsException is an exception used to indicates exception which
 * derived from trial to add company which already exists to DB.
 */

public class CompanyExistsException extends Exception {

	/* Data Member which holds company name that activated the exception */
	private String comanyName;

	/*
	 * Full CTOR: there is using of super pattern in order to achieve message pattern
	 * from Exception superclass. there is using of string format in order to append
	 * more details to message.
	 */
	public CompanyExistsException(String message, String companyName) {
		super(String.format(message + "%s", companyName));
		this.comanyName = companyName;
	}

	/* Getter method to receive the value of company name */
	public String getCompanyName() {
		return this.comanyName;
	}
}
