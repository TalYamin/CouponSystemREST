package com.couponsystem.exceptions;

import com.couponsystem.utils.ClientType;

/**
 * @author Shay Ben Haroush
 *
 */

/*
 * NotBelongsException is an exception used to indicates exception which derived
 * from trial of company to get data from DB which not belongs to it.
 */

public class NotBelongsException extends Exception {

	/*
	 * Data Members which hold company id, client type and coupon id that activated
	 * the exception
	 */
	private long comapnyId;
	private String clientType;
	private long couponId;

	/*
	 * Full CTOR: there is using of super pattern in order to achieve message
	 * pattern from Exception superclass. there is using of string format in order
	 * to append more details to message.
	 */
	public NotBelongsException(String message, long comapnyId, String clientType, long couponId) {
		super(String.format(message + "comapnyId: %d, clientType: %s, couponId: %d", comapnyId, clientType, couponId));
		this.comapnyId = comapnyId;
		this.clientType = clientType;
		this.couponId = couponId;
	}

	/* Getter method to receive the value of company id */
	public long getComapnyId() {
		return comapnyId;
	}

	/* Getter method to receive the value of client type */
	public String getClientType() {
		return clientType;
	}

	/* Getter method to receive the value of coupon id */
	public long getCouponId() {
		return couponId;
	}

}
