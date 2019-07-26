package com.couponsystem.utils;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RequestStatus {
	
	private boolean success;
	private String message;
	
	public RequestStatus() {
		
	}

	public RequestStatus(boolean success, String message) {
		this.success = success;
		this.message = message;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "RequestStatus [success=" + success + ", message=" + message + "]";
	}

	
	
	
	
	

}
