package com.couponsystem.facade;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.couponsystem.dao.CouponDAO;
import com.couponsystem.dao.CustomerDAO;
import com.couponsystem.dao.Customer_CouponDAO;
import com.couponsystem.dbdao.CouponDBDAO;
import com.couponsystem.dbdao.CustomerDBDAO;
import com.couponsystem.dbdao.Customer_CouponDBDAO;
import com.couponsystem.exceptions.CouponExpiredException;
import com.couponsystem.exceptions.NoDetailsFoundException;
import com.couponsystem.exceptions.ObjectNotFoundException;
import com.couponsystem.exceptions.OutOfStockException;
import com.couponsystem.exceptions.SamePurchaseException;
import com.couponsystem.bean.Coupon;
import com.couponsystem.bean.Customer;
import com.couponsystem.utils.ClientType;


/**
 * @author Tal Yamin
 *
 */

/*
 * This class sets the business logic and actions for the Customer client.
 * Methods in this class based on following pattern: (1) Using Iterators in
 * order to go through and check all the list objects. (2) Checking relevant
 * restrictions by business logic. (3) Throwing relevant Exception when it
 * activated.
 */

public class CustomerUserFacade implements CouponClientFacade {

	/* Data members which hold customer object, client type and Access to DAO */
	private Customer customer;
	private ClientType clientType;
	private CustomerDAO cusCustomerDAO;
	private Customer_CouponDAO cus_couCustomerDAO;
	private CouponDAO couCustomerDAO;

	/* empty CTOR of CustomerUserFacade */
	public CustomerUserFacade() {

	}

	/*
	 * Full CTOR: Receive 1 parameter: customer object. Sets specific customer which
	 * received, client type and DAO to DBDAO.
	 */
	public CustomerUserFacade(Customer customer) {
		this.customer = customer;
		this.clientType = ClientType.CUSTOMER;
		this.cusCustomerDAO = new CustomerDBDAO();
		this.cus_couCustomerDAO = new Customer_CouponDBDAO();
		this.couCustomerDAO = new CouponDBDAO();
	}

	/*
	 * Retrieves a specific customer using its ID.
	 */
	public Customer getCustomer() throws Exception {
		try {
			System.out.println(cusCustomerDAO.getCustomer(this.customer.getCustomerId()));
			return cusCustomerDAO.getCustomer(this.customer.getCustomerId());
		} catch (Exception e) {
			throw new Exception(
					"Cusstomer failed to get customer details. customerId: " + this.customer.getCustomerId());
		}
	}

