package com.couponsystem.facade;

import java.util.Iterator;
import java.util.List;

import com.couponsystem.dao.CompanyDAO;
import com.couponsystem.dao.Company_CouponDAO;
import com.couponsystem.dao.CouponDAO;
import com.couponsystem.dao.CustomerDAO;
import com.couponsystem.dao.Customer_CouponDAO;
import com.couponsystem.dbdao.CompanyDBDAO;
import com.couponsystem.dbdao.Company_CouponDBDAO;
import com.couponsystem.dbdao.CouponDBDAO;
import com.couponsystem.dbdao.CustomerDBDAO;
import com.couponsystem.dbdao.Customer_CouponDBDAO;
import com.couponsystem.exceptions.CompanyExistsException;
import com.couponsystem.exceptions.CustomerExistsException;
import com.couponsystem.exceptions.NoDetailsFoundException;
import com.couponsystem.exceptions.ObjectNotFoundException;
import com.couponsystem.bean.Company;
import com.couponsystem.bean.Coupon;
import com.couponsystem.bean.Customer;
import com.couponsystem.utils.ClientType;

/**
 * @author Tal Yamin
 *
 */

/*
 * This class sets the business logic and actions for the Admin client. Methods
 * in this class based on following pattern: (1) Using Iterators in order to go
 * through and check all the list objects. (2) Checking relevant restrictions by
 * business logic. (3) Throwing relevant Exception when it activated.
 */

public class AdminUserFacade implements CouponClientFacade {

	/* Data members which hold client type and Access to DAO */
	private ClientType clientType;
	private CompanyDAO compAdminDAO;
	private CustomerDAO custAdminDAO;
	private CouponDAO coupAdminDAO;
	private Company_CouponDAO com_couAdminDAO;
	private Customer_CouponDAO cus_couAdminDAO;

	/* Full CTOR: sets the client type and DAO to DBDAO */
	public AdminUserFacade() {
		this.clientType = ClientType.ADMIN;
		this.compAdminDAO = new CompanyDBDAO();
		this.custAdminDAO = new CustomerDBDAO();
		this.coupAdminDAO = new CouponDBDAO();
		this.com_couAdminDAO = new Company_CouponDBDAO();
		this.cus_couAdminDAO = new Customer_CouponDBDAO();
	}

	// ---------------------------
	// Companies related methods
	// ---------------------------

	/*
	 * This method receive 1 parameter: company object.
	 * Inserts a new company in the companies table under the following
	 * restrictions: Can't insert a company with the same name. If the restriction
	 * is exceeded, CompanyExistsException activated. There is List which holds all
	 * the companies in DB, Using Iterator in order to go through and check all the
	 * list objects.
	 */
	public String insertCompany(Company company) throws Exception {
		try {

			List<Company> companies = compAdminDAO.getAllCompanies();

			Iterator<Company> i = companies.iterator();

			while (i.hasNext()) {
				Company current = i.next();
				if (company.getCompanyName().equals(current.getCompanyName())) {
					throw new CompanyExistsException("Admin failed to add company - this company already exists: ",
							company.getCompanyName());

				}
			}
			if (!i.hasNext()) {
				compAdminDAO.insertCompany(company);
				System.out.println("Admin added new company: " + company.getCompanyId());
				return "Admin added new company: " + company.getCompanyId();

			}

		} catch (CompanyExistsException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			throw new Exception("Admin failed to add company. companyId: " + company.getCompanyId());

		}
		return null;

	}

