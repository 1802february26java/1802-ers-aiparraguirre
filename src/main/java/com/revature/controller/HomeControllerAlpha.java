package com.revature.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.revature.model.Employee;

public class HomeControllerAlpha implements HomeController {
	
	private static Logger logger= Logger.getLogger(HomeControllerAlpha.class);
	
	private static HomeController homeController = new HomeControllerAlpha();

	private HomeControllerAlpha() {}

	public static HomeController getInstance() {

		return homeController;

	}
	
	@Override
	public String showEmployeeHome(HttpServletRequest request) {
		Employee loggedEmployee = (Employee) request.getSession().getAttribute("loggedEmployee");
		
		
		logger.trace("Reached the homecontroller and emp id is: " + loggedEmployee.getEmployeeRole().getId());
		if(loggedEmployee.getEmployeeRole().getId()==1) {
			logger.trace("Returning homeEmployee.html");
				
			return "homeEmployee.html";

		}else if( loggedEmployee.getEmployeeRole().getId()==2){
			logger.trace("Returning homeManager.html");
			return "homeManager.html";

		}else{
			return "login.html";
		}
	}

}
