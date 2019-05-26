package com.couponsystem.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.couponsystem.bean.Company;
import com.couponsystem.bean.Coupon;
import com.couponsystem.facade.AdminUserFacade;
import com.couponsystem.facade.CompanyUserFacade;
import com.couponsystem.utils.CouponSystem;
import com.google.gson.Gson;

@Path("/company")
public class CompanyService {

	@Context
	private HttpServletRequest request;

	@Context
	private HttpServletResponse response;

	private CompanyUserFacade getFacade() {

		CompanyUserFacade companyUserFacade = null;
		companyUserFacade = (CompanyUserFacade) request.getSession(false).getAttribute("facade");
		return companyUserFacade;
	}

	// add coupon
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("addCoupon")
	public Coupon addCoupon(Coupon coupon) throws Exception {

		System.out.println("now in addCoupon");

//		CouponSystem couponSystem;
		CompanyUserFacade companyUserFacade;

		try {
			System.out.println(request.getSession(false).getId());
//			couponSystem = CouponSystem.getInstance();
			companyUserFacade = getFacade();
			if (companyUserFacade.insertCoupon(coupon) != null) {
				System.out.println("Coupon added in success " + coupon);
				return coupon;
			} else {
				throw new Exception("failed to add coupon " + coupon.getTitle());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	// get coupon
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("getCoupon/{couponId}")
	public String getCoupon(@PathParam("couponId") long couponId) throws Exception {

		System.out.println("now in getCoupon");

//		CouponSystem couponSystem;
		CompanyUserFacade companyUserFacade;

		try {
			System.out.println(request.getSession(false).getId());
//			couponSystem = CouponSystem.getInstance();
			companyUserFacade = getFacade();
			Coupon coupon = companyUserFacade.getCoupon(couponId);
			if (coupon != null) {
				System.out.println("Coupon was returned in success " + couponId);
				return new Gson().toJson(coupon);
			} else {
				throw new Exception("failed to get coupon " + couponId);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
}

	