	/*
	 * This method receive 1 parameter: company ID.
	 * Removes a company and all its coupons from the database. Removing a company
	 * with this method impacts the following database tables: (a) Coupon table: if
	 * any coupon record has been created. (b) Company_Coupon table: if any
	 * company's coupon record has been created. (c) Customer_Coupon table: if any
	 * customer-coupon record has been created. (d) Company table. It is imperative
	 * we will remove all company's related data from these tables in order to be
	 * able to remove the company data. once we clear all related data it is safe to
	 * remove the company from the companies table. There are Lists which hold
	 * relevant objects. Using Iterator in order to go through and check all the
	 * list objects. If company id doesn't exist in DB, ObjectNotFoundException is
	 * activated.
	 */
	public String removeCompany(long companyId) throws Exception {

		try {

			/* Check if compnayId exist */
			List<Company> companies = compAdminDAO.getAllCompanies();
			Iterator<Company> i = companies.iterator();
			int flag = 0;
			while (i.hasNext()) {
				Company current = i.next();
				if (current.getCompanyId() == companyId) {
					flag = 1;
				}
			}
			if (!i.hasNext() && flag == 0) {
				throw new ObjectNotFoundException("companyId does not exist in system", 0, this.clientType, companyId);
			}

			/* get all coupons that belongs to company from Company_Coupon table */
			List<Long> coupons = com_couAdminDAO.getCouponId(companyId);

			/* Run on ID of coupons in loop */
			for (Long cId : coupons) {

				/*
				 * Get all Coupons objects that belongs to company and remove them from
				 * Customer_Coupon, Company_Coupon, Coupon tables
				 */
				List<Coupon> couponsToRemove = coupAdminDAO.getAllCoupons(cId);
				for (Coupon c : couponsToRemove) {
					List<Long> customersId = cus_couAdminDAO.getAllCustomersId();
					List<Long> companiesId = com_couAdminDAO.getAllCompaniesId();
					if (!customersId.isEmpty()) {
						cus_couAdminDAO.removeCustomer_Coupon(c);
					}
					if (!companiesId.isEmpty()) {
						com_couAdminDAO.removeCompany_Coupon(c);

					}
					coupAdminDAO.removeCoupon(c);
				}

			}
			/* Remove company from Company table */
			compAdminDAO.removeCompany(compAdminDAO.getCompany(companyId));
			return "Admin removed company successfully. companyId: " + companyId;
		} catch (ObjectNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			throw new Exception("Admin failed to remove company. companyId: " + companyId);
		}
		return null;

	}

	/*
	 * This method receive 3 parameters: company ID, new company password and new company email.
	 * Updates an existing company in the Company table under the following
	 * restrictions: 1. There is no option to update company name or company id 2.
	 * Can't update a company that has not been created first. If the restriction is
	 * exceeded, ObjectNotFoundException activated. There are Lists which hold
	 * relevant objects. Using Iterator in order to go through and check all the
	 * list objects.
	 */
	public String updateCompany(long companyId, String newCompanyPassword, String newCompanyEmail) throws Exception {
		try {

			/* Check if compnayId exist */
			List<Company> companies = compAdminDAO.getAllCompanies();
			Iterator<Company> i = companies.iterator();
			int flag = 0;
			while (i.hasNext()) {
				Company current = i.next();
				if (current.getCompanyId() == companyId) {
					flag = 1;
				}
			}
			if (!i.hasNext() && flag == 0) {
				throw new ObjectNotFoundException("companyId does not exist in system. ", 0, this.clientType,
						companyId);
			}

			/* Set new values to company object */
			Company company = compAdminDAO.getCompany(companyId);
			company.setCompanyPassword(newCompanyPassword);
			company.setCompanyEmail(newCompanyEmail);
			compAdminDAO.updateCompany(company);
			return "Admin updated company successfully. companyId: " + companyId;
		} catch (ObjectNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			throw new Exception("Admin failed to update company. companyId: " + companyId);
		}
		return null;
	}

	/*
	 * Retrieves a all companies in DB. If there are no records in DB,
	 * NoDetailsFoundException is activated. There are Lists which hold relevant
	 * objects.
	 */
	public List<Company> getAllCompanies() throws Exception {
		try {
			List<Company> companies = compAdminDAO.getAllCompanies();

			if (companies.isEmpty()) {
				throw new NoDetailsFoundException("Admin failed to get all companies - no details found", 0,
						this.clientType);
			}

			for (Company c : companies) {
				System.out.println(c);
			}
			return compAdminDAO.getAllCompanies();
		} catch (NoDetailsFoundException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			throw new Exception("Admin failed to get all companies");
		}
		return null;
	}

