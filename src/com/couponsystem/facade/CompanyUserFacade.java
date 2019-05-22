package com.couponsystem.facade;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.couponsystem.dao.CompanyDAO;
import com.couponsystem.dao.Company_CouponDAO;
import com.couponsystem.dao.CouponDAO;
import com.couponsystem.dao.Customer_CouponDAO;
import com.couponsystem.dbdao.CompanyDBDAO;
import com.couponsystem.dbdao.Company_CouponDBDAO;
import com.couponsystem.dbdao.CouponDBDAO;
import com.couponsystem.dbdao.Customer_CouponDBDAO;
import com.couponsystem.exceptions.CouponExistsException;
import com.couponsystem.exceptions.EndDatePassedException;
import com.couponsystem.exceptions.NoDetailsFoundException;
import com.couponsystem.exceptions.NotBelongsException;
import com.couponsystem.exceptions.ObjectNotFoundException;
import com.couponsystem.bean.Company;
import com.couponsystem.bean.Coupon;
import com.couponsystem.utils.ClientType;
import com.couponsystem.utils.DateConverterUtil;

/**
 * @author Tal Yamin
 *
 */

/*
 * This class sets the business logic and actions for the Company client.
 * Methods in this class based on following pattern: (1) Using Iterators in
 * order to go through and check all the list objects. (2) Checking relevant
 * restrictions by business logic. (3) Throwing relevant Exception when it
 * activated.
 */

public class CompanyUserFacade implements CouponClientFacade {

	/* Data members which hold company object, client type and Access to DAO */
	private Company company;
	private ClientType clientType;
	private CompanyDAO compCompanyDAO;
	private CouponDAO coupCompanyDAO;
	private Company_CouponDAO com_couCompany;
	private Customer_CouponDAO cus_couCompany;


	/* Empty CTOR of CompanyUserFacade */
	public CompanyUserFacade() {

	}

	/*
	 * Full CTOR: Receive 1 parameter: company object. Sets specific company which
	 * received, client type and DAO to DBDAO
	 */
	public CompanyUserFacade(Company company) throws Exception {

		this.company = company;
		this.clientType = ClientType.COMPANY;
		this.compCompanyDAO = new CompanyDBDAO();
		this.coupCompanyDAO = new CouponDBDAO();
		this.com_couCompany = new Company_CouponDBDAO();
		this.cus_couCompany = new Customer_CouponDBDAO();
	}

	/*
	 * This method receive 1 parameter: coupon object. Inserts a new coupon in the
	 * Coupon table and Company_Coupon table under the following restrictions: 1.
	 * Can't insert coupon with amount 0. 2. Can't Insert coupon with end date which
	 * already passed. 3. Can't insert coupon with title which already exists in DB.
	 * If the restrictions is exceeded, one of the relevant exceptions activated:
	 * EndDatePassedException, CouponExistsException. There is List which holds all
	 * the relevant objects from DB, Using Iterator in order to go through and check
	 * all the list objects.
	 */
	public void insertCoupon(Coupon coupon) throws Exception {
		try {

			/* Check if amount is 0 */
			if (coupon.getAmount() < 1) {
				throw new Exception(
						"Company failed to add coupon - wrong amount: 0, couponId: " + coupon.getCouponId());
			}

			/* Check if the end date already passed */
			if (coupon.getEndDate().isBefore(LocalDate.now())) {
				throw new EndDatePassedException("Company failed to add coupon - the end date already passed. ",
						DateConverterUtil.DateStringFormat(coupon.getEndDate()), coupon.getCouponId(), this.company.getCompanyId());
			}
			
			List<Coupon> coupons = coupCompanyDAO.getAllCoupons();

			Iterator<Coupon> i = coupons.iterator();

			/* Check if there is same title */
			while (i.hasNext()) {
				Coupon current = i.next();
				if (coupon.getTitle().equals(current.getTitle())) {
					throw new CouponExistsException("Company failed to add coupon - this coupon already exists. ",
							coupon.getTitle(), this.company.getCompanyId());
				}
			}

			/* Insert coupon to Coupon table and Company_Coupon table */
			if (!i.hasNext()) {
				coupCompanyDAO.insertCoupon(coupon);
				com_couCompany.insertCompany_Coupon(this.company, coupon);
				System.out.println(
						"Company " + this.company.getCompanyName() + " added new coupon: " + coupon.getCouponId());
			}

		} catch (EndDatePassedException e) {
			System.out.println(e.getMessage());
		} catch (CouponExistsException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw new Exception("Company failed to add coupon. couponId: " + coupon.getCouponId());
		}
	}

