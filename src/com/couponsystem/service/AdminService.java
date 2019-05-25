package com.couponsystem.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.couponsystem.bean.Company;
import com.couponsystem.facade.AdminUserFacade;
import com.couponsystem.utils.ClientType;
import com.couponsystem.utils.CouponSystem;
import com.couponsystem.utils.RequestStatus;

@Path("/admin")
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
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("addCompany")
	public RequestStatus addCompany(Company company) throws Exception {

		System.out.println("now in addCompany");

		CouponSystem couponSystem;
		AdminUserFacade adminUserFacade;

		try {
			System.out.println(request.getSession(false).getId());
			couponSystem = CouponSystem.getInstance();
			adminUserFacade = getFacade();
			if (adminUserFacade.insertCompany(company) != null) {
				System.out.println("Company added in success " + company);
				return new RequestStatus(true);
			} else {
				throw new Exception("failed to add company " + company.getCompanyName());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return new RequestStatus(false);

	}

}
