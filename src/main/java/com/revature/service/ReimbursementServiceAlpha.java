package com.revature.service;

import java.util.Set;

import org.apache.log4j.Logger;

import com.revature.model.Employee;
import com.revature.model.Reimbursement;
import com.revature.model.ReimbursementType;
import com.revature.repository.ReimbursementRepositoryJDBC;

public class ReimbursementServiceAlpha implements ReimbursementService {
	
	private static Logger logger = Logger.getLogger(EmployeeServiceAlpha.class);
	
	private static ReimbursementService reimbursementService = new ReimbursementServiceAlpha();
	
	private ReimbursementServiceAlpha(){}
	
	public static ReimbursementService getInstance(){
		
		return reimbursementService;
	}

	@Override
	public boolean submitRequest(Reimbursement reimbursement)  {
		
		logger.trace("Reimbursement Service Inside submitRequest:" + reimbursement);
		
		return ReimbursementRepositoryJDBC.getInstance().insert(reimbursement);
	}

	@Override
	public boolean finalizeRequest(Reimbursement reimbursement) {

        return ReimbursementRepositoryJDBC.getInstance().update(reimbursement);
		
	}

	@Override
	public Reimbursement getSingleRequest(Reimbursement reimbursement)  {
		
		return ReimbursementRepositoryJDBC.getInstance().select(reimbursement.getId());
		
	}

	@Override
	public Set<Reimbursement> getUserPendingRequests(Employee employee) {

        return ReimbursementRepositoryJDBC.getInstance().selectPending(employee.getId());
        
	}

	@Override
	public Set<Reimbursement> getUserFinalizedRequests(Employee employee) {
		
		return ReimbursementRepositoryJDBC.getInstance().selectFinalized(employee.getId());
		
	}

	@Override
	public Set<Reimbursement> getAllPendingRequests() {
		
		return ReimbursementRepositoryJDBC.getInstance().selectAllPending();
		
	}

	@Override
	public Set<Reimbursement> getAllResolvedRequests() {
		
		logger.trace("Reimbursement Service: in get allResolvedRequests");
		return ReimbursementRepositoryJDBC.getInstance().selectAllFinalized();
		
	}

	@Override
	public Set<ReimbursementType> getReimbursementTypes() {
		
		return ReimbursementRepositoryJDBC.getInstance().selectTypes();
		
}

}
