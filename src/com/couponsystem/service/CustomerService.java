package com.couponsystem.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.couponsystem.bean.Company;
import com.couponsystem.bean.Coupon;
import com.couponsystem.bean.Customer;
import com.couponsystem.exceptions.CustomerServiceException;
import com.couponsystem.facade.AdminUserFacade;
import com.couponsystem.facade.CompanyUserFacade;
import com.couponsystem.facade.CustomerUserFacade;
import com.couponsystem.utils.ClientType;
import com.couponsystem.utils.RequestStatus;
import com.google.gson.Gson;

@Path("/customer")
public class CustomerService {

	private String requestMessage;

	@Context
	private HttpServletRequest request;

	@Context
	private HttpServletResponse response;

	private CustomerUserFacade getFacade() {

		CustomerUserFacade customerUserFacade = null;
		customerUserFacade = (CustomerUserFacade) request.getSession(false).getAttribute("facade");
		return customerUserFacade;
	}

	// get customer
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("getCustomer")
	public Customer getCustomer() throws Exception {

		System.out.println("now in getCustomer");

		try {
			System.out.println(request.getSession(false).getId());
			CustomerUserFacade customerUserFacade = getFacade();
			Customer customer = customerUserFacade.getCustomer();
			if (customer != null) {
				System.out.println(
						"customer was returned in success " + customerUserFacade.getCustomer().getCustomerId());
				return customer;
			} else {
				throw new CustomerServiceException(
						"Customer" + customerUserFacade.getCustomer().getCustomerId() + "failed to get customer ");
			}

		} catch (CustomerServiceException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	// purchase coupon
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("purchaseCoupon/{couponId}")
	public RequestStatus purchaseCoupon(@PathParam("couponId") long couponId) throws Exception {

		System.out.println("now in purchaseCoupon");

		try {
			System.out.println(request.getSession(false).getId());
			CustomerUserFacade customerUserFacade = getFacade();
			requestMessage = customerUserFacade.purchaseCoupon(couponId);
			if (requestMessage.indexOf("success") != -1) {
				System.out.println("coupon was purchased in success " + couponId);
				return new RequestStatus(true, requestMessage);
			} else {
				throw new CustomerServiceException("Customer failed to purchase coupon ",
						customerUserFacade.getCustomer().getCustomerId(),
						customerUserFacade.getCustomer().getCustomerName(), ClientType.CUSTOMER.toString(), couponId);
			}

		} catch (CustomerServiceException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new RequestStatus(false, requestMessage);

	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("getAllPurchases")
	public String getAllPurchases() throws Exception {

		System.out.println("now in getAllPurchases");

		try {
			System.out.println(request.getSession(false).getId());
			CustomerUserFacade customerUserFacade = getFacade();
			List<Coupon> coupons = customerUserFacade.getAllPurchases();
			if (coupons != null) {
				System.out.println("All purchases were returned in success ");
				return new Gson().toJson(coupons);
			} else {
				throw new CustomerServiceException("Customer" + customerUserFacade.getCustomer().getCustomerName()
						+ "failed to get all purchases ");
			}

		} catch (CustomerServiceException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("getAllCouponsByType/{typeName}")
	public String getAllCouponsByType(@PathParam("typeName") String typeName) throws Exception {

		System.out.println("now in getAllCouponsByType");

		try {
			System.out.println(request.getSession(false).getId());
			CustomerUserFacade customerUserFacade = getFacade();
			List<Coupon> coupons = customerUserFacade.getAllCouponsByType(typeName);
			if (coupons != null) {
				System.out.println("All coupons by type were returned in success ");
				return new Gson().toJson(coupons);
			} else {
				throw new CustomerServiceException("Customer" + customerUserFacade.getCustomer().getCustomerName()
						+ "failed to get all coupons by type " + typeName);
			}

		} catch (CustomerServiceException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("getAllCouponsByPrice/{priceTop}")
	public String getAllCouponsByPrice(@PathParam("priceTop") double priceTop) throws Exception {

		System.out.println("now in getAllCouponsByPrice");

		try {
			System.out.println(request.getSession(false).getId());
			CustomerUserFacade customerUserFacade = getFacade();
			List<Coupon> coupons = customerUserFacade.getAllCouponsByPrice(priceTop);
			if (coupons != null) {
				System.out.println("All coupons by price were returned in success ");
				return new Gson().toJson(coupons);
			} else {
				throw new CustomerServiceException("Customer" + customerUserFacade.getCustomer().getCustomerName()
						+ "failed to get all coupons by price " + priceTop);
			}

		} catch (CustomerServiceException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
}