	/*
	 * This method receive 1 parameter: company ID.
	 * Retrieves a specific company using its ID.If there are no records in DB for
	 * this id, ObjectNotFoundException is activated.There are Lists which hold
	 * relevant objects. Using Iterator in order to go through and check all the
	 * list objects.
	 */
	public Company getCompany(long companyId) throws Exception {
		try {

			/* Check if compnayId exist */
			List<Company> companies = compAdminDAO.getAllCompanies();
			Iterator<Company> i = companies.iterator();
			int flag = 0;
			while (i.hasNext()) {
				Company current = i.next();
				if (current.getCompanyId() == companyId) {
					flag = 1;
				}
			}
			if (!i.hasNext() && flag == 0) {
				throw new ObjectNotFoundException("companyId does not exist in system. ", 0, this.clientType,
						companyId);
			}

			System.out.println(compAdminDAO.getCompany(companyId));
			return compAdminDAO.getCompany(companyId);
		} catch (ObjectNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			throw new Exception("Admin failed to get a company. companyId: " + companyId);
		}
		return null;
	}

	// --------------------------
	// Customers related methods
	// --------------------------


	/*
	 * This method receive 1 parameter: customer object.
	 * Inserts a new customer in the customers table under the following
	 * restrictions: Can't insert a customer with the same name. If the restriction
	 * is exceeded, CustomerExistsException activated. There is List which holds all
	 * the customers in DB, Using Iterator in order to go through and check all the
	 * list objects.
	 */
	public String insertCustomer(Customer customer) throws Exception {
		try {

			List<Customer> customers = custAdminDAO.getAllCustomers();

			/* Check for same name of customer */
			Iterator<Customer> i = customers.iterator();
			while (i.hasNext()) {
				Customer current = i.next();
				if (customer.getCustomerName().equals(current.getCustomerName())) {
					throw new CustomerExistsException("Admin failed to add customer - this customer already exists: ",
							customer.getCustomerName());
				}
			}

			if (!i.hasNext()) {
				custAdminDAO.insertCustomer(customer);
				System.out.println("Admin added new custoemr: " + customer.getCustomerId());
				return ("Admin added new custoemr: " + customer.getCustomerId());
			}
		} catch (CustomerExistsException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			throw new Exception("Admin failed to add customer. customerId: " + customer.getCustomerId());
		}

		return null;
	}

	/*
	 * This method receive 1 parameter: customer ID.
	 * Removes a customer and all its coupons from the database. Removing a customer
	 * with this method impacts the following database tables: (a) Customer_Coupon
	 * table: if any customer-coupon record has been created. (b) Customer table. It
	 * is imperative we will remove all customer's related data from these tables in
	 * order to be able to remove the customer data. once we clear all related data
	 * it is safe to remove the customer from the Customer table. There are Lists
	 * which hold relevant objects. Using Iterator in order to go through and check
	 * all the list objects. If customer id doesn't exist in DB,
	 * ObjectNotFoundException is activated.
	 */
	public String removeCustomer(long customerId) throws Exception {

		try {

			/* check if customerId exist */
			List<Customer> customers = custAdminDAO.getAllCustomers();
			Iterator<Customer> i = customers.iterator();
			int flag = 0;
			while (i.hasNext()) {
				Customer current = i.next();
				if (current.getCustomerId() == customerId) {
					flag = 1;
				}
			}
			if (!i.hasNext() && flag == 0) {
				throw new ObjectNotFoundException("customerId does not exist in system. ", 0, this.clientType,
						customerId);
			}

			/*
			 * Get all lists of coupons which related to customer, if records exist - remove
			 * them from Customer_Coupon table
			 */
			Customer customer = custAdminDAO.getCustomer(customerId);
			List<Long> couponsId = cus_couAdminDAO.getCouponId(customer.getCustomerId());
			if (!couponsId.isEmpty()) {
				cus_couAdminDAO.removeCustomer_Coupon(customer);
				custAdminDAO.removeCustomer(customer);
			}
			/* After all coupons removed, remove data from Customer table */
			custAdminDAO.removeCustomer(customer);
			return "Admin removed customer successfully. customerId: " + customerId;

		} catch (ObjectNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			throw new Exception("Admin failed to remove customer.  customerId: " + customerId);
		}

		return null;
	}

