package com.revature.service;

import java.util.Set;

import org.apache.log4j.Logger;

import com.revature.model.Employee;
import com.revature.repository.EmployeeRepositoryJDBC;

public class EmployeeServiceAlpha implements EmployeeService {
	private static Logger logger = Logger.getLogger(EmployeeServiceAlpha.class);
	
	private static EmployeeService employeeService = new EmployeeServiceAlpha();
	
	private EmployeeServiceAlpha(){}
	
	public static EmployeeService getInstance(){
		
		return employeeService;
	}
	
	@Override
	public Employee authenticate(Employee employee) {
		
		Employee loggedEmployee = EmployeeRepositoryJDBC.getInstance().select(employee.getUsername());
		
		logger.trace("Username made it to AUTHENTICATE from login page: "+ employee.getUsername());

		logger.trace("Logged employee variable from authenticate: " +loggedEmployee);
		
		if(loggedEmployee!=null && loggedEmployee.getPassword().equals(EmployeeRepositoryJDBC.getInstance().getPasswordHash(employee))){
			logger.trace("password has been checked and we are in authenticate if statement");
			return loggedEmployee;
		}else
			return null;
	}
	
	@Override
	public Employee getEmployeeInformation(Employee employee) {
		
		logger.trace("Passing Employee Info. from service to DAO");
		
		logger.trace("Passing Employee Info. from DAO to Controller");
	
		return EmployeeRepositoryJDBC.getInstance().select(employee.getId());
		
	}

	@Override
	public Set<Employee> getAllEmployeesInformation() {
		logger.trace("Employee Service: Retrieving  all employee information");
		return EmployeeRepositoryJDBC.getInstance().selectAll();
	}

	@Override
	public boolean createEmployee(Employee employee) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateEmployeeInformation(Employee employee) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updatePassword(Employee employee) {
		// TODO Auto-generated method stub
		return false;
	}
/*
	@Override
	public boolean isUsernameTaken(Employee employee) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean createPasswordToken(Employee employee) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deletePasswordToken(EmployeeToken employeeToken) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isTokenExpired(EmployeeToken employeeToken) {
		// TODO Auto-generated method stub
		return false;
	}
*/
}
