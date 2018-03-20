package com.revature.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import com.revature.model.Employee;
import com.revature.model.Reimbursement;
import com.revature.model.ReimbursementStatus;
import com.revature.model.ReimbursementType;
import com.revature.util.ConnectionUtil;

public class ReimbursementRepositoryJDBC implements ReimbursementRepository {


	private static Logger logger = Logger.getLogger(ReimbursementRepositoryJDBC.class);
	
	@Override
	public boolean insert(Reimbursement reimbursement) {
		try(Connection connection = ConnectionUtil.getConnection()) {
			int parameterIndex = 0;
		
			String sql = "INSERT INTO REIMBURSEMENT VALUES(NULL,?,?,?,?,NULL,?,?,?,?)";

			PreparedStatement statement = connection.prepareStatement(sql);

			//Set attributes to be inserted
			statement.setTimestamp(++parameterIndex, Timestamp.valueOf(reimbursement.getRequested()));
			statement.setTimestamp(++parameterIndex, Timestamp.valueOf(reimbursement.getResolved()));
			statement.setDouble(++parameterIndex, reimbursement.getAmount());
			statement.setString(++parameterIndex, reimbursement.getDescription());
			statement.setInt(++parameterIndex, reimbursement.getRequester().getId());
			statement.setInt(++parameterIndex, reimbursement.getApprover().getId());
			statement.setInt(++parameterIndex, reimbursement.getStatus().getId());
			statement.setInt(++parameterIndex, reimbursement.getType().getId());

			if(statement.executeUpdate() > 0) {
				return true;
			}
		} catch (SQLException e) {
			logger.info("Exception creating a new reimbursement", e);
		}
		return false;
	}
	@Override
	public boolean update(Reimbursement reimbursement) {
		try(Connection connection = ConnectionUtil.getConnection()){
			
			int parameterIndex=0;
			String sql ="UPDATE REIMBURSEMENT SET R_ID= NULL, R_REQUESTED=?, R_RESOLVED = ?, R_AMOUNT=?, R_DESCRIPTION=?, R_RECIEPT = NULL, EMPLOYEE_ID=?, MANAGER_ID =?, RS_ID=?, RT_ID =?";
			
			PreparedStatement statement = connection.prepareStatement(sql);
			
			statement.setTimestamp(++parameterIndex, Timestamp.valueOf(reimbursement.getRequested()));
			statement.setTimestamp(++parameterIndex, Timestamp.valueOf(reimbursement.getResolved()));
			statement.setDouble(++parameterIndex, reimbursement.getAmount());
			statement.setString(++parameterIndex, reimbursement.getDescription());
			statement.setInt(++parameterIndex, reimbursement.getRequester().getId());
			statement.setInt(++parameterIndex, reimbursement.getApprover().getId());
			statement.setInt(++parameterIndex, reimbursement.getStatus().getId());
			statement.setInt(++parameterIndex, reimbursement.getType().getId());
			
			if(statement.executeUpdate() > 0) {
				return true;
			}
		}catch(SQLException e){
			logger.info("Error Inserting into Reimbursement table" + e);
		}
		return false;
	}

