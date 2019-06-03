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
import com.couponsystem.facade.AdminUserFacade;
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

		try {
			System.out.println(request.getSession(false).getId());
			AdminUserFacade adminUserFacade = getFacade();
			if (adminUserFacade.insertCompany(company) != null) {
				System.out.println("Company was added in success " + company.getCompanyId());
				return new RequestStatus(true);
			} else {
				throw new Exception("Admin failed to add company " + company.getCompanyId());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return new RequestStatus(false);

	}

	// remove company
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Path("removeCompany/{companyId}")
	public RequestStatus removeCompany(@PathParam("companyId") long companyId) throws Exception {

		System.out.println("now in removeCompany");

		try {
			System.out.println(request.getSession(false).getId());
			AdminUserFacade adminUserFacade = getFacade();
			if (adminUserFacade.removeCompany(companyId) != null) {
				System.out.println("Company was removed in success " + companyId);
				return new RequestStatus(true);
			} else {
				throw new Exception("Admin failed to remove company " + companyId);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return new RequestStatus(false);

	}

	// update company
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("updateCompany/{companyId}/{newCompanyPassword}/{newCompanyEmail}")
	public RequestStatus updateCompany(@PathParam("companyId") long companyId,
			@PathParam("newCompanyPassword") String newCompanyPassword,
			@PathParam("newCompanyEmail") String newCompanyEmail) throws Exception {

		System.out.println("now in updateCompany");

		try {
			System.out.println(request.getSession(false).getId());
			AdminUserFacade adminUserFacade = getFacade();
			if (adminUserFacade.updateCompany(companyId, newCompanyPassword, newCompanyEmail) != null) {
				System.out.println("Company was updated in success " + companyId);
				return new RequestStatus(true);
			} else {
				throw new Exception("Admin failed to update company " + companyId);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return new RequestStatus(false);

	}

	// get All Companies
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("getAllCompanies")
	public List<Company> getAllCompanies() throws Exception {

		System.out.println("now in getAllCompanies");

		try {
			System.out.println(request.getSession(false).getId());
			AdminUserFacade adminUserFacade = getFacade();
			List<Company> companies = adminUserFacade.getAllCompanies();
			if (companies != null) {
				System.out.println("All companies were returned in success ");
				return companies;
			} else {
				throw new Exception("Admin failed to get all companies ");
			}

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
			System.out.println(request.getSession(false).getId());
			AdminUserFacade adminUserFacade = getFacade();
			Company company = adminUserFacade.getCompany(companyId);
			if (company != null) {
				System.out.println("company was returned in success " + companyId);
				return company;
			} else {
				throw new Exception("Admin failed to get company " + companyId);
			}

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
			System.out.println(request.getSession(false).getId());
			AdminUserFacade adminUserFacade = getFacade();
			if (adminUserFacade.insertCustomer(customer) != null) {
				System.out.println("Customer was added in success " + customer.getCustomerId());
				return new RequestStatus(true);
			} else {
				throw new Exception("Admin failed to add customer " + customer.getCustomerId());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return new RequestStatus(false);

	}

	// remove customer
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Path("removeCustomer/{customerId}")
	public RequestStatus removeCustomer(@PathParam("customerId") long customerId) throws Exception {

		System.out.println("now in removeCustomer");

		try {
			System.out.println(request.getSession(false).getId());
			AdminUserFacade adminUserFacade = getFacade();
			if (adminUserFacade.removeCustomer(customerId) != null) {
				System.out.println("Customer was removed in success " + customerId);
				return new RequestStatus(true);
			} else {
				throw new Exception("Admin failed to remove customer " + customerId);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return new RequestStatus(false);

	}

	// update customer
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("updateCustomer/{customerId}/{newCustomerPassword}")
	public RequestStatus updateCustomer(@PathParam("customerId") long customerId,
			@PathParam("newCustomerPassword") String newCustomerPassword) throws Exception {

		System.out.println("now in updateCustomer");

		try {
			System.out.println(request.getSession(false).getId());
			AdminUserFacade adminUserFacade = getFacade();
			if (adminUserFacade.updateCustomer(customerId, newCustomerPassword) != null) {
				System.out.println("Customer was updated in success " + customerId);
				return new RequestStatus(true);
			} else {
				throw new Exception("Admin failed to update customer " + customerId);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return new RequestStatus(false);

	}

	// get All Customers
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("getAllCustomers")
	public List<Customer> getAllCustomers() throws Exception {

		System.out.println("now in getAllCustomers");

		try {
			System.out.println(request.getSession(false).getId());
			AdminUserFacade adminUserFacade = getFacade();
			List<Customer> customers = adminUserFacade.getAllCustomers();
			if (customers != null) {
				System.out.println("All customers were returned in success ");
				return customers;
			} else {
				throw new Exception("Admin failed to get all customers ");
			}

		} catch (Exception e) {
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
			System.out.println(request.getSession(false).getId());
			AdminUserFacade adminUserFacade = getFacade();
			Customer customer = adminUserFacade.getCustomer(customerId);
			if (customer != null) {
				System.out.println("customer was returned in success " + customerId);
				return customer;
			} else {
				throw new Exception("Admin failed to get customer " + customerId);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

}
