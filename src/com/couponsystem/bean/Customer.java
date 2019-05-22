package com.couponsystem.bean;


/**
 * @author Shay Ben Haroush
 *
 */

/*
 * This class sets Customer type object.
 */


public class Customer {

	/* Data members of Customer */
	private long customerId;
	private String customerName;
	private String customerPassword;
	

	
	/* Empty CTOR Customer */
	public Customer(){
		
	}

	//Full CTOR Company: sets the customerId, customerName, customerPassword */
	public Customer(long customerId, String customerName, String customerPassword) {
		setCustomerId(customerId);
		setCustomerName(customerName);
		setCustomerPassword(customerPassword);
	}

	
	/* Getter method to receive the value of customer id */
	public long getCustomerId() {
		return this.customerId;
	}

	/* Setter method to set the value of customer id */
	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	/* Getter method to receive the value of customer name */
	public String getCustomerName() {
		return this.customerName;
	}

	/* Setter method to set the value of customer name */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	/* Getter method to receive the value of customer password */
	public String getCustomerPassword() {
		return this.customerPassword;
	}
	
	/* Setter method to set the value of customer password */
	public void setCustomerPassword(String customerPassword) {
		this.customerPassword = customerPassword;
	}


	/* toString method of Company - allows pattern to print*/
	@Override
	public String toString() {
		return "Customer [customerId=" + this.getCustomerId() + ", customerName=" + this.getCustomerName()
				+ ", customerPassword=" + this.getCustomerPassword() + "]";
	}

}