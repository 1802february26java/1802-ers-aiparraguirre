package com.revature.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.revature.ajax.EmployeeMessage;
import com.revature.ajax.MessageUtil;
import com.revature.model.Employee;
import com.revature.service.EmployeeServiceAlpha;

public class LoginControllerAlpha implements LoginController {

	private static Logger logger = Logger.getLogger(LoginControllerAlpha.class);
	
    private static LoginController loginController = new LoginControllerAlpha();
	
	private LoginControllerAlpha(){}
	
	public static LoginController getInstance(){
		return loginController;
	}
	
	@Override
	public Object login(HttpServletRequest request) {
		
		//logger.trace("Login Controller login check");
		
		Employee employee = new Employee();
		
		if (request.getMethod().equals("GET")){
			return "login.html";
		}
		
		employee.setUsername(request.getParameter("username"));
		employee.setPassword(request.getParameter("password"));
		
		//logger.trace("Username made it to here from login page: "+ employee.getUsername());
		
		Employee loggedEmployee = EmployeeServiceAlpha.getInstance().authenticate(employee);
		
		logger.trace("Logged employee variable from authenticate: " +loggedEmployee);
		
		if(loggedEmployee == null){
			
			logger.trace("Not an employee returning a fail message");
			logger.trace(MessageUtil.EMPLOYEE_LOGIN_FAILED);
			return  new EmployeeMessage(MessageUtil.EMPLOYEE_LOGIN_FAILED);
		}
		
		request.getSession().setAttribute("loggedEmployee", loggedEmployee);
		
		return loggedEmployee;
	}

	@Override
	public String logout(HttpServletRequest request) {
		
		request.getSession().invalidate();
		return "login.html";
	}

}