	/*
	 * Purchase coupon by customer. This method receive 1 parameter: coupon ID.
	 * Inserts a new coupon in the Customer_Coupon table under the following
	 * restrictions: 1. Can't purchase coupon which not exist in DB. 2. Can't
	 * purchase coupon with same id of coupon which already purchased. 3. Can't
	 * purchase coupon which its amount 0, out of stock. 4. Can't purchase coupon
	 * which its end date already passed, expired coupon. If one of the restrictions
	 * is exceeded, the relevant exceptions activated: ObjectNotFoundException,
	 * SamePurchaseException, OutOfStockException, CouponExpiredException. If the
	 * purchase succeeds - there is updating of amount in Coupon table. There are
	 * Lists which hold all the relevant objects from DB, Using Iterator in order to
	 * go through and check all the list objects.
	 */
	public String purchaseCoupon(long couponId) throws Exception {

		try {

			/* Check if couponId exists */
			List<Coupon> coupons = couCustomerDAO.getAllCoupons();
			Iterator<Coupon> it = coupons.iterator();
			int flag = 0;
			while (it.hasNext()) {
				Coupon current = it.next();
				if (current.getCouponId() == couponId) {
					flag = 1;
				}
			}
			if (!it.hasNext() && flag == 0) {
				throw new ObjectNotFoundException("couponId does not exist in system. ", this.customer.getCustomerId(),
						this.clientType, couponId);
			}

			/* Check if couponId similar to another coupon id */
			List<Long> customers = cus_couCustomerDAO.getCustomerId(couponId);

			Iterator<Long> i = customers.iterator();

			while (i.hasNext()) {
				long current = i.next();
				if (this.customer.getCustomerId() == current) {
					throw new SamePurchaseException("Customer unable to purchase - already purchased same coupon. ",
							couponId, this.customer.getCustomerId());
				}
			}
			if (!i.hasNext()) {

				/* Check if coupon is out of stock */
				Coupon c = couCustomerDAO.getCoupon(couponId);
				if (c.getAmount() <= 0) {
					throw new OutOfStockException("Customer unable to purchase - this coupon is out of stock. ",
							c.getAmount(), couponId, this.customer.getCustomerId());

				}
				/* Check if coupon already expired */
				if (c.getEndDate().isBefore(LocalDate.now())) {
					throw new CouponExpiredException("Customer unable to purchase - this coupon has expired. ",
							c.getEndDate().toString(), couponId, this.customer.getCustomerId());
				} else {

					/* update amount of coupon in Coupon table and insert record to Customer_Coupon table */
					Coupon newCoupon = couCustomerDAO.getCoupon(couponId);
					newCoupon.setAmount(newCoupon.getAmount() - 1);
					couCustomerDAO.updateCoupon(newCoupon);
					cus_couCustomerDAO.insertCustomer_Coupon(this.customer, newCoupon);
					System.out.println(
							"Customer " + customer.getCustomerName() + " purchased successfully Coupon " + couponId);
					return "Customer " + customer.getCustomerName() + " purchased successfully Coupon " + couponId;
				}
			}
		} catch (ObjectNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (SamePurchaseException e) {
			System.out.println(e.getMessage());
		} catch (OutOfStockException e) {
			System.out.println(e.getMessage());
		} catch (CouponExpiredException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			throw new Exception("Customer failed to purchase coupon. couponId: " + couponId + " customerId: "
					+ this.customer.getCustomerId());
		}
		return null;

	}

	/*
	 * Retrieves a all purchases which belong to this customer in DB. Get only coupons
	 * which related in Join table to this customer ID. If there are no records in
	 * DB, NoDetailsFoundException is activated. There are Lists which hold relevant
	 * objects.
	 */
	public List<Coupon> getAllPurchases() throws Exception {

		try {
			/* Get all coupons that belongs to customer from Customer_Coupon table */
			List<Long> coupons = cus_couCustomerDAO.getCouponId(this.customer.getCustomerId());
			List<Coupon> couponsToGet = new ArrayList<>();
			/* Run on ID of coupons in loop */
			for (Long cId : coupons) {
				// get all Coupons objects that belongs to customer
				couponsToGet.add(couCustomerDAO.getCoupon(cId));
			}
			
			/* Check if there are no records in DB */
			if (couponsToGet.isEmpty()) {
				throw new NoDetailsFoundException(
						"Customer " + this.customer.getCustomerId()
								+ " failed to get all purchase history - no details found",
						this.customer.getCustomerId(), this.clientType);
			}

			List<Coupon> couponsToView = couponsToGet;
			for (Coupon c : couponsToView) {
				System.out.println(c);
			}
			return couponsToGet;
		} catch (NoDetailsFoundException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			throw new Exception(
					"Custoemr failed to get all purchase history. customerId: " + this.customer.getCustomerId());
		}
		return null;
	}

	/*
	 * This method receive 1 parameter: type name. Retrieves a all coupons
	 * which belong to this customer and with specific type in DB. Get only coupons
	 * which related in Join table to this customer ID. If there are no records in
	 * DB, NoDetailsFoundException is activated. There are Lists which hold relevant
	 * objects.
	 */
	public List<Coupon> getAllCouponsByType(String typeName) throws Exception {

		try {
			/* Get all coupons that belongs to customer from Customer_Coupon table */
			List<Long> coupons = cus_couCustomerDAO.getCouponId(this.customer.getCustomerId());
			List<Coupon> couponsToGet = new ArrayList<>();
			for (Long cId : coupons) {
				/* Get all Coupons objects that belongs to customer and has relevant type */
				couponsToGet.addAll(couCustomerDAO.getAllCouponsByType(cId, typeName));

			}
			
			/* Check if there are no records in DB */
			if (couponsToGet.isEmpty()) {
				throw new NoDetailsFoundException(
						"Customer " + this.customer.getCustomerId()
								+ " failed to get all coupons by type - no details found",
						this.customer.getCustomerId(), this.clientType);
			}
			List<Coupon> couponsToView = couponsToGet;
			for (Coupon c : couponsToView) {
				System.out.println(c);
			}
			return couponsToGet;
		} catch (NoDetailsFoundException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			throw new Exception("Customer failed to get coupons data by Type. customerId: "
					+ this.customer.getCustomerId() + " couponType: " + typeName);
		}
		return null;

	}

	/*
	 * This method receive 1 parameter: price top. Retrieves a all coupons
	 * which belong to this customer and with specific price top in DB. Get only coupons
	 * which related in Join table to this customer ID. If there are no records in
	 * DB, NoDetailsFoundException is activated. There are Lists which hold relevant
	 * objects.
	 */
	public List<Coupon> getAllCouponsByPrice(double priceTop) throws Exception {

		try {
			/* get all coupons that belongs to customer from Customer_Coupon table */
			List<Long> coupons = cus_couCustomerDAO.getCouponId(this.customer.getCustomerId());
			List<Coupon> couponsToGet = new ArrayList<>();
			for (Long cId : coupons) {
				/* get all Coupons objects that belongs to customer and has relevant price */
				couponsToGet.addAll(couCustomerDAO.getAllCouponsByPrice(cId, priceTop));

			}
			
			/* Check if there are no records in DB */
			if (couponsToGet.isEmpty()) {
				throw new NoDetailsFoundException(
						"Customer " + this.customer.getCustomerId()
								+ " failed to get all coupons by price - no details found",
						this.customer.getCustomerId(), this.clientType);
			}
			List<Coupon> couponsToView = couponsToGet;
			for (Coupon c : couponsToView) {
				System.out.println(c);
			}
			return couponsToGet;
		} catch (NoDetailsFoundException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			throw new Exception("Customer failed to get coupons data by Price. customerId: "
					+ this.customer.getCustomerId() + " priceTop: " + priceTop);
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
