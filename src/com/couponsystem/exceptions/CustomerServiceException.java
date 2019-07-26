package com.couponsystem.exceptions;

public class CustomerServiceException extends Exception {
	
	private long customerId;
	private String customerName;
	private String clientType;
	private long couponId;

	public CustomerServiceException(String message, long customerId, String customerName, String clientType,long couponId) {
		super(String.format(message + "customer id: %d, customer name: %s, client type: %s, coupon id: %d ", customerId, customerName,
				clientType, couponId));
		this.customerId = customerId;
		this.customerName = customerName;
		this.clientType = clientType;
		this.couponId = couponId;
	}
	
	public CustomerServiceException(String message) {
		super(message);
	}

	public long getCustomerId() {
		return customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public String getClientType() {
		return clientType;
	}

	public long getCouponId() {
		return couponId;
	}
	
	
}
