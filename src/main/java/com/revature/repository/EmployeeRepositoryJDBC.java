package com.revature.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import com.revature.model.Employee;
import com.revature.model.EmployeeRole;
import com.revature.util.ConnectionUtil;

public class EmployeeRepositoryJDBC implements EmployeeRepository {

	private static Logger logger = Logger.getLogger(EmployeeRepositoryJDBC.class);
	
	@Override
	public boolean insert(Employee employee) {
		
		try(Connection connection = ConnectionUtil.getConnection()) {
			int parameterIndex = 0;
			String command = "INSERT INTO USER_T VALUES(NULL,?,?,?,?,?,?)";

			PreparedStatement statement = connection.prepareStatement(command);

			//Set attributes to be inserted
			statement.setString(++parameterIndex, employee.getFirstName().toUpperCase());
			statement.setString(++parameterIndex, employee.getLastName().toUpperCase());
			statement.setString(++parameterIndex, employee.getUsername().toLowerCase());
			statement.setString(++parameterIndex, employee.getPassword());
			statement.setString(++parameterIndex, employee.getEmail().toUpperCase());
			statement.setInt(++parameterIndex, employee.getEmployeeRole().getId());
			
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
		
		try(Connection connection =  ConnectionUtil.getConnection()){
			
			int parameterIndex = 0;
			// 
			String sql = "UPDATE USER_T SET U_ID = NULL, U_FIRSTNAME = ?, U_LASTNAME = ?, U_PASSWORD=?, U_EMAIL=?, UR_ID= ? WHERE U_ID = ?";
			
			PreparedStatement statement = connection.prepareStatement(sql);
			
			statement.setString(++parameterIndex, employee.getFirstName().toUpperCase());
			statement.setString(++parameterIndex, employee.getLastName().toUpperCase());
			statement.setString(++parameterIndex, employee.getUsername().toUpperCase());
			statement.setString(++parameterIndex, employee.getPassword());
			statement.setString(++parameterIndex, employee.getEmail());
			statement.setInt(++parameterIndex, employee.getEmployeeRole().getId());
						
		}catch(SQLException e){
			logger.trace("An error occured when updating Employee information");
		}
		
		return false;
		
	}

	@Override
	public Employee select(int employeeId) {
		
		try(Connection connection = ConnectionUtil.getConnection()) {
			int parameterIndex = 0;
//need to create the join table here to get the employee role type
			String sql = "SELECT U.U_ID, U.U_FIRSTNAME, U.U_LASTNAME, U.U_USERNAME, U.U_PASSWORD, U.U_EMAIL ,U.UR_ID, UR.UR_TYPE FROM USER_T U FULL OUTER JOIN USER_ROLE UR ON U.UR_ID = UR.UR_ID WHERE U_ID = ? ";
			PreparedStatement statement = connection.prepareStatement(sql);
			
			statement.setInt(++parameterIndex, employeeId);
			ResultSet result = statement.executeQuery();

			while(result.next()) {
				return new Employee(
						result.getInt("U_ID"),
						result.getString("U_FIRSTNAME"),
						result.getString("U_LASTNAME"),
						result.getString("U_USERNAME"),
						result.getString("U_PASSWORD"),
						result.getString("U_EMAIL"), 
						new EmployeeRole(result.getInt("UR_ID"),result.getString("UR_TYPE"))
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
			int parameterIndex = 0;
			
			String sql = "SELECT U.U_ID, U.U_FIRSTNAME, U.U_LASTNAME, U.U_USERNAME, U.U_PASSWORD, U.U_EMAIL ,U.UR_ID, UR.UR_TYPE FROM USER_T U FULL OUTER JOIN USER_ROLE UR ON U.UR_ID = UR.UR_ID WHERE USERNAME = ? ";
			PreparedStatement statement = connection.prepareStatement(sql);
			
			statement.setString(++parameterIndex, username);
			ResultSet result = statement.executeQuery();

			while(result.next()) {
				return new Employee(
						result.getInt("U_ID"),
						result.getString("U_FIRSTNAME"),
						result.getString("U_LASTNAME"),
						result.getString("U_USERNAME"),
						result.getString("U_PASSWORD"),
						result.getString("U_EMAIL"), 
						new EmployeeRole(result.getInt("UR_ID"),result.getString("UR_TYPE"))
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
			String sql = "SELECT U.U_ID, U.U_FIRSTNAME, U.U_LASTNAME, U.U_USERNAME, U.U_PASSWORD, U.U_EMAIL ,U.UR_ID, UR.UR_TYPE FROM USER_T U FULL OUTER JOIN USER_ROLE UR ON U.UR_ID = UR.UR_ID";
			
			PreparedStatement statement = connection.prepareStatement(sql);
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
						new EmployeeRole(result.getInt("UR_ID"), result.getString("UR_TYPE"))
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
		
		try(Connection connection = ConnectionUtil.getConnection()){
			int parameterIndex=0;
			
			String sql="SELECT GET_HASH(?) AS HASHEDPASSWORD FROM DUAL";
			
			PreparedStatement statement = connection.prepareStatement(sql);
			
			statement.setString(++parameterIndex, employee.getPassword());
			ResultSet result = statement.executeQuery();
			
			if(result.next()) {
				return result.getString("HASHEDPASSWORD");
			}
			
		}catch(SQLException e){
			logger.info("Issue when retrieving password occurred."+ e);
			}
		return new String();
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
