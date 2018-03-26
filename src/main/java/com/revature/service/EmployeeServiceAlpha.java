package com.revature.service;

import java.util.Set;

import com.revature.model.Employee;
import com.revature.repository.EmployeeRepositoryJDBC;

public class EmployeeServiceAlpha implements EmployeeService {

	private static EmployeeService employeeService = new EmployeeServiceAlpha();
	
	private EmployeeServiceAlpha(){}
	
	public static EmployeeService getInstance(){
		
		return employeeService;
	}
	
	@Override
	public Employee authenticate(Employee employee) {
		
		Employee loggedEmployee = EmployeeRepositoryJDBC.getInstance().select(employee.getUsername());
		
			if(loggedEmployee.getPassword().equals(EmployeeRepositoryJDBC.getInstance().getPasswordHash(employee))){
			
			return loggedEmployee;
		}
		return null;
	}
	
	@Override
	public Employee getEmployeeInformation(Employee employee) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Employee> getAllEmployeesInformation() {
		// TODO Auto-generated method stub
		return null;
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
