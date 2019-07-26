//package com.couponsystem.servlet;
//
//import java.io.IOException;
//
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//
//import com.couponsystem.facade.CouponClientFacade;
//import com.couponsystem.utils.ClientType;
//import com.couponsystem.utils.CouponSystem;
//
//@WebServlet("/login")
//public class LoginServlet extends HttpServlet {
//
//	private CouponSystem system;
//
//	public LoginServlet() {
//
//	}
//
//	public void init() throws ServletException {
//
//	
//		try {
//			system = CouponSystem.getInstance();
//		} catch (Exception e) {
//			System.out.println("Failed to connect to db, Failed to load system");
//			// system.exit(1);
//		}
//		System.out.println("Loaded...");
//	}
//
//	protected void service(HttpServletRequest request, HttpServletResponse response)
//			throws ServletException, IOException {
//
//		HttpSession session = request.getSession(false);
//		//session.setMaxInactiveInterval(2 * 60 * 60); // two hours
//		if (session != null) {
//			session.invalidate();
//		}
//
//		session = request.getSession(true);
//		
//		
//
//		System.out.println(session.getId() + " * " + session.getMaxInactiveInterval());
//
//		String username = request.getParameter("username");
//		String password = request.getParameter("password");
//		String clientType = request.getParameter("clientType");
//		ClientType cType = ClientType.valueOf(clientType);
//
//		try {
//
//			CouponClientFacade facade = system.login(username, password, cType);
//
//			System.out.println("LoginServlet: request = " + request);
//			System.out.println("LoginServlet: response = " + response);
//
//			if (facade != null) {
//
//				session.setAttribute("facade", facade);
//
//				switch (cType) {
//
//				case ADMIN:
////					response.sendRedirect("http://localhost:8080/CouponSystemREST/rest/admin");
//					response.sendRedirect("thankYou.html");
//					request.getRequestDispatcher("rest/admin").forward(request, response);
//					break;
//
//				case COMPANY:
//					response.sendRedirect("thankYou.html");
//					request.getRequestDispatcher("rest/company").forward(request, response);
//					break;
//
//				case CUSTOMER:
//					response.sendRedirect("thankYou.html");
//					request.getRequestDispatcher("rest/customer").forward(request, response);
//					break;
//
//				default:
//					break;
//				}
//
//			}
//
//			else {
//				response.sendRedirect("Login.html");
//
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//	}
//
//}
