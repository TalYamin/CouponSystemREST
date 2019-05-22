package com.couponsystem.dao;

import java.util.List;

import com.couponsystem.bean.Company;

/**
 * @author Shay Ben Haroush
 *
 */

public interface CompanyDAO {
	
	/*
	 * Data Access Object is an object that provides an abstract interface
	 * to the database. The DAO provides some specific data operations without exposing details
	 * of the database. Implementation is done by other classes - DBDAO which interacts with the Driver
	 * and with the Database through SQL queries.
	 */
	
	/* This method used to add record of company object */
	void insertCompany(Company company) throws Exception;

	/* This method used to remove record of company object */
	void removeCompany(Company company) throws Exception;

	/* This method used to update record of company object */
	void updateCompany(Company company) throws Exception;

	/* This method used to get specific record of company object */
	Company getCompany(long companyId) throws Exception;

	/* This method used to get all companies records */
	List<Company> getAllCompanies() throws Exception;


}
