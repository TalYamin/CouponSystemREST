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

@Path("/admin")
public class AdminService {

	@Context
	private HttpServletRequest request;

	@Context
	private HttpServletResponse response;

//	private AdminUserFacade getFacade() {
//
//		AdminUserFacade admin = null;
//		HttpSession session = request.getSession(false);
//		admin = (AdminUserFacade) request.getSession(false).getAttribute("facade");
//		System.out.println(admin.toString());
//		return admin;
//	}
	
//	@GET
//	public void AdminLogin () {
//		System.out.println("admin now is logged in");
//	}

//	// add company
//
//	@POST
//	@Path("addCompany")
//	@Produces(MediaType.APPLICATION_JSON)
//	@Consumes(MediaType.APPLICATION_JSON)
//	public Boolean addCompany(Company company) {
//
//		// check
//		CouponSystem couponSystem;
//		boolean success = false;
//		try {
//			couponSystem = CouponSystem.getInstance();
//			// check
//			AdminUserFacade adminUserFacade = (AdminUserFacade) couponSystem.login("admin", "1234", ClientType.ADMIN);
//			// AdminUserFacade adminUserFacade = getFacade();
//			adminUserFacade.insertCompany(company);
//			success = true;
//			return success;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return success;
//
//	}
	
	
	//add company
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("addCompany")
	public Company addCompany (Company company) throws Exception {
		
//		System.out.println(getFacade().toString());
		System.out.println("now in addCompany");
//		System.out.println("facade ="  + getFacade());
		
		CouponSystem couponSystem;
		AdminUserFacade adminUserFacade;
		
		try {
			System.out.println(1);
			System.out.println(request.getSession(false).getId());
			couponSystem = CouponSystem.getInstance();
//			adminUserFacade = (AdminUserFacade) couponSystem.login("admin", "1234", ClientType.ADMIN);
//			adminUserFacade = this.getFacade();
			System.out.println(2);
			adminUserFacade = (AdminUserFacade) request.getSession(false).getAttribute("facade");
			System.out.println(3);
			if (adminUserFacade.insertCompany(company) != null) {
				System.out.println("Company added in success " + company);
				return company;
			}
			else {
				throw new Exception("failed to add company " + company.getCompanyName());
			}
			

		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
		
//		System.out.println(company.getCompanyName() + " - " + company.getCompanyEmail());
//		company.setCompanyEmail("no email");
//		return company;
	}
	
	
	
	
	
	

}
