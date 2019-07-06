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
import com.couponsystem.exceptions.CompanyServiceException;
import com.couponsystem.facade.AdminUserFacade;
import com.couponsystem.facade.CompanyUserFacade;
import com.couponsystem.utils.ClientType;
import com.couponsystem.utils.CouponSystem;
import com.couponsystem.utils.DateConverterUtil;
import com.couponsystem.utils.RequestStatus;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/* 
 * issues: 
 * addCoupon - start date added in client side
 * updateCoupon - json need to be transfered with Sting of date "d/mm/yyyy"
 * getAllCouponsByDate
*/
@Path("/company")
public class CompanyService {

	private String requestMessage;

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
//			System.out.println(request.getSession(false).getId());
//			CompanyUserFacade companyUserFacade = getFacade();
			CompanyUserFacade companyUserFacade = (CompanyUserFacade) CouponSystem.getInstance().login("Dell", "Dell9876", ClientType.COMPANY);
			requestMessage = companyUserFacade.insertCoupon(coupon);
			if (requestMessage.indexOf("success") != -1) {
				System.out.println("Coupon added in success " + coupon.getCouponId());
				return new RequestStatus(true, requestMessage);
			} else {
				throw new CompanyServiceException("Company failed to add coupon ",
						companyUserFacade.getCompany().getCompanyId(), companyUserFacade.getCompany().getCompanyName(),
						ClientType.COMPANY.toString(), coupon.getCouponId());
			}

		} catch (CompanyServiceException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new RequestStatus(false, requestMessage);

	}

	// remove coupon
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Path("removeCoupon/{couponId}")
	public RequestStatus removeCoupon(@PathParam("couponId") long couponId) throws Exception {

		System.out.println("now in removeCoupon");

//		CompanyUserFacade companyUserFacade;

		try {
//			System.out.println(request.getSession(false).getId());
//			companyUserFacade = getFacade();
			CompanyUserFacade companyUserFacade = (CompanyUserFacade) CouponSystem.getInstance().login("Dell", "Dell9876", ClientType.COMPANY);
			requestMessage = companyUserFacade.removeCoupon(couponId);
			if (requestMessage.indexOf("success") != -1) {
				System.out.println("Coupon was removed in success " + couponId);
				return new RequestStatus(true, requestMessage);
			} else {
				throw new CompanyServiceException("Company failed to remove coupon ",
						companyUserFacade.getCompany().getCompanyId(), companyUserFacade.getCompany().getCompanyName(),
						ClientType.COMPANY.toString(), couponId);
			}

		} catch (CompanyServiceException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new RequestStatus(false, requestMessage);

	}

	// update coupon
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("updateCoupon")
	public RequestStatus updateCoupon(String jsonString) throws Exception {

		System.out.println("now in updateCoupon");

		try {
			System.out.println(jsonString);
	//		System.out.println(request.getSession(false).getId());
			JsonParser parser = new JsonParser();
			JsonObject obj = parser.parse(jsonString).getAsJsonObject();
			long couponId = Long.parseLong(obj.get("couponId").getAsString());
			String newEndDate = obj.get("newEndDate").getAsString();
			double newPrice = Double.parseDouble(obj.get("newPrice").getAsString());
		//	CompanyUserFacade companyUserFacade = getFacade();
			CompanyUserFacade companyUserFacade = (CompanyUserFacade) CouponSystem.getInstance().login("Dell", "Dell9876", ClientType.COMPANY);
			requestMessage = companyUserFacade.updateCoupon(couponId, newEndDate, newPrice);
			if (requestMessage.indexOf("success") != -1) {
				System.out.println("Coupon was updated in success " + couponId);
				return new RequestStatus(true, requestMessage);
			} else {
				throw new CompanyServiceException("Company failed to update coupon ",
						companyUserFacade.getCompany().getCompanyId(), companyUserFacade.getCompany().getCompanyName(),
						ClientType.COMPANY.toString(), couponId);
			}

		} catch (CompanyServiceException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new RequestStatus(false, requestMessage);

	}

	// get Company
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("getCompany")
	public Company getCompany() throws Exception {

		System.out.println("now in getCompany");

		try {
//			System.out.println(request.getSession(false).getId());
//			CompanyUserFacade companyUserFacade = getFacade();
			CompanyUserFacade companyUserFacade = (CompanyUserFacade) CouponSystem.getInstance().login("Dell", "Dell9876", ClientType.COMPANY);
			Company company = companyUserFacade.getCompany();
			if (company != null) {
				System.out.println("company was returned in success " + companyUserFacade.getCompany().getCompanyId());
				return company;
			} else {
				throw new CompanyServiceException(
						"Company" + companyUserFacade.getCompany().getCompanyName() + "failed to get company ");
			}

		} catch (CompanyServiceException e) {
			System.out.println(e.getMessage());
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
	//		System.out.println(request.getSession(false).getId());
	//		CompanyUserFacade companyUserFacade = getFacade();
			CompanyUserFacade companyUserFacade = (CompanyUserFacade) CouponSystem.getInstance().login("Dell", "Dell9876", ClientType.COMPANY);
			Coupon coupon = companyUserFacade.getCoupon(couponId);
			if (coupon != null) {
				System.out.println("Coupon was returned in success " + couponId);
				return new Gson().toJson(coupon);
			} else {
				throw new CompanyServiceException("Company failed to get coupon ",
						companyUserFacade.getCompany().getCompanyId(), companyUserFacade.getCompany().getCompanyName(),
						ClientType.COMPANY.toString(), couponId);
			}

		} catch (CompanyServiceException e) {
			System.out.println(e.getMessage());
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
//			System.out.println(request.getSession(false).getId());
//			CompanyUserFacade companyUserFacade = getFacade();
			CompanyUserFacade companyUserFacade = (CompanyUserFacade) CouponSystem.getInstance().login("Dell", "Dell9876", ClientType.COMPANY);
			List<Coupon> coupons = companyUserFacade.getAllCoupons();
			if (coupons != null) {
				System.out.println("All coupons were returned in success ");
				return new Gson().toJson(coupons);
			} else {
				throw new CompanyServiceException(
						"Company" + companyUserFacade.getCompany().getCompanyName() + "failed to get all coupons ");
			}

		} catch (CompanyServiceException e) {
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
//			System.out.println(request.getSession(false).getId());
//			CompanyUserFacade companyUserFacade = getFacade();
			CompanyUserFacade companyUserFacade = (CompanyUserFacade) CouponSystem.getInstance().login("Dell", "Dell9876", ClientType.COMPANY);
			List<Coupon> coupons = companyUserFacade.getAllCouponsByType(typeName);
			if (coupons != null) {
				System.out.println("All coupons by type were returned in success ");
				return new Gson().toJson(coupons);
			} else {
				throw new CompanyServiceException("Company" + companyUserFacade.getCompany().getCompanyName()
						+ "failed to get all coupons by type " + typeName);
			}

		} catch (CompanyServiceException e) {
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
//			System.out.println(request.getSession(false).getId());
//			CompanyUserFacade companyUserFacade = getFacade();
			CompanyUserFacade companyUserFacade = (CompanyUserFacade) CouponSystem.getInstance().login("Dell", "Dell9876", ClientType.COMPANY);
			List<Coupon> coupons = companyUserFacade.getAllCouponsByPrice(priceTop);
			if (coupons != null) {
				System.out.println("All coupons by price were returned in success ");
				return new Gson().toJson(coupons);
			} else {
				throw new CompanyServiceException("Company" + companyUserFacade.getCompany().getCompanyName()
						+ "failed to get all coupons by price " + priceTop);
			}

		} catch (CompanyServiceException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("getAllCouponsByDate")
	public String getAllCouponsByDate(String jsonString) throws Exception {

		System.out.println("now in getAllCouponsByDate");

		try {
		//	System.out.println(request.getSession(false).getId());
			JsonParser parser = new JsonParser();
			JsonObject obj = parser.parse(jsonString).getAsJsonObject();
			String untilDate = obj.get("untilDate").getAsString();
		//	CompanyUserFacade companyUserFacade = getFacade();
			CompanyUserFacade companyUserFacade = (CompanyUserFacade) CouponSystem.getInstance().login("Dell", "Dell9876", ClientType.COMPANY);
			List<Coupon> coupons = companyUserFacade.getAllCouponsByDate(untilDate);
			if (coupons != null) {
				System.out.println("All coupons by date were returned in success ");
				return new Gson().toJson(coupons);
			} else {
				throw new CompanyServiceException("Company" + companyUserFacade.getCompany().getCompanyName()
						+ "failed to get all coupons by date " + untilDate);
			}

		} catch (CompanyServiceException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

}
