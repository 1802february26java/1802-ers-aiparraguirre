package com.revature.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Set;

import org.apache.log4j.Logger;

import com.revature.model.Reimbursement;
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
			logger.warn("Exception creating a new reimbursement", e);
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
		
		try(){
			
		}
		catch(SQLException e){
			logger.info("Error Selecting Reimbursement by ID");
		}
		return null;
	}

	@Override
	public Set<Reimbursement> selectPending(int employeeId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Reimbursement> selectFinalized(int employeeId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Reimbursement> selectAllPending() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Reimbursement> selectAllFinalized() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<ReimbursementType> selectTypes() {
		// TODO Auto-generated method stub
		return null;
	}

}