	/*
	 * This method receive 2 parameters: customer ID and new customer password.
	 * Updates an existing customer in the Customer table under the following
	 * restrictions: 1. There is no option to update customer name or customer id 2.
	 * Can't update a customer that has not been created first. If the restriction
	 * is exceeded, ObjectNotFoundException activated. There are Lists which hold
	 * relevant objects. Using Iterator in order to go through and check all the
	 * list objects.
	 */
	public String updateCustomer(long customerId, String newCustomerPassword) throws Exception {

		try {

			/* Check if customerId exist */
			List<Customer> customers = custAdminDAO.getAllCustomers();
			Iterator<Customer> i = customers.iterator();
			int flag = 0;
			while (i.hasNext()) {
				Customer current = i.next();
				if (current.getCustomerId() == customerId) {
					flag = 1;
				}
			}
			if (!i.hasNext() && flag == 0) {
				throw new ObjectNotFoundException("customerId does not exist in system. ", 0, this.clientType,
						customerId);
			}

			/* Set new values to customer object */
			Customer customer = custAdminDAO.getCustomer(customerId);
			customer.setCustomerPassword(newCustomerPassword);
			custAdminDAO.updateCustomer(customer);
			return "Admin updated customer successfully. customerId: " + customerId;
		} catch (ObjectNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			throw new Exception("Admin failed to update customer. customerId: " + customerId);
		}
		return null;
	}

	/*
	 * Retrieves a all customers in DB. If there are no records in DB,
	 * NoDetailsFoundException is activated. There are Lists which hold relevant
	 * objects.
	 */
	public List<Customer> getAllCustomers() throws Exception {
		try {
			List<Customer> customers = custAdminDAO.getAllCustomers();

			if (customers.isEmpty()) {
				throw new NoDetailsFoundException("Admin failed to get all customers - no details found", 0,
						this.clientType);
			}

			for (Customer c : customers) {
				System.out.println(c);
			}
			return custAdminDAO.getAllCustomers();
		} catch (NoDetailsFoundException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			throw new Exception("Admin failed to get all customers");
		}
		return null;
	}

	/*
	 * This method receive 1 parameter: customer ID.
	 * Retrieves a specific customer using its ID.If there are no records in DB for
	 * this id, ObjectNotFoundException is activated.There are Lists which hold
	 * relevant objects. Using Iterator in order to go through and check all the
	 * list objects.
	 */
	public Customer getCustomer(long customerId) throws Exception {
		try {

			/* Check if customerId exist */
			List<Customer> customers = custAdminDAO.getAllCustomers();
			Iterator<Customer> i = customers.iterator();
			int flag = 0;
			while (i.hasNext()) {
				Customer current = i.next();
				if (current.getCustomerId() == customerId) {
					flag = 1;
				}
			}
			if (!i.hasNext() && flag == 0) {
				throw new ObjectNotFoundException("customerId does not exist in system. ", 0, this.clientType,
						customerId);
			}

			System.out.println(custAdminDAO.getCustomer(customerId));
			return custAdminDAO.getCustomer(customerId);
		} catch (ObjectNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			throw new Exception("Admin failed to get a customer. customerId: " + customerId);
		}
		return null;
	}

	/*
	 * NOT IN USE - login process performed in the CouponSystem class. Override from
	 * CouponClientFacade interface - make it available to return facade for login
	 * method.
	 */
	@Override
	public CouponClientFacade login(String name, String password, ClientType clientType) throws Exception {
		return null;

	}

}
