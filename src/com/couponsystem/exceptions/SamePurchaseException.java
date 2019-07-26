package com.couponsystem.exceptions;

/**
 * @author Shay Ben Haroush
 *
 */

/*
 * SamePurchaseException is an exception used to indicates exception which
 * derived from trial of customer to purchase coupon with same id, he already
 * purchased it.
 */

public class SamePurchaseException extends Exception {

	/*
	 * Data Members which hold coupon id and customer id that activated the
	 * exception
	 */
	private long couponId;
	private long customerId;

	/*
	 * Full CTOR: there is using of super pattern in order to achieve message
	 * pattern from Exception superclass. there is using of string format in order
	 * to append more details to message.
	 */
	public SamePurchaseException(String message, long couponId, long customerId) {
		super(String.format(message + "couponId: %d, customerId: %d", couponId, customerId));
		this.couponId = couponId;
		this.customerId = customerId;
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
