package com.couponsystem.utils;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RequestStatus {
	
	private boolean success;
	
	public RequestStatus() {
		
	}

	public RequestStatus(boolean success) {
		this.success = success;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	@Override
	public String toString() {
		return "RequestStatus [success=" + success + "]";
	}
	
	
	
	

}
