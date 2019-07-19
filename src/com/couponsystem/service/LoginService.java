package com.couponsystem.service;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.couponsystem.bean.Company;
import com.couponsystem.bean.Customer;
import com.couponsystem.dao.CompanyDAO;
import com.couponsystem.dao.CustomerDAO;
import com.couponsystem.dbdao.CompanyDBDAO;
import com.couponsystem.dbdao.CustomerDBDAO;
import com.couponsystem.exceptions.LoginCouponSystemException;
import com.couponsystem.facade.AdminUserFacade;
import com.couponsystem.facade.CompanyUserFacade;
import com.couponsystem.facade.CouponClientFacade;
import com.couponsystem.facade.CustomerUserFacade;
import com.couponsystem.utils.ClientType;
import com.couponsystem.utils.CouponSystem;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Path("/login")
public class LoginService {

	private CouponSystem system;
	private String jsessionid;
	private CompanyDAO companyLoginDAO = new CompanyDBDAO();
	private CustomerDAO customerLoginDAO = new CustomerDBDAO();

	@Context
	private HttpServletRequest request;

	@Context
	private HttpServletResponse response;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String userLogin(String jsonString) {

		System.out.println("now in userLogin");

		try {
			system = CouponSystem.getInstance();
			System.out.println("Loaded...");

			HttpSession session = request.getSession(false);
			if (session != null) {
				session.invalidate();
			}
			session = request.getSession(true);
			System.out.println(session.getId() + " * " + session.getMaxInactiveInterval());

			JsonParser parser = new JsonParser();
			JsonObject obj = parser.parse(jsonString).getAsJsonObject();
			String username = obj.get("username").getAsString();
			String password = obj.get("password").getAsString();
			String clientType = obj.get("clientType").getAsString();
			ClientType cType = ClientType.valueOf(clientType);

			CouponClientFacade facade = system.login(username, password, cType);

			System.out.println("LoginService: request = " + request);
			System.out.println("LoginService: response = " + response);

			if (facade != null) {

				Cookie cookie = new Cookie("Set-Cookie", "JSESSIONID=" + request.getSession().getId()
						+ ";path=/CouponSystemREST/rest; HttpOnly; domain=/localhost; secure=false;");
				cookie.setComment(clientType);
				String response = new Gson().toJson(cookie);

				switch (cType) {

				case ADMIN:
					if (username.equals("admin") && password.equals("1234")) {
						AdminUserFacade adminF = new AdminUserFacade();
						session.setAttribute("facade", adminF);
						System.out.println(response);
						return response;
					}
					break;
				case COMPANY:

					/* Take the list of companies from DB */
					List<Company> companies = companyLoginDAO.getAllCompanies();
					Iterator<Company> i = companies.iterator();

					/* For any company in DB check the validity of the parameters values */
					while (i.hasNext()) {
						Company current = i.next();
						if (current.getCompanyName().equals(username)
								&& current.getCompanyPassword().equals(password)) {
							CompanyUserFacade companyF = new CompanyUserFacade(current);
							session.setAttribute("facade", companyF);
							System.out.println(response);
							return response;
						}

					}
					break;
				case CUSTOMER:
					/* Take the list of customers from DB */
					List<Customer> customers = customerLoginDAO.getAllCustomers();
					Iterator<Customer> it = customers.iterator();

					/* For any customer in DB check the validity of the parameters values */
					while (it.hasNext()) {
						Customer current = it.next();
						if (current.getCustomerName().equals(username)
								&& current.getCustomerPassword().equals(password)) {
							CustomerUserFacade customerF = new CustomerUserFacade(current);
							session.setAttribute("facade", customerF);
							System.out.println(response);
							return response;
						}
					}
					break;

				default:
					break;

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
		return null;

	}
}
