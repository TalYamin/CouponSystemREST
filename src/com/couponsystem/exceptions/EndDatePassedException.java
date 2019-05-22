package com.couponsystem.exceptions;

import java.time.LocalDate;

/**
 * @author Shay Ben Haroush
 *
 */

/*
 * EndDatePassedException is an exception used to indicates exception which
 * derived from trial to add or update coupon with end date which already
 * passed.
 */

public class EndDatePassedException extends Exception {

	/*
	 * Data Members which hold coupon end date, coupon id and company id that
	 * activated the exception
	 */
	private String endDate;
	private long couponId;
	private long companyId;

	/*
	 * Full CTOR: there is using of super pattern in order to achieve message
	 * pattern from Exception superclass. there is using of string format in order
	 * to append more details to message.
	 */
	public EndDatePassedException(String message, String endDate, long couponId, long companyId) {
		super(String.format(message + "endDate: %s, couponId: %d, companyId: %d", endDate, couponId, companyId));
		this.endDate = endDate;
		this.couponId = couponId;
		this.companyId = companyId;
	}

	/* Getter method to receive the value of coupon end date */
	public String getEndDate() {
		return this.endDate;
	}

	/* Getter method to receive the value of coupon id */
	public long getCouponId() {
		return this.couponId;
	}

	/* Getter method to receive the value of company id */
	public long getCompanyId() {
		return this.companyId;
	}

}
