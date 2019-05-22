package com.couponsystem.exceptions;

/**
 * @author Shay Ben Haroush
 *
 */

/*
 * CouponExistsException is an exception used to indicates exception which
 * derived from trial to add coupon which already exists to DB.
 */

public class CouponExistsException extends Exception {

	/*
	 * Data Members which hold coupon title and company id that activated the
	 * exception
	 */
	private String title;
	private long companyId;

	/*
	 * Full CTOR: there is using of super pattern in order to achieve message
	 * pattern from Exception superclass. there is using of string format in order
	 * to append more details to message.
	 */
	public CouponExistsException(String message, String title, long companyId) {
		super(String.format(message + "couponTitle: %s, companyId: %d", title, companyId));
		this.title = title;
		this.companyId = companyId;
	}

	/* Getter method to receive the value of coupon title */
	public String getTitle() {
		return this.title;
	}

	/* Getter method to receive the value of company id */
	public long getCompanyId() {
		return this.companyId;
	}

}
