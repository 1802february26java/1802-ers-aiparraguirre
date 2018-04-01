package com.revature.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.revature.model.Employee;
import com.revature.repository.EmployeeRepositoryJDBC;
import com.revature.service.EmployeeService;
import com.revature.service.EmployeeServiceAlpha;

public class EmployeeInformationControllerAlpha implements EmployeeInformationController {

	private static Logger logger = Logger.getLogger(EmployeeRepositoryJDBC.class);
	
	private static EmployeeInformationControllerAlpha employeeICA = new EmployeeInformationControllerAlpha();
	
	private EmployeeInformationControllerAlpha(){}
	
	public static EmployeeInformationControllerAlpha getInstance(){
		return employeeICA;
	}
	
	private EmployeeService employeeService = EmployeeServiceAlpha.getInstance();
	
	
	@Override
	public Object registerEmployee(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object updateEmployee(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}
//-------------------------------------------------------------------------------------------------------------------------------
	@Override
	public Object viewEmployeeInformation(HttpServletRequest request) {
		/**
		 * 1 will take the request and check to see if it is a logged employee
		 * 
		 * if it is null return login page 
		 * 
		 * else we pass to service the employee pojo 
		 * return the model forward
		 * 2 return specific employee data to front end will need to do this by calling the employee DAO
		 */
		logger.trace("Within the EmpInfoController: " + request.getSession().getAttribute("loggedEmployee"));
		Employee employee = (Employee) request.getSession().getAttribute("loggedEmployee");
		
		if(request.getSession().getAttribute("loggedEmployee")== null){
			return "login.html";
		}else {
			return EmployeeServiceAlpha.getInstance().getEmployeeInformation(employee);
		}
	}

	@Override
	public Object viewAllEmployees(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object usernameExists(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

}
