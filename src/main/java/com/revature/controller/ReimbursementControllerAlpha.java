package com.revature.controller;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.revature.ajax.EmployeeMessage;
import com.revature.ajax.MessageUtil;
import com.revature.model.Employee;
import com.revature.model.Reimbursement;
import com.revature.model.ReimbursementStatus;
import com.revature.model.ReimbursementType;
import com.revature.service.EmployeeServiceAlpha;
import com.revature.service.ReimbursementServiceAlpha;


public class ReimbursementControllerAlpha implements ReimbursementController {
	
	private static Logger logger = Logger.getLogger(EmployeeServiceAlpha.class);
	
	private static ReimbursementController reimbursementController = new ReimbursementControllerAlpha();
	
	private ReimbursementControllerAlpha(){}
	
	public static ReimbursementController getInstance(){
		
		return reimbursementController;
	}
	
	@Override
	public Object submitRequest(HttpServletRequest request) {
		
		logger.trace("In Controller sending submit reimbursement request to service");
		
		Employee loggedEmployee = (Employee) request.getSession().getAttribute("loggedEmployee");
		
		logger.trace(loggedEmployee);
		
		if(loggedEmployee == null ) {
			   
			return "login.html";
			
		}
		
		if(loggedEmployee.getEmployeeRole().getId() == 2){
			
			return "403.html";
			
		}	
		
		if(request.getMethod().equals("GET")){
			
			return "reimbursement.html";
		
		}else{
			try {
				Double.parseDouble(request.getParameter("amount"));
			} catch (NumberFormatException e) {
				return new EmployeeMessage(MessageUtil.INVALID_AMOUNT);
			}
			try {	
					logger.trace("Reimbursement type id: " + Integer.parseInt(request.getParameter("reimbursementType")));
					Integer.parseInt(request.getParameter("reimbursementType"));
			}catch(NumberFormatException e) {
					return new EmployeeMessage(MessageUtil.INVALID_TYPE);
			}
		}
		
		ReimbursementType reimburseType= new ReimbursementType(Integer.parseInt(request.getParameter("reimbursementType")),
																			request.getParameter("reimbursementTypeName"));
		
		logger.trace("Reimbursement Controller before creating reimbursement object");
		logger.trace(loggedEmployee);
		
		 Reimbursement reimbursement = new Reimbursement(
				0,
				LocalDateTime.now(),
				null,
				Double.parseDouble(request.getParameter("amount")),
				request.getParameter("description"),
				loggedEmployee,
				null,
				new ReimbursementStatus(1,"PENDING"),
				reimburseType
			);
		 
		 logger.trace("Reimbursement controller: before: REIMBURSEMENT sent to service SENT TO DAO: " + reimbursement);
		 logger.trace("Reimbursement controller: after reimbursement obj showing");
		 
		 if(ReimbursementServiceAlpha.getInstance().submitRequest(reimbursement)){
			 logger.trace("REIMBURSEMENT CONTROLLER SENT TO DAO: " + reimbursement);
			return new EmployeeMessage(MessageUtil.REIMBURSEMENT_SUBMIT_SUCCESS);
		 }else{
			 logger.trace("failing to send/recieve anything");
			 logger.trace(reimbursement);
			return new EmployeeMessage(MessageUtil.REIMBURSEMENT_SUBMIT_FAILED);
		 }
	}

	@Override
	public Object singleRequest(HttpServletRequest request) {
		 logger.trace("A single request is being made to DAO");
			
	        Employee loggedEmployee = (Employee) request.getSession().getAttribute("loggedEmployee");
			
			if(loggedEmployee == null ) {
			   
				return "login.html";
				
			}
			
			Reimbursement reimbursement = new Reimbursement();
			return ReimbursementServiceAlpha.getInstance().getSingleRequest(reimbursement);
			
		}