	@Override
	public Reimbursement select(int reimbursementId) {
		
		try(Connection connection = ConnectionUtil.getConnection()){
			
			String sql ="SELECT R.R_ID, R.R_REQUESTED, R.R_RESOLVED, R.R_AMOUNT, R.R_DESCRIPTION,"
						+" R.R_RECEIPT, R.EMPLOYEE_ID, R.MANAGER_ID, RS.RS_RS_ID,RS.RS_STATUS, RT.RT_ID,RT.RT_TYPE "
						+"FROM REIMBURSEMENT R FULL JOIN REIMBURSEMENT_TYPE RT ON (R.RT_ID = RT.RT_ID)"
						+"FULL JOIN REIMBURSEMENT_STATUS RS ON (R.RS_ID=RS.RS_ID)"
						+"WHERE R.R_ID = ?";
			
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, reimbursementId);
			
			ResultSet result = statement.executeQuery();
			
			while(result.next()){
				return new Reimbursement(
						result.getInt("R.R_ID"),
						result.getTimestamp("R.R_REQUESTED"),
						result.getTimestamp("R.R_RESOLVED"),
						result.getDouble("R.R_AMOUNT"),
						result.getString("R.R_DESCRIPTION"),
						result.getObject("R.R_RECEIPT"),
						new Employee.setId(result.getInt("R.EMPLOYEE_ID")),
						new Employee.setId(result.getInt("R.MANAGER_ID")),
						new ReimbursementStatus(result.getInt("RS.RS_ID"), result.getString("RS.RS_STATUS")),
						new ReimbursementType(result.getInt("RT.RT_ID"), result.getString("RT.RT_TYPE")),
						result.getString("RT.RT_TYPE")
						);
			}
			
		}
		catch(SQLException e){
			logger.info("Error Selecting Reimbursement by ID",e);
		}
		return null;
	}

	@Override
	public Set<Reimbursement> selectPending(int employeeId) {
		
		try(Connection connection = ConnectionUtil.getConnection()){
			
			int parameterIndex=0;
			String pending = "PENDING";
			String sql ="SELECT R.R_ID, R.R_REQUESTED, R.R_RESOLVED, R.R_AMOUNT, R.R_DESCRIPTION,"
						+" R.R_RECEIPT, R.EMPLOYEE_ID, R.MANAGER_ID, RS.RS_RS_ID,RS.RS_STATUS, RT.RT_ID,RT.RT_TYPE "
						+"FROM REIMBURSEMENT R FULL JOIN REIMBURSEMENT_TYPE RT ON (R.RT_ID = RT.RT_ID)"
						+"FULL JOIN REIMBURSEMENT_STATUS RS ON (R.RS_ID=RS.RS_ID)"
						+"WHERE R.EMPLOYEE_ID = ?";
			
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(++parameterIndex, employeeId);
			ResultSet result = statement.executeQuery();
			
			Set<Employee> employeePendingList = new HashSet<>();
			
			while(result.next()){
				
				if(result.getString("RS.RS_STATUS").equals(pending)){
					return employeePendingList.add(new Reimbursement(
						result.getInt("R.R_ID"),
						result.getTimestamp("R.R_REQUESTED"),
						result.getTimestamp("R.R_RESOLVED"),
						result.getDouble("R.R_AMOUNT"),
						result.getString("R.R_DESCRIPTION"),
						result.getObject("R.R_RECEIPT"),
						new Employee(result.getInt("R.EMPLOYEE_ID")),
						new Employee.setId(result.getInt("R.MANAGER_ID")),
						new ReimbursementStatus(result.getInt("RS.RS_ID"), result.getString("RS.RS_STATUS")),
						new ReimbursementType(result.getInt("RT.RT_ID"), result.getString("RT.RT_TYPE")),
						result.getString("RT.RT_TYPE")
						)
					);
				}
			}
			
		}
		catch(SQLException e){
			logger.info("Error Selecting Reimbursement by Pending",e);
		}
		return null;
	}
	

	@Override
	public Set<Reimbursement> selectFinalized(int employeeId) {
		
		try(Connection connection = ConnectionUtil.getConnection()){
			
			int parameterIndex=0;
			String status = "APPROVED";
		
			String sql ="SELECT R.R_ID, R.R_REQUESTED, R.R_RESOLVED, R.R_AMOUNT, R.R_DESCRIPTION,"
						+" R.R_RECEIPT, R.EMPLOYEE_ID, R.MANAGER_ID, RS.RS_RS_ID,RS.RS_STATUS, RT.RT_ID,RT.RT_TYPE "
						+"FROM REIMBURSEMENT R FULL JOIN REIMBURSEMENT_TYPE RT ON (R.RT_ID = RT.RT_ID)"
						+"FULL JOIN REIMBURSEMENT_STATUS RS ON (R.RS_ID=RS.RS_ID)"
						+"WHERE R.EMPLOYEE_ID = ?";
			
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(++parameterIndex, employeeId);
			ResultSet result = statement.executeQuery();
			
			Set<Employee> reimbursementApprovedList = new HashSet<>();
			
			while(result.next()){
				
				if(result.getString("RS.RS_STATUS").equals(status)){
					return reimbursementApprovedList.add(new Reimbursement(
						result.getInt("R.R_ID"),
						result.getTimestamp("R.R_REQUESTED"),
						result.getTimestamp("R.R_RESOLVED"),
						result.getDouble("R.R_AMOUNT"),
						result.getString("R.R_DESCRIPTION"),
						result.getObject("R.R_RECEIPT"),
						new Employee(result.getInt("R.EMPLOYEE_ID")),
						new Employee.setId(result.getInt("R.EMPLOYEE_ID")),
						new ReimbursementStatus(result.getInt("RS.RS_ID"), result.getString("RS.RS_STATUS")),
						new ReimbursementType(result.getInt("RT.RT_ID"), result.getString("RT.RT_TYPE")),
						result.getString("RT.RT_TYPE")
						)
					);
				}
			}
			
		}
		catch(SQLException e){
			logger.info("Error Selecting Reimbursement that are finalized",e);
		}
		return null;
	}

	@Override
	public Set<Reimbursement> selectAllPending() {
		
		try(Connection connection = ConnectionUtil.getConnection()){
			
			String status = "PENDING";
		
			String sql ="SELECT R.R_ID, R.R_REQUESTED, R.R_RESOLVED, R.R_AMOUNT, R.R_DESCRIPTION,"
						+" R.R_RECEIPT, R.EMPLOYEE_ID, R.MANAGER_ID, RS.RS_RS_ID,RS.RS_STATUS, RT.RT_ID,RT.RT_TYPE "
						+"FROM REIMBURSEMENT R FULL JOIN REIMBURSEMENT_TYPE RT ON (R.RT_ID = RT.RT_ID)"
						+"FULL JOIN REIMBURSEMENT_STATUS RS ON (R.RS_ID=RS.RS_ID)"
						+"WHERE RS.RS_STATUS = ?";
			
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(0, status);
			ResultSet result = statement.executeQuery();
			
			Set<Employee> employeePendingList = new HashSet<>();
			
			while(result.next()){
					return employeePendingList.add(new Reimbursement(
						result.getInt("R.R_ID"),
						result.getTimestamp("R.R_REQUESTED"),
						result.getTimestamp("R.R_RESOLVED"),
						result.getDouble("R.R_AMOUNT"),
						result.getString("R.R_DESCRIPTION"),
						result.getObject("R.R_RECEIPT"),
						new Employee(result.getInt("R.EMPLOYEE_ID")),
						new Employee.setId(result.getInt("R.EMPLOYEE_ID")),
						new ReimbursementStatus(result.getInt("RS.RS_ID"), result.getString("RS.RS_STATUS")),
						new ReimbursementType(result.getInt("RT.RT_ID"), result.getString("RT.RT_TYPE")),
						result.getString("RT.RT_TYPE")
						)
					);
				}
			}catch(SQLException e){
				logger.info("Exception selecting all Reimbursements that are Pending",e);
		}
		return null;
	}

	@Override
	public Set<Reimbursement> selectAllFinalized() {
		
		try(Connection connection = ConnectionUtil.getConnection()){
			
			String status = "APPROVED";
		
			String sql ="SELECT R.R_ID, R.R_REQUESTED, R.R_RESOLVED, R.R_AMOUNT, R.R_DESCRIPTION,"
						+" R.R_RECEIPT, R.EMPLOYEE_ID, R.MANAGER_ID, RS.RS_RS_ID,RS.RS_STATUS, RT.RT_ID,RT.RT_TYPE "
						+"FROM REIMBURSEMENT R FULL JOIN REIMBURSEMENT_TYPE RT ON (R.RT_ID = RT.RT_ID)"
						+"FULL JOIN REIMBURSEMENT_STATUS RS ON (R.RS_ID=RS.RS_ID)"
						+"WHERE RS.RS_STATUS = ?";
			
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(0, status);
			ResultSet result = statement.executeQuery();
			
			Set<Employee> employeePendingList = new HashSet<>();
			
			while(result.next()){
					return employeePendingList.add(new Reimbursement(
						result.getInt("R.R_ID"),
						result.getTimestamp("R.R_REQUESTED"),
						result.getTimestamp("R.R_RESOLVED"),
						result.getDouble("R.R_AMOUNT"),
						result.getString("R.R_DESCRIPTION"),
						result.getObject("R.R_RECEIPT"),
						new Employee(result.getInt("R.EMPLOYEE_ID")),
						new Employee.setId(result.getInt("R.EMPLOYEE_ID")),
						new ReimbursementStatus(result.getInt("RS.RS_ID"), result.getString("RS.RS_STATUS")),
						new ReimbursementType(result.getInt("RT.RT_ID"), result.getString("RT.RT_TYPE")),
						result.getString("RT.RT_TYPE")
						)
					);
				}
			}catch(SQLException e){
				logger.info("Exception Selecting All Reimbursements that are approved",e);
		}
		return null;
	}


	@Override
	public Set<ReimbursementType> selectTypes() {
	
		try(Connection connection = ConnectionUtil.getConnection()){


			String sql ="SELECT RT_TYPE FROM REIMBURSEMENT_TYPE";

			PreparedStatement statement = connection.prepareStatement(sql);
		
			ResultSet result = statement.executeQuery();

			Set<ReimbursementType> reimbursementType = new HashSet<>();

			while (result.next()) {
				reimbursementType.add(new ReimbursementType(
						result.getInt("RT_ID"),
						result.getString("RT_TYPE")
						));
			}
			return reimbursementType;
			
		}catch(SQLException e){
			logger.info("Exception when showing Reimbursement Type.",e);
		}
		return null;
	}
}
