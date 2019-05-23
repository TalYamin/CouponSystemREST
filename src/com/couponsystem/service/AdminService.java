package com.couponsystem.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.couponsystem.bean.Company;
import com.couponsystem.facade.AdminUserFacade;
import com.couponsystem.utils.ClientType;
import com.couponsystem.utils.CouponSystem;

@Path("admin")
public class AdminService {

	@Context
	private HttpServletRequest request;

	@Context
	private HttpServletResponse response;

	private AdminUserFacade getFacade() {

		AdminUserFacade admin = null;
		admin = (AdminUserFacade) request.getSession(false).getAttribute("facade");
		return admin;
	}

	// add company

	@POST
	@Path("addCompany")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Boolean addCompany(Company company) {

		// check
		CouponSystem couponSystem;
		boolean success = false;
		try {
			couponSystem = CouponSystem.getInstance();
			// check
			AdminUserFacade adminUserFacade = (AdminUserFacade) couponSystem.login("admin", "1234", ClientType.ADMIN);
			// AdminUserFacade adminUserFacade = getFacade();
			adminUserFacade.insertCompany(company);
			success = true;
			return success;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return success;

	}

}
