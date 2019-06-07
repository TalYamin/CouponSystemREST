package com.couponsystem.service;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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
import com.couponsystem.utils.DateConverterUtil;
import com.couponsystem.utils.RequestStatus;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/* 
 * issues: 
 * addCoupon - star date
 * updateCoupon
 * getAllCouponsByDate
*/
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
	@Enumerated(EnumType.STRING)
	public RequestStatus addCoupon(String jsonString) throws Exception {

		System.out.println("now in addCoupon");

		try {
			Gson gson = new Gson();
			JsonParser parser = new JsonParser();
			JsonObject object = (JsonObject) parser.parse(jsonString);
			Coupon coupon = gson.fromJson(object, Coupon.class);

			System.out.println(request.getSession(false).getId());
			CompanyUserFacade companyUserFacade = getFacade();
			if (companyUserFacade.insertCoupon(coupon) != null) {
				System.out.println("Coupon added in success " + coupon.getCouponId());
				return new RequestStatus(true);
			} else {
				throw new Exception("Company failed to add coupon " + coupon.getCouponId());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return new RequestStatus(false);

	}

	// remove coupon
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Path("removeCoupon/{couponId}")
	public RequestStatus removeCoupon(@PathParam("couponId") long couponId) throws Exception {

		System.out.println("now in removeCoupon");

		CompanyUserFacade companyUserFacade;

		try {
			System.out.println(request.getSession(false).getId());
			companyUserFacade = getFacade();
			if (companyUserFacade.removeCoupon(couponId) != null) {
				System.out.println("Coupon was removed in success " + couponId);
				return new RequestStatus(true);
			} else {
				throw new Exception("Company" + companyUserFacade.getCompany().getCompanyName()
						+ "failed to remove company " + couponId);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return new RequestStatus(false);

	}

	// update coupon
//	@POST
//	@Consumes(MediaType.APPLICATION_JSON)
//	@Produces(MediaType.APPLICATION_JSON)
	// @Path("updateCoupon/{couponId}/{newEndDate}/{newPrice}")
	// public RequestStatus updateCoupon(@PathParam("couponId") long couponId,
	// @PathParam("newEndDate") String newEndDate,
	// @PathParam("newPrice") double newPrice) throws Exception {
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("updateCoupon")
	public RequestStatus updateCoupon(String jsonString) throws Exception {

		System.out.println("now in updateCoupon");

		try {
			System.out.println(jsonString);
			System.out.println(request.getSession(false).getId());
			JsonParser parser = new JsonParser();
			JsonObject obj = parser.parse(jsonString).getAsJsonObject();
			long couponId = Long.parseLong(obj.get("couponId").getAsString());
			String newEndDate = obj.get("newEndDate").getAsString();
			double newPrice = Double.parseDouble(obj.get("newPrice").getAsString());
			CompanyUserFacade companyUserFacade = getFacade();
			if (companyUserFacade.updateCoupon(couponId, newEndDate, newPrice) != null) {
				System.out.println("Coupon was updated in success " + couponId);
				return new RequestStatus(true);
			} else {
				throw new Exception("Company" + companyUserFacade.getCompany().getCompanyName()
						+ " failed to update coupon " + couponId);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return new RequestStatus(false);

	}

	// get Company
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("getCompany")
	public Company getCompany() throws Exception {

		System.out.println("now in getCompany");

		try {
			System.out.println(request.getSession(false).getId());
			CompanyUserFacade companyUserFacade = getFacade();
			Company company = companyUserFacade.getCompany();
			if (company != null) {
				System.out.println("company was returned in success " + companyUserFacade.getCompany().getCompanyId());
				return company;
			} else {
				throw new Exception(
						"Company" + companyUserFacade.getCompany().getCompanyName() + "failed to get company ");
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

		try {
			System.out.println(request.getSession(false).getId());
			CompanyUserFacade companyUserFacade = getFacade();
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

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("getAllCoupons")
	public String getAllCoupons() throws Exception {

		System.out.println("now in getAllCoupons");

		try {
			System.out.println(request.getSession(false).getId());
			CompanyUserFacade companyUserFacade = getFacade();
			List<Coupon> coupons = companyUserFacade.getAllCoupons();
			if (coupons != null) {
				System.out.println("All coupons were returned in success ");
				return new Gson().toJson(coupons);
			} else {
				throw new Exception(
						"Company" + companyUserFacade.getCompany().getCompanyName() + "failed to get all coupons ");
			}

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
			CompanyUserFacade companyUserFacade = getFacade();
			List<Coupon> coupons = companyUserFacade.getAllCouponsByType(typeName);
			if (coupons != null) {
				System.out.println("All coupons by type were returned in success ");
				return new Gson().toJson(coupons);
			} else {
				throw new Exception("Company" + companyUserFacade.getCompany().getCompanyName()
						+ "failed to get all coupons by type ");
			}

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
			CompanyUserFacade companyUserFacade = getFacade();
			List<Coupon> coupons = companyUserFacade.getAllCouponsByPrice(priceTop);
			if (coupons != null) {
				System.out.println("All coupons by price were returned in success ");
				return new Gson().toJson(coupons);
			} else {
				throw new Exception("Company" + companyUserFacade.getCompany().getCompanyName()
						+ "failed to get all coupons by price ");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("getAllCouponsByDate/{untilDate}")
	public String getAllCouponsByDate(@PathParam("untilDate") String untilDate) throws Exception {

		System.out.println("now in getAllCouponsByDate");

		try {
			// System.out.println(request.getSession(false).getId());
			// CompanyUserFacade companyUserFacade = getFacade();
			// List<Coupon> coupons = companyUserFacade.getAllCouponsByDate(untilDate);
			// if (coupons != null) {
			// System.out.println("All coupons by date were returned in success ");
			// return new Gson().toJson(coupons);
			// } else {
			// throw new Exception("Company" +
			// companyUserFacade.getCompany().getCompanyName()
			// + "failed to get all coupons by date ");
			// }
			//
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
}
