package com.couponsystem.exceptions;

/**
 * @author Shay Ben Haroush
 *
 */

/*
 * OutOfStockException is an exception used to indicates exception which derived
 * from trial of customer to purchase coupon with amount 0 and it out of stock.
 */

public class OutOfStockException extends Exception {

	/*
	 * Data Members which hold coupon amount, coupon id and customer id that
	 * activated the exception
	 */
	private int amount;
	private long couponId;
	private long customerId;

	/*
	 * Full CTOR: there is using of super pattern in order to achieve message
	 * pattern from Exception superclass. there is using of string format in order
	 * to append more details to message.
	 */
	public OutOfStockException(String message, int amount, long couponId, long customerId) {
		super(String.format(message + "amount: %d, couponId: %d, customerId: %d", amount, couponId, customerId));
		this.amount = amount;
		this.couponId = couponId;
		this.customerId = customerId;
	}

	/* Getter method to receive the value of coupon amount */
	public int getAmount() {
		return amount;
	}

	/* Getter method to receive the value of coupon id */
	public long getCouponId() {
		return couponId;
	}

	/* Getter method to receive the value of customer id */
	public long getCustomerId() {
		return customerId;
	}

}
