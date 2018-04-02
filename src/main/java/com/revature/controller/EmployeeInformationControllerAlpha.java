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
		
		if(request.getSession().getAttribute("loggedEmployee")== null){
			return "login.html";
		}else if(request.getParameter("fetch") == null) {
			
			return "viewAllEmployees.html"; 
		} else {
			logger.trace("Employee info controller: Retrieving  all employee information");
		return EmployeeServiceAlpha.getInstance().getAllEmployeesInformation();
}
	}

	@Override
	public Object usernameExists(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

}
