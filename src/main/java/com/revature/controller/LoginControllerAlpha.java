package com.revature.controller;

import javax.servlet.http.HttpServletRequest;

import com.revature.ajax.EmployeeMessage;
import com.revature.model.Employee;
import com.revature.service.EmployeeServiceAlpha;

public class LoginControllerAlpha implements LoginController {

//private static LoginController loginController = new LoginControllerAlpha();
	
	//private LoginControllerAlpha(){}
	
	//public static LoginController getInstance(){
	//	return loginController;
	//}
	
	@Override
	public String login(HttpServletRequest request) {
		
		Employee employee = new Employee();
		
		if (request.getMethod().equals("GET")){
			return "login.html";
		}
		
		employee.setUsername(request.getParameter("username"));
		employee.setPassword(request.getParameter("password"));
		
		Employee loggedEmployee = EmployeeServiceAlpha.getInstance().authenticate(employee);
		
		if(loggedEmployee == null){
			return  EmployeeMessage.EMPLOYEE_LOGIN_FAILED;
		}
		
		request.getSession().setAttribute("loggedEmployee", loggedEmployee);
		
		return EmployeeMessage.EMPLOYEE_LOGIN_SUCCESSFUL;
	}

	@Override
	public String logout(HttpServletRequest request) {
		
		request.getSession().invalidate();
		return "login.html";
	}

}
