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
			int statementIndex = 0;
		
			String command = "INSERT INTO REIMBURSEMENT VALUES(NULL,?,?,?,?,NULL, ?, ?,?,?)";

			PreparedStatement statement = connection.prepareStatement(command);

			//Set attributes to be inserted
			statement.setTimestamp(++statementIndex, Timestamp.valueOf(reimbursement.getRequested()));
			statement.setTimestamp(++statementIndex, Timestamp.valueOf(reimbursement.getResolved()));
			statement.setDouble(++statementIndex, reimbursement.getAmount());
			statement.setString(++statementIndex, reimbursement.getDescription());
			statement.setInt(++statementIndex, reimbursement.getRequester().getId());
			statement.setInt(++statementIndex, reimbursement.getApprover().getId());
			statement.setInt(++statementIndex, reimbursement.getStatus().getId());
			statement.setInt(++statementIndex, reimbursement.getType().getId());

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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Reimbursement select(int reimbursementId) {
		// TODO Auto-generated method stub
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
