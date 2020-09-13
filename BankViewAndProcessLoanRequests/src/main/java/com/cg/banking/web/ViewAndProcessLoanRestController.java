package com.cg.banking.web;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.cg.banking.dto.LoanSuccessMessage;
import com.cg.banking.entity.Account;
import com.cg.banking.entity.LoanRequest;
import com.cg.banking.exceptions.LoanProcessingException;
import com.cg.banking.exceptions.LoginException;
import com.cg.banking.exceptions.NoRequestsFoundException;
import com.cg.banking.service.ILoanService;
import com.cg.banking.util.CgConstants;

@RestController
@CrossOrigin(origins={"http://localhost:4200"})
public class ViewAndProcessLoanRestController {
	
	Logger logger = LoggerFactory.getLogger(ViewAndProcessLoanRestController.class);
	
	@Autowired
	private ILoanService loanService;
	
	@SuppressWarnings("unused")
	@Autowired
	private RestTemplate rt;
	
	
	//viewing all the pending loan requests
	@GetMapping(CgConstants.VIEW_ALL_PENDING_REQUESTS) //get method for fetching the details
	public List<LoanRequest> getLoanRequests(@RequestHeader(name="tokenId",required=false) String tokenId) throws NoRequestsFoundException, LoginException {
		logger.info(CgConstants.TOKEN_ID+ tokenId);
				String role=loanService.validateTokenInAdminLoginService(tokenId);
		logger.info(CgConstants.ROLE+role);
		if(!role.contentEquals(CgConstants.ADMIN))
			throw new LoginException(CgConstants.ONLY_ADMIN_IS_ALLOWED);
	    List<LoanRequest> loanRequestList= loanService.viewAllLoanRequests();
		return loanRequestList;
	}
	
	
	//to process loan requests raised by customer based on the loan request ID
	@GetMapping(CgConstants.PROCESS_REQUESTS_BY_ID)  //get method for fetching the details
	public LoanSuccessMessage processLoanRequest(@RequestHeader(name="tokenId",required=false) String tokenId, 
			@PathVariable("reqID") String requestId) throws NoRequestsFoundException, LoanProcessingException, LoginException {
		
		logger.info(CgConstants.TOKEN_ID+ tokenId);
		String role=loanService.validateTokenInAdminLoginService(tokenId);
            logger.info(CgConstants.ROLE+role);
          if(!role.contentEquals(CgConstants.ADMIN))
	            throw new LoginException(CgConstants.ONLY_ADMIN_IS_ALLOWED);
		String res=loanService.processLoanRequest(requestId);
		return new LoanSuccessMessage(res); //displays the success message
	}
	
	
	//to view the approved loans
	@GetMapping(CgConstants.APPROVED_LOAN_REQUESTS_URL) //get method for fetching details
	public List<LoanRequest> getAcceptedLoanRequests(@RequestHeader(name="tokenId",required=false) String tokenId) throws NoRequestsFoundException, LoginException {
       logger.info(CgConstants.TOKEN_ID+ tokenId);
		String role=loanService.validateTokenInAdminLoginService(tokenId);
		logger.info(CgConstants.ROLE+role);
		List<LoanRequest> loanRequestAcceptedList= loanService.viewAcceptedLoans();
		return loanRequestAcceptedList;
	}
	
	
	//to view rejected loans
	@GetMapping(CgConstants.REJECTED_LOAN_REQUESTS_URL)  //get method for fetching details
	public List<LoanRequest> getRejectedLoanRequests(@RequestHeader(name="tokenId",required=false) String tokenId) throws NoRequestsFoundException, LoginException {
		logger.info(CgConstants.TOKEN_ID+ tokenId);
		String role=loanService.validateTokenInAdminLoginService(tokenId);
		logger.info(CgConstants.ROLE+role);
		List<LoanRequest> loanRequestRejectedList= loanService.viewRejectedLoans();
		return loanRequestRejectedList;
	}
	
	
	//to view account details
	@GetMapping(CgConstants.UPDATED_ACCOUNT_LIST_URL) //get method for fetching details
	public List<Account> getUpdatedAccountList(@RequestHeader(name="tokenId",required=false) String tokenId) throws LoginException{
		logger.info(CgConstants.TOKEN_ID+ tokenId);
		String role=loanService.validateTokenInAdminLoginService(tokenId);
		logger.info(CgConstants.ROLE+role);
		List<Account> accList=loanService.getUpdatedAccountList();
		return accList;
	}
	
}