	@Override
	public Object multipleRequests(HttpServletRequest request) {
					
	        Employee loggedEmployee = (Employee) request.getSession().getAttribute("loggedEmployee");
			
			if(loggedEmployee == null ) {
			   
				return "login.html";
				
			}
			
			logger.trace("Reimbursement Controller: Inside multipleRequests " +request.getParameter("fetch"));
			
			if(loggedEmployee.getEmployeeRole().getId()==1){
				
			    if(request.getParameter("fetch") == null) {
			    	
			    	logger.trace("Reimbursement Controller: did we get the fetch? ");
				    return "empPendReimbursement.html";
			    
			    }else if(request.getParameter("fetch").equals("resolved")){
			    
			    	logger.trace("Reimbursement Controller: did we get the fetch resolved ");
			         return "empResReimbursement.html";
			    
			    }else if(request.getParameter("fetch").equals("finalized")){
			    
			    	logger.trace("Reimbursement Controller: did we get the fetch finalized ");
					return ReimbursementServiceAlpha.getInstance().getUserFinalizedRequests(loggedEmployee);
				
			    }else if (request.getParameter("fetch").equals("pending")){
					
			    	logger.trace("Reimbursement Controller: did we get fetch pending ");
				    return ReimbursementServiceAlpha.getInstance().getUserPendingRequests(loggedEmployee);  
				
				}else{
				
					Set<Reimbursement> set = new HashSet<Reimbursement>(ReimbursementServiceAlpha.getInstance().getUserPendingRequests(loggedEmployee));
					
					set.addAll(ReimbursementServiceAlpha.getInstance().getUserFinalizedRequests(loggedEmployee));		
					
					return set;
				}
			    
			}else{
				
			    if(request.getParameter("fetch") == null) {
			    	
			    	 logger.trace("Reimbursement Controller: Manager the fetch null ");
				     return "manPendReimbursement.html";
			    
			    }else if(request.getParameter("fetch").equals("resolved")){
			    
			    	 logger.trace("Reimbursement Controller: Manger the fetch resolved ");
			         return "manResReimbursement.html";
			    
			    }else if(request.getParameter("fetch").equals("finalized")){
			    	
			    	logger.trace("Reimbursement Controller: Manger the fetch finalized ");
			    	return ReimbursementServiceAlpha.getInstance().getAllResolvedRequests();
				
			    }else if (request.getParameter("fetch").equals("pending")){
			    	
			    	logger.trace("Reimbursement Controller: Manger the fetch pending ");
			        return ReimbursementServiceAlpha.getInstance().getAllPendingRequests();
				
				}else if (request.getParameter("fetch").equals("viewSelected")){
			 	
			 		return "selectedReimbursementList.html";
			 	
				}else if (request.getParameter("fetch").equals("viewSelectedList")){
	                 
				 	Employee selectedEmployee = new Employee(Integer.parseInt(request.getParameter("selectedEmployeeId")));	
		
					Set<Reimbursement> set = new HashSet<Reimbursement>(ReimbursementServiceAlpha.getInstance().getUserPendingRequests(selectedEmployee));
					
					set.addAll(ReimbursementServiceAlpha.getInstance().getUserFinalizedRequests(selectedEmployee));				
			 		
					return set;
				 
				}else{
					
					return "manaPendReimbursement.html";
			
				}
				
			}

	}

	@Override
	public Object finalizeRequest(HttpServletRequest request) {
		
		Employee loggedEmployee = (Employee) request.getSession().getAttribute("loggedEmployee");
		
		/* If customer is not logged in */
		if(loggedEmployee == null ) {
		   
			return "login.html";
			
		}
		//If he/she is a employee not a manager
		if(loggedEmployee.getEmployeeRole().getId()==1){
			
			return "403.html";
			
		}
		ReimbursementStatus status = new ReimbursementStatus(Integer.parseInt(request.getParameter("statusId")),request.getParameter("status"));
		
		Reimbursement reimbursement = new Reimbursement(Integer.parseInt(request.getParameter("reimbursementId")));
		
		Reimbursement reimbursementToUpdate = ReimbursementServiceAlpha.getInstance().getSingleRequest(reimbursement);
		
		reimbursementToUpdate.setStatus(status);
		
		reimbursementToUpdate.setResolved(LocalDateTime.now());
		
		
		if (ReimbursementServiceAlpha.getInstance().finalizeRequest(reimbursementToUpdate)) {	
			
			return new EmployeeMessage(MessageUtil.FINALIZED_SUCCESS);
			
		} else {
			return new EmployeeMessage(MessageUtil.FINALIZED_FAILED);
}
	}

	@Override
	public Object getRequestTypes(HttpServletRequest request) {
		 
		Employee loggedEmployee = (Employee) request.getSession().getAttribute("loggedEmployee");
			
		if(loggedEmployee == null ) {
			   
			return "login.html";
				
		}
		
		return ReimbursementServiceAlpha.getInstance().getReimbursementTypes();
			 
	}
}


