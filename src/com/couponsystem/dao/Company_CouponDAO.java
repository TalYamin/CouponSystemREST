package com.couponsystem.dao;

import java.util.List;

import com.couponsystem.bean.Company;
import com.couponsystem.bean.Coupon;




/**
 * @author Shay Ben Haroush
 *
 */

public interface Company_CouponDAO {
	
	/*
	 * Data Access Object is an object that provides an abstract interface
	 * to the database. The DAO provides some specific data operations without exposing details
	 * of the database. Implementation is done by other classes - DBDAO which interacts with the Driver
	 * and with the Database through SQL queries.
	 */

	
	/* This method used to add record of relation between company and coupon */
	void insertCompany_Coupon(Company company, Coupon coupon) throws Exception;

	/* This method used to remove record of relation between company and coupon by 2 params */
	void removeCompany_Coupon(Company company, Coupon coupon) throws Exception;

	/* This method used to remove record of relation between company and coupon by company object */
	void removeCompany_Coupon(Company company) throws Exception;
	
	/* This method used to remove record of relation between company and coupon by coupon object */
	void removeCompany_Coupon(Coupon coupon) throws Exception;
	
	/* This method used to get list of companiesId by specific couponId */
	List<Long> getCompanyId(long couponId) throws Exception;

	/* This method used to get list of all companiesId in this table */
	List<Long> getAllCompaniesId() throws Exception;
	
	/* This method used to get list of couponsId by specific companyId */
	List<Long> getCouponId (long companyId) throws Exception;
	
	/* This method used to get list of all couponsId in this table */
	List<Long> getAllCouponsId() throws Exception;
}
