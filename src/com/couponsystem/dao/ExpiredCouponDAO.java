package com.couponsystem.dao;

import com.couponsystem.bean.Coupon;

/**
 * @author Shay Ben Haroush
 *
 */

/*
 * Data Access Object is an object that provides an abstract interface
 * to the database. The DAO provides some specific data operations without exposing details
 * of the database. Implementation is done by other classes - DBDAO which interacts with the Driver
 * and with the Database through SQL queries.
 */

public interface ExpiredCouponDAO {
	
	
	/* This method used to add record of coupon object which expired */
	void insertCoupon(Coupon coupon) throws Exception;

}