	/*
	 * This method receive 1 parameter: coupon ID. Removes a coupon and all its
	 * relations from the database. Removing a coupon with this method impacts the
	 * following database tables: (a) Coupon table (b) Company_Coupon table: (c)
	 * Customer_Coupon table: if any customer-coupon record has been created. It is
	 * imperative we will remove all coupon's related data from these tables in
	 * order to be able to remove the coupon data. once we clear all related data it
	 * is safe to remove the coupon from the Coupon table. There are Lists which
	 * hold relevant objects. Using Iterator in order to go through and check all
	 * the list objects. If coupon id doesn't exist in DB, ObjectNotFoundException
	 * is activated. If Company try to remove company which not belongs it,
	 * NotBelongsException is activated.
	 */
	public void removeCoupon(long couponId) throws Exception {

		try {

			/* check if couponId exist */
			List<Coupon> coupons = coupCompanyDAO.getAllCoupons();
			Iterator<Coupon> i = coupons.iterator();
			int flag = 0;
			while (i.hasNext()) {
				Coupon current = i.next();
				if (current.getCouponId() == couponId) {
					flag = 1;
				}
			}
			if (!i.hasNext() && flag == 0) {
				throw new ObjectNotFoundException("couponId does not exist in system. ", this.company.getCompanyId(),
						this.clientType, couponId);
			}

			/* check if coupon belongs to this company */
			List<Long> companiesId = com_couCompany.getCompanyId(couponId);
			Iterator<Long> it = companiesId.iterator();
			while (it.hasNext()) {
				long current = it.next();
				if (current == this.company.getCompanyId()) {
					Coupon coupon = coupCompanyDAO.getCoupon(couponId);

					/*
					 * Check if there are records in Customer_Coupon table. Remove all relations in
					 * Join tables and then remove from Coupon table.
					 */
					List<Long> customersId = cus_couCompany.getCustomerId(couponId);
					if (!customersId.isEmpty()) {
						cus_couCompany.removeCustomer_Coupon(coupon);
						com_couCompany.removeCompany_Coupon(coupon);
						coupCompanyDAO.removeCoupon(coupon);
					}
					com_couCompany.removeCompany_Coupon(coupon);
					coupCompanyDAO.removeCoupon(coupon);
				} else if (!it.hasNext()) {
					throw new NotBelongsException(
							"Company failed to remove coupon - this coupon not belongs to this company. ",
							this.company.getCompanyId(), this.clientType.toString(), couponId);
				}
			}

		} catch (ObjectNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (NotBelongsException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			throw new Exception("Compnay failed to remove coupon. couponId: " + couponId);
		}

	}

	/*
	 * This method receive 3 parameters: coupon ID, new end date and new price.
	 * Updates an existing coupon in the Coupon table under the following
	 * restrictions: 1. There is only option to update end date and price 2. Can't
	 * update a coupon that has not been created first 3. Can't update coupon which
	 * not belongs to this company 4. Can't update end date with date which already
	 * passed. If the restrictions is exceeded, the relevant exception activated:
	 * ObjectNotFoundException, NotBelongsException, EndDatePassedException. There
	 * are Lists which hold relevant objects. Using Iterator in order to go through
	 * and check all the list objects.
	 */
	public void updateCoupon(long couponId, String newEndDate, double newPrice) throws Exception {
		try {

			/* Check if couponId exists */
			List<Coupon> coupons = coupCompanyDAO.getAllCoupons();
			Iterator<Coupon> i = coupons.iterator();
			int flag = 0;
			while (i.hasNext()) {
				Coupon current = i.next();
				if (current.getCouponId() == couponId) {
					flag = 1;
				}
			}
			if (!i.hasNext() && flag == 0) {
				throw new ObjectNotFoundException("couponId does not exist in system", this.company.getCompanyId(),
						this.clientType, couponId);
			}

			/* Check if coupon belongs to this company */
			List<Long> companiesId = com_couCompany.getCompanyId(couponId);
			Iterator<Long> it = companiesId.iterator();
			while (it.hasNext()) {
				long current = it.next();
				if (current == this.company.getCompanyId()) {
					Coupon coupon = coupCompanyDAO.getCoupon(couponId);
					coupon.setEndDate(DateConverterUtil.convertStringDate(newEndDate));
					coupon.setPrice(newPrice);

					/* Check if end date already passed */
					if (coupon.getEndDate().isBefore(LocalDate.now())) {
						throw new EndDatePassedException(
								"Company failed to update coupon - the end date already passed. ", newEndDate,
								coupon.getCouponId(), this.company.getCompanyId());
					}
					coupCompanyDAO.updateCoupon(coupon);
				} else if (!it.hasNext()) {
					throw new NotBelongsException(
							"Company failed to update coupon - this coupon not belongs to this company. ",
							this.company.getCompanyId(), this.clientType.toString(), couponId);
				}
			}

		} catch (ObjectNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (NotBelongsException e) {
			System.out.println(e.getMessage());
		} catch (EndDatePassedException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			throw new Exception("Company failed to update coupon. couponId: " + couponId);
		}
	}

	/*
	 * Retrieves a specific company using its ID.
	 */
	public Company getCompany() throws Exception {

		try {
			System.out.println(compCompanyDAO.getCompany(this.company.getCompanyId()));
			return compCompanyDAO.getCompany(this.company.getCompanyId());
		} catch (Exception e) {
			throw new Exception("Company failed to get company details. companyId: " + this.company.getCompanyId());
		}
	}

	/*
	 * This method receive 1 parameter: coupon ID. Retrieves a specific coupon using
	 * its ID.If there are no records in DB for this id, ObjectNotFoundException is
	 * activated. If Company try to remove company which not belongs it,
	 * NotBelongsException is activated. There are Lists which hold relevant
	 * objects. Using Iterator in order to go through and check all the list
	 * objects.
	 */
	public Coupon getCoupon(long couponId) throws Exception {

		try {

			/* Check if couponId exists */
			List<Coupon> coupons = coupCompanyDAO.getAllCoupons();
			Iterator<Coupon> i = coupons.iterator();
			int flag = 0;
			while (i.hasNext()) {
				Coupon current = i.next();
				if (current.getCouponId() == couponId) {
					flag = 1;
				}
			}
			if (!i.hasNext() && flag == 0) {
				throw new ObjectNotFoundException("couponId does not exist in system", this.company.getCompanyId(),
						this.clientType, couponId);
			}

			/* check if coupon belongs to this company */
			List<Long> companiesId = com_couCompany.getCompanyId(couponId);
			Iterator<Long> it = companiesId.iterator();
			while (it.hasNext()) {
				long current = it.next();
				if (current == this.company.getCompanyId()) {
					System.out.println(coupCompanyDAO.getCoupon(couponId));
					return coupCompanyDAO.getCoupon(couponId);
				} else if (!it.hasNext()) {
					throw new NotBelongsException(
							"Company failed to get coupon - this coupon not belongs to this company. ",
							this.company.getCompanyId(), this.clientType.toString(), couponId);
				}
			}
		} catch (ObjectNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (NotBelongsException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			throw new Exception("Company failed to get coupon data. companyId: " + this.company.getCompanyId());
		}
		return null;

	}

	/*
	 * Retrieves a all coupons which belong to this company in DB. Get only coupons
	 * which related in Join table to this company ID. If there are no records in
	 * DB, NoDetailsFoundException is activated. There are Lists which hold relevant
	 * objects.
	 */
	public List<Coupon> getAllCoupons() throws Exception {

		try {
			/* Get all coupons that belongs to company from Company_Coupon table */
			List<Long> coupons = com_couCompany.getCouponId(this.company.getCompanyId());
			List<Coupon> couponsToGet = new ArrayList<>();
			/* Run on ID of coupons in loop */
			for (Long cId : coupons) {
				/* Get all Coupons objects that belongs to company */
				couponsToGet.add(coupCompanyDAO.getCoupon(cId));
			}

			/* Check if there are no records in DB */
			if (couponsToGet.isEmpty()) {
				throw new NoDetailsFoundException(
						"Company " + this.company.getCompanyId() + " failed to get all coupons - no details found",
						this.company.getCompanyId(), this.clientType);
			}

			List<Coupon> couponsToView = couponsToGet;
			for (Coupon c : couponsToView) {
				System.out.println(c);
			}
			return couponsToGet;
		} catch (NoDetailsFoundException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			throw new Exception("Company failed to get coupons data. companyId: " + this.company.getCompanyId());
		}
		return null;

	}

	/*
	 * This method receive 1 parameter: type name. Retrieves a all coupons
	 * which belong to this company and with specific type in DB. Get only coupons
	 * which related in Join table to this company ID. If there are no records in
	 * DB, NoDetailsFoundException is activated. There are Lists which hold relevant
	 * objects.
	 */
	public List<Coupon> getAllCouponsByType(String typeName) throws Exception {

		try {
			/* Get all coupons that belongs to company from Company_Coupon table */
			List<Long> coupons = com_couCompany.getCouponId(this.company.getCompanyId());
			List<Coupon> couponsToGet = new ArrayList<>();
			for (Long cId : coupons) {
				/* Get all Coupons objects that belongs to company and has relevant type */
				couponsToGet.addAll(coupCompanyDAO.getAllCouponsByType(cId, typeName));

			}
			
			/* Check if there are no records in DB */
			if (couponsToGet.isEmpty()) {
				throw new NoDetailsFoundException(
						"Company " + this.company.getCompanyId()
								+ " failed to get all coupons by type - no details found",
						this.company.getCompanyId(), this.clientType);
			}

			List<Coupon> couponsToView = couponsToGet;
			for (Coupon c : couponsToView) {
				System.out.println(c);
			}
			return couponsToGet;
		} catch (NoDetailsFoundException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			throw new Exception("Company failed to get coupons data by Type. companyId: " + this.company.getCompanyId()
					+ " couponType: " + typeName);
		}
		return null;

	}

	/*
	 * This method receive 1 parameter: price top. Retrieves a all coupons
	 * which belong to this company and with specific price top in DB. Get only coupons
	 * which related in Join table to this company ID. If there are no records in
	 * DB, NoDetailsFoundException is activated. There are Lists which hold relevant
	 * objects.
	 */
	public List<Coupon> getAllCouponsByPrice(double priceTop) throws Exception {

		try {
			/* Get all coupons that belongs to company from Company_Coupon table */
			List<Long> coupons = com_couCompany.getCouponId(this.company.getCompanyId());
			List<Coupon> couponsToGet = new ArrayList<>();
			for (Long cId : coupons) {
				/* Get all Coupons objects that belongs to company and has relevant price */
				couponsToGet.addAll(coupCompanyDAO.getAllCouponsByPrice(cId, priceTop));

			}
			
			/* Check if there are no records in DB */
			if (couponsToGet.isEmpty()) {
				throw new NoDetailsFoundException(
						"Company " + this.company.getCompanyId()
								+ " failed to get all coupons by price - no details found",
						this.company.getCompanyId(), this.clientType);
			}

			List<Coupon> couponsToView = couponsToGet;
			for (Coupon c : couponsToView) {
				System.out.println(c);
			}
			return couponsToGet;
		} catch (NoDetailsFoundException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			throw new Exception("Company failed to get coupons data by Price. companyId: " + this.company.getCompanyId()
					+ " priceTop: " + priceTop);
		}
		return null;
	}

	/*
	 * This method receive 1 parameter: until date. Retrieves a all coupons
	 * which belong to this company and with specific date limit in DB. Get only coupons
	 * which related in Join table to this company ID. If there are no records in
	 * DB, NoDetailsFoundException is activated. There are Lists which hold relevant
	 * objects.
	 */
	public List<Coupon> getAllCouponsByDate(String untilDate) throws Exception {

		try {
			/* Get all coupons that belongs to company from Company_Coupon table */
			List<Long> coupons = com_couCompany.getCouponId(this.company.getCompanyId());
			List<Coupon> couponsToGet = new ArrayList<>();
			for (Long cId : coupons) {
				/* Get all Coupons objects that belongs to company and has relevant date */
				couponsToGet.addAll(coupCompanyDAO.getAllCouponsByDate(cId, untilDate));

			}
			
			/* Check if there are no records in DB */
			if (couponsToGet.isEmpty()) {
				throw new NoDetailsFoundException(
						"Company " + this.company.getCompanyId()
								+ " failed to get all coupons by date - no details found",
						this.company.getCompanyId(), this.clientType);
			}

			List<Coupon> couponsToView = couponsToGet;
			for (Coupon c : couponsToView) {
				System.out.println(c);
			}
			return couponsToGet;
		} catch (NoDetailsFoundException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			throw new Exception("Company failed to get coupons data by Date. companyId: " + this.company.getCompanyId()
					+ " untilDate: " + untilDate);
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
