package com.couponsystem.dao;

import java.util.List;

import com.couponsystem.bean.Coupon;
import com.couponsystem.bean.Customer;

/**
 * @author Shay Ben Haroush
 *
 */

public interface Customer_CouponDAO {
	
	/*
	 * Data Access Object is an object that provides an abstract interface
	 * to the database. The DAO provides some specific data operations without exposing details
	 * of the database. Implementation is done by other classes - DBDAO which interacts with the Driver
	 * and with the Database through SQL queries.
	 */
	
	/* This method used to add record of relation between customer and coupon */
	void insertCustomer_Coupon(Customer customer, Coupon coupon) throws Exception;

	/* This method used to remove record of relation between customer and coupon - by 2 params */
	void removeCustomer_Coupon(Customer customer, Coupon coupon) throws Exception;

	/* This method used to remove record of relation between customer and coupon by customer object */
	void removeCustomer_Coupon(Customer customer) throws Exception;
	
	/* This method used to remove record of relation between customer and coupon - by coupon object */
	void removeCustomer_Coupon(Coupon coupon) throws Exception;
	
	/* This method used to get list of customersId by specific couponId */
	List<Long> getCustomerId(long couponId) throws Exception;

	/* This method used to get list of all customersId in this table */
	List<Long> getAllCustomersId() throws Exception;
	
	/* This method used to get list of couponsId by specific customerId */
	List<Long> getCouponId (long customerId) throws Exception;
	
	/* This method used to get list of all couponsId in this table */
	List<Long> getAllCouponsId() throws Exception;
}
