package com.couponsystem.dao;

import java.util.List;

import com.couponsystem.bean.Customer;

/**
 * @author Shay Ben Haroush
 *
 */

public interface CustomerDAO {
	
	/*
	 * Data Access Object is an object that provides an abstract interface
	 * to the database. The DAO provides some specific data operations without exposing details
	 * of the database. Implementation is done by other classes - DBDAO which interacts with the Driver
	 * and with the Database through SQL queries.
	 */

	/* This method used to add record of customer object */
	void insertCustomer(Customer customer) throws Exception;

	/* This method used to remove record of customer object */
	void removeCustomer(Customer customer) throws Exception;

	/* This method used to update record of customer object */
	void updateCustomer(Customer customer) throws Exception;

	/* This method used to get specific record of customer object */
	Customer getCustomer(long customerId) throws Exception;

	/* This method used to get all customers records */
	List<Customer> getAllCustomers() throws Exception;

}
