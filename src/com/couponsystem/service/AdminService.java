package com.couponsystem.service;

import java.util.List;

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
import com.couponsystem.bean.Customer;
import com.couponsystem.exceptions.AdminServiceException;
import com.couponsystem.facade.AdminUserFacade;
import com.couponsystem.facade.CompanyUserFacade;
import com.couponsystem.utils.ClientType;
import com.couponsystem.utils.CouponSystem;
import com.couponsystem.utils.RequestStatus;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Path("/admin")
public class AdminService {

	private String requestMessage;

	@Context
	private HttpServletRequest request;

	@Context
	private HttpServletResponse response;

	private AdminUserFacade getFacade() {

		AdminUserFacade admin = null;
		admin = (AdminUserFacade) request.getSession(false).getAttribute("facade");
		System.out.println(request.getSession(false).getAttribute("facade"));
		return admin;
	}

	// add company
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("addCompany")
	public RequestStatus addCompany(Company company) throws Exception {

		System.out.println("now in addCompany");

		try {
		//	System.out.println(request.getSession(false).getId());
		//	AdminUserFacade adminUserFacade = getFacade();
			AdminUserFacade adminUserFacade = (AdminUserFacade) CouponSystem.getInstance().login("admin", "1234", ClientType.ADMIN);
			requestMessage = adminUserFacade.insertCompany(company);
			if (requestMessage.indexOf("success") != -1) {
				System.out.println("Company was added in success " + company.getCompanyId());
				return new RequestStatus(true, requestMessage);
			} else {
				throw new AdminServiceException("Admin failed to add company ", company.getCompanyId(),
						company.getCompanyName(), ClientType.COMPANY.toString());
			}
		} catch (AdminServiceException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new RequestStatus(false, requestMessage);

	}

	// remove company
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Path("removeCompany/{companyId}")
	public RequestStatus removeCompany(@PathParam("companyId") long companyId) throws Exception {

		System.out.println("now in removeCompany");

		try {
//			System.out.println(request.getSession(false).getId());
//			AdminUserFacade adminUserFacade = getFacade();
			AdminUserFacade adminUserFacade = (AdminUserFacade) CouponSystem.getInstance().login("admin", "1234", ClientType.ADMIN);
			requestMessage = adminUserFacade.removeCompany(companyId);
			if (requestMessage.indexOf("success") != -1) {
				System.out.println("Company was removed in success " + companyId);
				return new RequestStatus(true, requestMessage);
			} else {
				throw new AdminServiceException("Admin failed to remove company " + companyId);
			}

		} catch (AdminServiceException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new RequestStatus(false, requestMessage);

	}

	// update company
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("updateCompany")
	public RequestStatus updateCompany(String jsonString) throws Exception {

		System.out.println("now in updateCompany");

		try {
		//	System.out.println(request.getSession(false).getId());
			JsonParser parser = new JsonParser();
			JsonObject obj = parser.parse(jsonString).getAsJsonObject();
			long companyId = Long.parseLong(obj.get("companyId").getAsString());
			String newCompanyPassword = obj.get("newCompanyPassword").getAsString();
			String newCompanyEmail = obj.get("newCompanyEmail").getAsString();
		//	AdminUserFacade adminUserFacade = getFacade();
			AdminUserFacade adminUserFacade = (AdminUserFacade) CouponSystem.getInstance().login("admin", "1234", ClientType.ADMIN);
			requestMessage = adminUserFacade.updateCompany(companyId, newCompanyPassword, newCompanyEmail);
			if (requestMessage.indexOf("success") != -1) {
				System.out.println("Company was updated in success " + companyId);
				return new RequestStatus(true, requestMessage);
			} else {
				throw new AdminServiceException("Admin failed to update company " + companyId);
			}

		} catch (AdminServiceException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new RequestStatus(false, requestMessage);

	}

	// get All Companies
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("getAllCompanies")
	public List<Company> getAllCompanies() throws Exception {

		System.out.println("now in getAllCompanies");

		try {
//			System.out.println(request.getSession(false).getId());
//			AdminUserFacade adminUserFacade = getFacade();
			AdminUserFacade adminUserFacade = (AdminUserFacade) CouponSystem.getInstance().login("admin", "1234", ClientType.ADMIN);
			List<Company> companies = adminUserFacade.getAllCompanies();
			if (companies != null) {
				System.out.println("All companies were returned in success ");
				return companies;
			} else {
				throw new AdminServiceException("Admin failed to get all companies ");
			}

		} catch (AdminServiceException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	// get Company
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("getCompany/{companyId}")
	public Company getCompany(@PathParam("companyId") long companyId) throws Exception {

		System.out.println("now in getCompany");

		try {
//			System.out.println(request.getSession(false).getId());
//			AdminUserFacade adminUserFacade = getFacade();
			AdminUserFacade adminUserFacade = (AdminUserFacade) CouponSystem.getInstance().login("admin", "1234", ClientType.ADMIN);
			Company company = adminUserFacade.getCompany(companyId);
			if (company != null) {
				System.out.println("company was returned in success " + companyId);
				return company;
			} else {
				throw new AdminServiceException("Admin failed to get company " + companyId);
			}

		} catch (AdminServiceException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	// add customer
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("addCustomer")
	public RequestStatus addCustomer(Customer customer) throws Exception {

		System.out.println("now in addCustomer");

		try {
//			System.out.println(request.getSession(false).getId());
//			AdminUserFacade adminUserFacade = getFacade();
			AdminUserFacade adminUserFacade = (AdminUserFacade) CouponSystem.getInstance().login("admin", "1234", ClientType.ADMIN);
			requestMessage = adminUserFacade.insertCustomer(customer);
			if (requestMessage.indexOf("success") != -1) {
				System.out.println("Customer was added in success " + customer.getCustomerId());
				return new RequestStatus(true, requestMessage);
			} else {
				throw new AdminServiceException("Admin failed to add customer ", customer.getCustomerId(), customer.getCustomerName(), ClientType.CUSTOMER.toString());
			}

		} catch (AdminServiceException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new RequestStatus(false, requestMessage);

	}

	// remove customer
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Path("removeCustomer/{customerId}")
	public RequestStatus removeCustomer(@PathParam("customerId") long customerId) throws Exception {

		System.out.println("now in removeCustomer");

		try {
//			System.out.println(request.getSession(false).getId());
//			AdminUserFacade adminUserFacade = getFacade();
			AdminUserFacade adminUserFacade = (AdminUserFacade) CouponSystem.getInstance().login("admin", "1234", ClientType.ADMIN);
			requestMessage = adminUserFacade.removeCustomer(customerId);
			if (requestMessage.indexOf("success") != -1) {
				System.out.println("Customer was removed in success " + customerId);
				return new RequestStatus(true, requestMessage);
			} else {
				throw new AdminServiceException("Admin failed to remove customer " + customerId);
			}

		} catch (AdminServiceException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new RequestStatus(false, requestMessage);

	}

	// update customer
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("updateCustomer")
	public RequestStatus updateCustomer(String jsonString) throws Exception {

		System.out.println("now in updateCustomer");

		try {
		//	System.out.println(request.getSession(false).getId());
			JsonParser parser = new JsonParser();
			JsonObject obj = parser.parse(jsonString).getAsJsonObject();
			long customerId = Long.parseLong(obj.get("customerId").getAsString());
			String newCustomerPassword = obj.get("newCustomerPassword").getAsString();
		//	AdminUserFacade adminUserFacade = getFacade();
			AdminUserFacade adminUserFacade = (AdminUserFacade) CouponSystem.getInstance().login("admin", "1234", ClientType.ADMIN);
			requestMessage = adminUserFacade.updateCustomer(customerId, newCustomerPassword);
			if (requestMessage.indexOf("success") != -1) {
				System.out.println("Customer was updated in success " + customerId);
				return new RequestStatus(true, requestMessage);
			} else {
				throw new AdminServiceException("Admin failed to update customer " + customerId);
			}

		} catch (AdminServiceException e) {
			System.out.println(e.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
		}
		return new RequestStatus(false, requestMessage);

	}

	// get All Customers
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("getAllCustomers")
	public List<Customer> getAllCustomers() throws Exception {

		System.out.println("now in getAllCustomers");

		try {
//			System.out.println(request.getSession(false).getId());
//			AdminUserFacade adminUserFacade = getFacade();
			AdminUserFacade adminUserFacade = (AdminUserFacade) CouponSystem.getInstance().login("admin", "1234", ClientType.ADMIN);
			List<Customer> customers = adminUserFacade.getAllCustomers();
			if (customers != null) {
				System.out.println("All customers were returned in success ");
				return customers;
			} else {
				throw new AdminServiceException("Admin failed to get all customers ");
			}

		} catch (AdminServiceException e) {
			System.out.println(e.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	// get Customer
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("getCustomer/{customerId}")
	public Customer getCustomer(@PathParam("customerId") long customerId) throws Exception {

		System.out.println("now in getCustomer");

		try {
//			System.out.println(request.getSession(false).getId());
//			AdminUserFacade adminUserFacade = getFacade();
			AdminUserFacade adminUserFacade = (AdminUserFacade) CouponSystem.getInstance().login("admin", "1234", ClientType.ADMIN);
			Customer customer = adminUserFacade.getCustomer(customerId);
			if (customer != null) {
				System.out.println("customer was returned in success " + customerId);
				return customer;
			} else {
				throw new AdminServiceException("Admin failed to get customer " + customerId);
			}

		} catch (AdminServiceException e) {
			System.out.println(e.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

}
