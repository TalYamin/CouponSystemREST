package com.couponsystem.exceptions;

import java.time.LocalDate;

/**
 * @author Shay Ben Haroush
 *
 */

/*
 * CouponExpiredException is an exception used to indicates exception which
 * derived from trial of customer to purchase coupon with end date which already
 * expired.
 */

public class CouponExpiredException extends Exception {

	/*
	 * Data Members which holds coupon end date, coupon id and customer id that
	 * activated the exception
	 */
	private String endDate;
	private long couponId;
	private long customerId;

	/*
	 * Full CTOR: there is using of super pattern in order to achieve message pattern
	 * from Exception superclass. there is using of string format in order to append
	 * more details to message.
	 */
	public CouponExpiredException(String message, String endDate, long couponId, long customerId) {
		super(String.format(message + "endDate: %s, couponId: %d, customerId: %d", endDate, couponId, customerId));
		this.endDate = endDate;
		this.couponId = couponId;
		this.customerId = customerId;
	}

	/* Getter method to receive the value of coupon end date */
	public String getEndDate() {
		return this.endDate;
	}

	/* Getter method to receive the value of coupon id */
	public long getCouponId() {
		return this.couponId;
	}

	/* Getter method to receive the value of customer id */
	public long getCustomerId() {
		return this.customerId;
	}

}
