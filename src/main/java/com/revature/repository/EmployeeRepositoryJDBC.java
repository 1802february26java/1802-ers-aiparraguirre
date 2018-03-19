package com.revature.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import com.revature.model.Employee;
import com.revature.util.ConnectionUtil;

public class EmployeeRepositoryJDBC implements EmployeeRepository {

	private static Logger logger = Logger.getLogger(EmployeeRepositoryJDBC.class);
	
	@Override
	public boolean insert(Employee employee) {
		
		try(Connection connection = ConnectionUtil.getConnection()) {
			int statementIndex = 0;
			String command = "INSERT INTO USER_T VALUES(NULL,?,?,?,?,?)";

			PreparedStatement statement = connection.prepareStatement(command);

			//Set attributes to be inserted
			statement.setString(++statementIndex, employee.getFirstName().toUpperCase());
			statement.setString(++statementIndex, employee.getLastName().toUpperCase());
			statement.setString(++statementIndex, employee.getUsername().toLowerCase());
			statement.setString(++statementIndex, employee.getPassword());
			statement.setString(++statementIndex, employee.getEmail().toUpperCase());
			
			if(statement.executeUpdate() > 0) {
				return true;
			}
		} catch (SQLException e) {
			logger.warn("Exception creating a new employee", e);
		}
		return false;
	}

	@Override
	public boolean update(Employee employee) {
		
		return false;
		
	}

	@Override
	public Employee select(int employeeId) {
		
		try(Connection connection = ConnectionUtil.getConnection()) {
			int statementIndex = 0;
//need to create the join table here to get the employee role type
			String command = "SELECT * FROM USER_T WHERE U_ID = ?";
			PreparedStatement statement = connection.prepareStatement(command);
			
			statement.setInt(++statementIndex, employeeId);
			ResultSet result = statement.executeQuery();

			while(result.next()) {
				return new Employee(
						result.getInt("U_ID"),
						result.getString("U_FIRSTNAME"),
						result.getString("U_LASTNAME"),
						result.getString("U_USERNAME"),
						result.getString("U_PASSWORD"),
						result.getString("U_EMAIL"), null
						);
			}
		} catch (SQLException e) {
			logger.warn("Exception selecting a User using Employee I.D.", e);
		}
		return new Employee();
	}

	

	@Override
	public Employee select(String username) {
		try(Connection connection = ConnectionUtil.getConnection()) {
			int statementIndex = 0;
			
//need to create the join table here to get the employee role type
			String command = "SELECT * FROM USER_T WHERE U_USERNAME= ?";
			PreparedStatement statement = connection.prepareStatement(command);
			
			statement.setString(++statementIndex, username);
			ResultSet result = statement.executeQuery();

			while(result.next()) {
				return new Employee(
						result.getInt("U_ID"),
						result.getString("U_FIRSTNAME"),
						result.getString("U_LASTNAME"),
						result.getString("U_USERNAME"),
						result.getString("U_PASSWORD"),
						result.getString("U_EMAIL"), null
						);
			}
		} catch (SQLException e) {
			logger.warn("Exception selecting a User using Username.", e);
		}
		return new Employee();
	}
	

	@Override
	public Set<Employee> selectAll() {
		
		try(Connection connection = ConnectionUtil.getConnection()) {
			String command = "SELECT * FROM USER";
			PreparedStatement statement = connection.prepareStatement(command);
			ResultSet result = statement.executeQuery();

			Set<Employee> employeeList = new HashSet<>();
			while(result.next()) {
				employeeList.add(new Employee(
						result.getInt("U_ID"),
						result.getString("U_FIRSTNAME"),
						result.getString("U_LASTNAME"),
						result.getString("U_USERNAME"),
						result.getString("U_PASSWORD"), 
						result.getString("U_EMAIL"),
						null
						));
			}

			return (Set<Employee>) employeeList;
		} catch (SQLException e) {
			logger.warn("Exception selecting all Users", e);
		} 
		return new HashSet<>();
	}	
	

	@Override
	public String getPasswordHash(Employee employee) {
		// TODO Auto-generated method stub
		return null;
	}
/*
	@Override
	public boolean insertEmployeeToken(EmployeeToken employeeToken) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteEmployeeToken(EmployeeToken employeeToken) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public EmployeeToken selectEmployeeToken(EmployeeToken employeeToken) {
		// TODO Auto-generated method stub
		return null;
	}*/

}
