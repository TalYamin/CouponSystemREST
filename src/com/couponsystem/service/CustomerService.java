package com.couponsystem.service;

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
import com.couponsystem.bean.Customer;
import com.couponsystem.facade.AdminUserFacade;
import com.couponsystem.facade.CompanyUserFacade;
import com.couponsystem.facade.CustomerUserFacade;
import com.couponsystem.utils.RequestStatus;

@Path("/customer")
public class CustomerService {

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
				throw new Exception(
						"Customer" + customerUserFacade.getCustomer().getCustomerId() + "failed to get customer ");
			}

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
			if (customerUserFacade.purchaseCoupon(couponId) != null) {
				System.out.println("coupon was purchased in success " + couponId);
				return new RequestStatus(true);
			} else {
				throw new Exception("Customer" + customerUserFacade.getCustomer().getCustomerId() +" failed to update company " + couponId);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return new RequestStatus(false);

	}

}
