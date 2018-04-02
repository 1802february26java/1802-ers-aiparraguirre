package com.revature.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;


import com.revature.model.Reimbursement;
import com.revature.model.ReimbursementStatus;
import com.revature.model.ReimbursementType;
import com.revature.util.ConnectionUtil;

public class ReimbursementRepositoryJDBC implements ReimbursementRepository {


	private static Logger logger = Logger.getLogger(ReimbursementRepositoryJDBC.class);
	
	private static ReimbursementRepository reimbursementRepository = new ReimbursementRepositoryJDBC();
	
	private ReimbursementRepositoryJDBC(){}
	
	public static ReimbursementRepository getInstance(){
		return reimbursementRepository;
	}
	
	@Override
	public boolean insert(Reimbursement reimbursement) {
		try(Connection connection = ConnectionUtil.getConnection()) {
			
			logger.trace("Inside the Reimbursement DAO insert:" + reimbursement);
			int parameterIndex = 0;
		
			String sql = "INSERT INTO REIMBURSEMENT VALUES(NULL,?,NULL,?,?,NULL,?,NULL,?,?)";

			PreparedStatement statement = connection.prepareStatement(sql);

			logger.trace("localdatetime: " + reimbursement.getRequested());
			
			logger.trace("timestamp: " + Timestamp.valueOf(reimbursement.getRequested()));
			statement.setTimestamp(++parameterIndex, Timestamp.valueOf(reimbursement.getRequested()));
			
			statement.setDouble(++parameterIndex, reimbursement.getAmount());
			statement.setString(++parameterIndex, reimbursement.getDescription());
			statement.setInt(++parameterIndex, reimbursement.getRequester().getId());
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
			String sql ="UPDATE REIMBURSEMENT SET R_RESOLVED = ?, R_AMOUNT=?,  MANAGER_ID =?, RS_ID=? WHERE R_ID = ?";
			
			PreparedStatement statement = connection.prepareStatement(sql);
			
			statement.setTimestamp(++parameterIndex, Timestamp.valueOf(reimbursement.getResolved()));
			statement.setDouble(++parameterIndex, reimbursement.getAmount());
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
			
			String sql ="SELECT R.*, U.*, RS.RS_ID, RS.RS_STATUS, RT.RT_ID, RT.RT_TYPE FROM REIMBURSEMENT R" 
							+" FULL JOIN REIMBURSEMENT_TYPE RT ON (R.RT_ID = RT.RT_ID)"
							+" FULL JOIN REIMBURSEMENT_STATUS RS ON (R.RS_ID=RS.RS_ID)"
							+" FULL JOIN USER_T U ON U.U_ID = R.R_ID "
							+" WHERE R.R_ID = ?";
			
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, reimbursementId);
			
			ResultSet result = statement.executeQuery();
			
			if(result.next()){
				return new Reimbursement(
						result.getInt("R_ID"),
						result.getTimestamp("R_REQUESTED").toLocalDateTime(),
						result.getTimestamp("R_RESOLVED").toLocalDateTime(),
						result.getDouble("R_AMOUNT"),
						result.getString("R_DESCRIPTION"),
						EmployeeRepositoryJDBC.getInstance().select(result.getInt("EMPLOYEE_ID")),
						EmployeeRepositoryJDBC.getInstance().select(result.getInt("MANAGER_ID")),
						new ReimbursementStatus(result.getInt("RS.RS_ID"), result.getString("RS_STATUS")),
						new ReimbursementType(result.getInt("RT_ID"), result.getString("RT_TYPE"))
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
			String sql ="SELECT R.*, U.*, RS.RS_ID, RS.RS_STATUS, RT.RT_ID, RT.RT_TYPE FROM REIMBURSEMENT R" 
					+" FULL JOIN REIMBURSEMENT_TYPE RT ON (R.RT_ID = RT.RT_ID)"
					+" FULL JOIN REIMBURSEMENT_STATUS RS ON (R.RS_ID=RS.RS_ID)"
					+" FULL JOIN USER_T U ON U.U_ID = R.R_ID "
					+" WHERE R.EMPLOYEE_ID = ?";
			
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(++parameterIndex, employeeId);
			ResultSet result = statement.executeQuery();
			
			Set<Reimbursement> employeePendingList = new HashSet<>();
			
			while(result.next()){
				
				if(result.getString("RS_STATUS").equals(pending)){
					employeePendingList.add(new Reimbursement(
						result.getInt("R_ID"),
						result.getTimestamp("R_REQUESTED").toLocalDateTime(),
						null,
						result.getDouble("R_AMOUNT"),
						result.getString("R_DESCRIPTION"),
						EmployeeRepositoryJDBC.getInstance().select(result.getInt("EMPLOYEE_ID")),
						EmployeeRepositoryJDBC.getInstance().select(result.getInt("MANAGER_ID")),
						new ReimbursementStatus(result.getInt("RS_ID"), result.getString("RS_STATUS")),
						new ReimbursementType(result.getInt("RT_ID"), result.getString("RT_TYPE"))
						)
					);
				}
				return employeePendingList;
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
		
			String sql ="SELECT R.*, U.*, RS.RS_ID, RS.RS_STATUS, RT.RT_ID, RT.RT_TYPE FROM REIMBURSEMENT R" 
						+" FULL JOIN REIMBURSEMENT_TYPE RT ON (R.RT_ID = RT.RT_ID)"
						+" FULL JOIN REIMBURSEMENT_STATUS RS ON (R.RS_ID=RS.RS_ID)"
						+" FULL JOIN USER_T U ON U.U_ID = R.R_ID "
						+" WHERE R.EMPLOYEE_ID = '?' && WHERE RS.STATUS LIKE 'APPROVED'";
			
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(++parameterIndex, employeeId);
			ResultSet result = statement.executeQuery();
			
			Set<Reimbursement> reimbursementApprovedList = new HashSet<>();
			
			while(result.next()){
				
				if(result.getString("RS_STATUS").equals(status)){
					reimbursementApprovedList.add(new Reimbursement(
						result.getInt("R_ID"),
						result.getTimestamp("R_REQUESTED").toLocalDateTime(),
						result.getTimestamp("R_RESOLVED").toLocalDateTime(),
						result.getDouble("R_AMOUNT"),
						result.getString("R_DESCRIPTION"),
						EmployeeRepositoryJDBC.getInstance().select(result.getInt("EMPLOYEE_ID")),
						EmployeeRepositoryJDBC.getInstance().select(result.getInt("MANAGER_ID")),
						new ReimbursementStatus(result.getInt("RS_ID"), result.getString("RS_STATUS")),
						new ReimbursementType(result.getInt("RT_ID"), result.getString("RT_TYPE"))
						
						)
					);
				}
				return reimbursementApprovedList;
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
			
			String sql ="SELECT R.*, U.*, RS.RS_ID, RS.RS_STATUS, RT.RT_ID, RT.RT_TYPE FROM REIMBURSEMENT R"
					+" FULL JOIN REIMBURSEMENT_TYPE RT ON (R.RT_ID = RT.RT_ID)"
					+" FULL JOIN REIMBURSEMENT_STATUS RS ON (R.RS_ID=RS.RS_ID)"
					+" FULL JOIN USER_T U ON U.U_ID = R.EMPLOYEE_ID" 
					+" WHERE RS.RS_STATUS = 'PENDING' ";
			
			PreparedStatement statement = connection.prepareStatement(sql);
			
			ResultSet result = statement.executeQuery();
		
			
			Set<Reimbursement> employeePendingList = new HashSet<>();
						
			while(result.next()){
					employeePendingList.add(new Reimbursement(
						result.getInt("R_ID"),
						result.getTimestamp("R_REQUESTED").toLocalDateTime(),
						null,
						result.getDouble("R_AMOUNT"),
						result.getString("R_DESCRIPTION"),
						EmployeeRepositoryJDBC.getInstance().select(result.getInt("EMPLOYEE_ID")),
						null,
						new ReimbursementStatus(result.getInt("RS_ID"), result.getString("RS_STATUS")),
						new ReimbursementType(result.getInt("RT_ID"), result.getString("RT_TYPE"))
						)
					);
				}
			
			return employeePendingList;
			
		}catch(SQLException e){
				logger.info("Exception selecting all Reimbursements that are Pending",e);
		}
		return null;
	}

	@Override
	public Set<Reimbursement> selectAllFinalized() {
		
		try(Connection connection = ConnectionUtil.getConnection()){
			logger.trace("Reimbursement JDBC: selectAllFinalized");
			String status = "APPROVED";
		
			String sql ="SELECT R.*, U.*, RS.RS_ID, RS.RS_STATUS, RT.RT_ID, RT.RT_TYPE FROM REIMBURSEMENT R" 
						+" FULL JOIN REIMBURSEMENT_TYPE RT ON (R.RT_ID = RT.RT_ID)"
						+" FULL JOIN REIMBURSEMENT_STATUS RS ON (R.RS_ID=RS.RS_ID)"
						+" FULL JOIN USER_T U ON U.U_ID = R.R_ID "
						+" WHERE RS.RS_STATUS = 'APPROVED'";
			
			PreparedStatement statement = connection.prepareStatement(sql);
			//statement.setString(0, status);
			ResultSet result = statement.executeQuery();
			
			Set<Reimbursement> employeeFinalizedList = new HashSet<>();
			
			logger.trace("Returning all select All Finalized");
			
			while(result.next()){
					    employeeFinalizedList.add(new Reimbursement(
						result.getInt("R_ID"),
						result.getTimestamp("R_REQUESTED").toLocalDateTime(),
						result.getTimestamp("R_RESOLVED").toLocalDateTime(),
						result.getDouble("R_AMOUNT"),
						result.getString("R_DESCRIPTION"),
						EmployeeRepositoryJDBC.getInstance().select(result.getInt("EMPLOYEE_ID")),
						EmployeeRepositoryJDBC.getInstance().select(result.getInt("MANAGER_ID")),
						new ReimbursementStatus(result.getInt("RS_ID"), result.getString("RS_STATUS")),
						new ReimbursementType(result.getInt("RT_ID"), result.getString("RT_TYPE"))
						)
					);
				}
			return employeeFinalizedList;
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
	public static void main(String[] args) {
		//logger.trace(ReimbursementRepositoryJDBC.getInstance().selectAllPending());
		logger.trace(ReimbursementRepositoryJDBC.getInstance().selectAllFinalized());
		
	}
}
