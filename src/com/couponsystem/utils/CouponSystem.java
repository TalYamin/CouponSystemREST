package com.couponsystem.utils;

import java.util.Iterator;
import java.util.List;

import com.couponsystem.dao.CompanyDAO;
import com.couponsystem.dao.CustomerDAO;
import com.couponsystem.db.DataBase;
import com.couponsystem.dbdao.CompanyDBDAO;
import com.couponsystem.dbdao.CustomerDBDAO;
import com.couponsystem.exceptions.DailyTaskException;
import com.couponsystem.exceptions.LoginCouponSystemException;
import com.couponsystem.facade.AdminUserFacade;
import com.couponsystem.facade.CompanyUserFacade;
import com.couponsystem.facade.CouponClientFacade;
import com.couponsystem.facade.CustomerUserFacade;
import com.couponsystem.bean.Company;
import com.couponsystem.bean.Customer;
import com.couponsystem.utils.ClientType;

/**
 * @author Shay Ben Haroush and Tal Yamin
 *
 */

/*
 * This is the coupon system Single-tone class which: (1) Allows only one
 * Instance to be created and only within the CouponSystem Class. other classes
 * can interact with this instance via getInstance() Method. (2) Allows the
 * different clients to Login into the system and perform different actions
 * according to the Client type. (3) Getting instance of ConnectionPool (4)
 * Creating and running the DailyCouponExpirationTask. (5) Contains the shutdown
 * method which shuts the coupon system.
 */

public class CouponSystem {

	/* the CouponSystem one instance */
	private static CouponSystem instance = new CouponSystem();

	/* the DAO objects for Login method */
	private CompanyDAO companySystemDAO = new CompanyDBDAO();
	private CustomerDAO customerSystemDAO = new CustomerDBDAO();

	/* ConnectionPool object */
	private ConnectionPool connectionPool;

	/* DailyCouponExpirationTask object */
	private static DailyCouponExpirationTask dailyCouponExpirationTask;

	/*
	 * Private CTOR (SingelTone design pattern). only one Instance can be created,
	 * and only within the CouponSystem Class. other classes can interact with this
	 * instance via getInstance() Method. This CTOR includes Data Base building.
	 */
	private CouponSystem() {
		System.out.println("Welcome to Coupon System");
		try {
			DataBase.BuildDB();
		} catch (Exception e) {
			System.out.println("DB already exists");
		}

	}

	/*
	 * allows to get the single instance variable that was created in the singleton
	 * class. when the instance received: ConnectionPool and
	 * DailyCouponExpirationTask are activated.
	 */
	public static CouponSystem getInstance() throws Exception {
		try {
			ConnectionPool connectionPool = ConnectionPool.getInstance();
//			dailyCouponExpirationTask = new DailyCouponExpirationTask();
//			dailyCouponExpirationTask.startTask();
			return instance;
		} catch (DailyTaskException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			throw new Exception("DailyCouponExpirationTask failed to start");
		}
		return instance;

	}

	/*
	 * Login to system method: This method receive 3 parameters: userName, password
	 * and clientType. According to clientType, the method check the validity of the
	 * parameters values. If the values valid - the method return client facade. If
	 * the values not valid - LoginCouponSystemException is activated.
	 */

	public CouponClientFacade login(String userName, String password, ClientType clientType) throws Exception {

		try {
			switch (clientType) {

			case ADMIN:

				/* Check the validity of the parameters values */
				if (userName.equals("admin") && password.equals("1234")) {
					AdminUserFacade adminF = new AdminUserFacade();
					System.out.println("Admin logged in to system");
					return adminF;
				} else {
					throw new LoginCouponSystemException("invalid details for Admin user. ", userName, password,
							clientType);
				}

			case COMPANY:

				/* Take the list of companies from DB */
				List<Company> companies = companySystemDAO.getAllCompanies();
				Iterator<Company> i = companies.iterator();

				/* For any company in DB check the validity of the parameters values */
				while (i.hasNext()) {
					Company current = i.next();
					if (current.getCompanyName().equals(userName) && current.getCompanyPassword().equals(password)) {
						CompanyUserFacade companyF = new CompanyUserFacade(current);
						System.out.println("Company " + current.getCompanyName() + " logged in to system");
						return companyF;
					} else if (!i.hasNext()) {
						throw new LoginCouponSystemException("invalid details for Company user. ", userName, password,
								clientType);
					}
				}

			case CUSTOMER:

				/* Take the list of customers from DB */
				List<Customer> customers = customerSystemDAO.getAllCustomers();
				Iterator<Customer> it = customers.iterator();

				/* For any customer in DB check the validity of the parameters values */
				while (it.hasNext()) {
					Customer current = it.next();
					if (current.getCustomerName().equals(userName) && current.getCustomerPassword().equals(password)) {
						CustomerUserFacade customerF = new CustomerUserFacade(current);
						System.out.println("Customer " + current.getCustomerName() + " logged in to system");
						return customerF;
					} else if (!it.hasNext()) {
						throw new LoginCouponSystemException("invalid details for Customer user. ", userName, password,
								clientType);
					}
				}

			}
		} catch (LoginCouponSystemException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			throw new Exception("Login failed");
		}
		return null;
	}

	/*
	 * Shutdown CouponSystem:
	 * (1) Close all connections in connectionPool.
	 * (2) Stop DailyCouponExpirationTask.
	 */
	public void shutdown() {
		try {
			connectionPool = ConnectionPool.getInstance();
			connectionPool.closeAllConnections();
	//		dailyCouponExpirationTask.stopTask();
			System.out.println("Shutdown...");
		} catch (Exception e) {
			System.out.println("DailyCouponExpirationTask failed to stop");
		}
	}

}
