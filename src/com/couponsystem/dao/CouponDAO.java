package com.couponsystem.dao;

import java.util.List;

import com.couponsystem.bean.Coupon;

/**
 * @author Shay Ben Haroush
 *
 */

public interface CouponDAO {

	/*
	 * Data Access Object is an object that provides an abstract interface
	 * to the database. The DAO provides some specific data operations without exposing details
	 * of the database. Implementation is done by other classes - DBDAO which interacts with the Driver
	 * and with the Database through SQL queries.
	 */

	/* This method used to add record of coupon object */
	void insertCoupon(Coupon coupon) throws Exception;

	/* This method used to remove record of coupon object */
	void removeCoupon(Coupon coupon) throws Exception;

	/* This method used to update record of coupon object */
	void updateCoupon(Coupon coupon) throws Exception;

	/* This method used to update record of coupon object - only activation */
	void updateNoActiveCoupon(Coupon coupon) throws Exception;
	
	/* This method used to get specific record of coupon object */
	Coupon getCoupon(long couponId) throws Exception;

	/* This method used to get all coupons records */
	List<Coupon> getAllCoupons() throws Exception;

	/* This method used to get all coupons by couponId */
	List<Coupon> getAllCoupons(long couponId) throws Exception;

	/* This method used to get all coupons by couponId and typeName */
	List<Coupon> getAllCouponsByType(long couponId, String typeName) throws Exception;

	/* This method used to get all coupons by couponId and price limit */
	List<Coupon> getAllCouponsByPrice(long couponId, double priceTop) throws Exception;

	/* This method used to get all coupons by couponId and date limit */
	List<Coupon> getAllCouponsByDate(long couponId, String untilDate) throws Exception;
}
