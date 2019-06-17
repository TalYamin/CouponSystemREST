package com.couponsystem.exceptions;

import com.couponsystem.utils.ClientType;

public class CompanyServiceException extends Exception {

	private long companyId;
	private String companyName;
	private String clientType;
	private long couponId;

	public CompanyServiceException(String message, long companyId, String companyName, String clientType,
			long couponId) {
		super(String.format(message + "company id: %d, company name: %s, client type: %s, coupon id: %d ", companyId,
				companyName, clientType, couponId));
		this.companyId = companyId;
		this.companyName = companyName;
		this.clientType = clientType;
		this.couponId = couponId;
	}

	public CompanyServiceException(String message) {
		super(message);
	}

	public long getCompanyId() {
		return companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public String getClientType() {
		return clientType;
	}

	public long getCouponId() {
		return couponId;
	}

}
