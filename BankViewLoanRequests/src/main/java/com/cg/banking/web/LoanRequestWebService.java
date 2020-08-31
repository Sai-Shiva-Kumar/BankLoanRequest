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
import com.cg.banking.entity.LoanRequest;
import com.cg.banking.exceptions.LoanProcessingException;
import com.cg.banking.exceptions.LoginException;
import com.cg.banking.exceptions.NoRequestsFoundException;
import com.cg.banking.service.ILoanService;
import com.cg.banking.util.CgConstants;

@RestController
@CrossOrigin(origins={"http://localhost:4200"})
public class LoanRequestWebService {
	
	Logger logger = LoggerFactory.getLogger(LoanRequestWebService.class);
	
	@Autowired
	private ILoanService loanService;
	
	@SuppressWarnings("unused")
	@Autowired
	private RestTemplate rt;
	
	@GetMapping(CgConstants.VIEW_ALL_PENDING_REQUESTS)
	public List<LoanRequest> getLoanRequests(@RequestHeader(name="tokenId",required=false) String tokenId) throws NoRequestsFoundException, LoginException {
		logger.info(CgConstants.TOKEN_ID+ tokenId);
		
		String role=loanService.validateTokenInAdminLoginService(tokenId);
		logger.info(CgConstants.ROLE+role);
	    List<LoanRequest> loanRequestList= loanService.viewAllLoanRequests();
		return loanRequestList;
	}
	
	@GetMapping(CgConstants.PROCESS_REQUESTS_BY_ID)
	public LoanSuccessMessage processLoanRequest(@PathVariable("reqID") String requestId) throws NoRequestsFoundException, LoanProcessingException {
		String res=loanService.processLoanRequest(requestId);
		return new LoanSuccessMessage(res);
	}
	
}
