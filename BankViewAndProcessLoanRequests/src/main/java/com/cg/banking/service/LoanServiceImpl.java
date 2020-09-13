package com.cg.banking.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.cg.banking.dao.IAccountDao;
import com.cg.banking.dao.LoanRequestDao;
import com.cg.banking.entity.Account;
import com.cg.banking.entity.LoanRequest;
import com.cg.banking.exceptions.LoanProcessingException;
import com.cg.banking.exceptions.LoginException;
import com.cg.banking.exceptions.NoRequestsFoundException;
import com.cg.banking.util.CgConstants;
import com.cg.banking.web.ViewAndProcessLoanRestController;
@Service
public class LoanServiceImpl implements ILoanService {
	Logger logger = LoggerFactory.getLogger(ViewAndProcessLoanRestController.class);
	
	@Autowired
	private LoanRequestDao loanRequestDao;
	
	@Autowired
	private IAccountDao accountDao;

	@Autowired
	private RestTemplate rt;
	
	//to display all the pending loan request
	@Override
	public List<LoanRequest> viewAllLoanRequests() throws NoRequestsFoundException {
		List<LoanRequest> loanRequestList= loanRequestDao.getPendingRequests(CgConstants.PENDING_REQUESTS);
		if(loanRequestList.isEmpty())
			throw new NoRequestsFoundException(CgConstants.NO_LOAN_REQUESTS);
		loanRequestList.sort((loanRequest1,loanRequest2)->loanRequest1.getDateOfRequest().compareTo(loanRequest2.getDateOfRequest()));
		return loanRequestList;
	}
	
	//Method to process loan request by using loan request id
	@Override
	@Transactional
	public String processLoanRequest(String loanRequestId) throws NoRequestsFoundException, LoanProcessingException {
		
		Optional<LoanRequest> optLoan = loanRequestDao.findById(loanRequestId);
		if(!optLoan.isPresent())throw new NoRequestsFoundException(CgConstants.NO_LOAN_REQUESTS);
		LoanRequest req = optLoan.get();
		String custId=req.getCustomer().getCustomerId();
		int count=loanRequestDao.getAvailedLoans(custId, CgConstants.LOAN_APPROVED);
		
		/*checking the first condition, which is nothing but, to check if the customer had availed loans previously and if yes, the count will be 
		greater than zero and the loan will be rejected if he did not availed loans from bank previously, his loan will be approved*/
		if(count>0) {
			req.setReqStatus(CgConstants.LOAN_REJECTED);
			loanRequestDao.save(req);
			throw new LoanProcessingException(CgConstants.LOAN_UNDERGOING);
		}
		
		//calculating the compound interest and here the compound interest method is called
		double ci =calculateCompoundInt(req.getLoanTenure(), req.getLoanAmount());
		
		//calculating the EMI and by calling calculate EMI method
		double emi = calculateEmi(ci, req.getLoanTenure());
		logger.info(emi+CgConstants.EMI);
		
		//calculating the monthly salary from annual income and obtaining half part of the monthly salary
		double fiftyPercentOfMonthlyIncome = req.getAnnualIncome() * 0.5 /12;
		logger.info(fiftyPercentOfMonthlyIncome+CgConstants.FIFTY_PERCENT_OF_SALARY);
		
		/*checking the second condition that decides where or not the loan request should be accepted, if EMI greater than half the salary
		the loan should be rejected throwing an exception and if EMI is less than half the salary,, the loan request is approved*/
		if(emi > fiftyPercentOfMonthlyIncome) {
			req.setReqStatus(CgConstants.LOAN_REJECTED);
			loanRequestDao.save(req);
			throw new LoanProcessingException(CgConstants.NOT_ENOUGH_INCOME);}
		req.setReqStatus(CgConstants.LOAN_APPROVED);
		loanRequestDao.save(req);
		
		/*after approving the loan request raised by the customer, the customer will be provided with a new Loan Account where he can 
		 look after the loan amount*/
		Account acc=new Account();
		int loanCount=loanRequestDao.countLoansOfCustomer(custId)+1;
		acc.setAccountId(CgConstants.LOAN+custId+CgConstants.EMPTY+loanCount);
		acc.setAccountName(CgConstants.PERSONAL_LOAN);
		acc.setAccountStatus(CgConstants.ACTIVE);
		acc.setCreatedDt(LocalDate.now());
		acc.setCustomer(req.getCustomer());
		acc.setAccountBalance(req.getLoanAmount());
		acc.setLastUpdated(LocalDate.now());
		accountDao.save(acc);
		return CgConstants.APPROVED;
			}

//for calculating the compound interest
	public double calculateCompoundInt(int years,double amt) {
		return amt*Math.pow((1+0.1), years);
	}


//to calculate the EMI using Compound interest
	public double calculateEmi(double amt,int years) {
		return amt/(years*12);
	}

	
//for validating the login details and this method is linked to login MicroService
	@Override
	public String validateTokenInAdminLoginService(String tokenId) throws LoginException{
		if(tokenId==null||tokenId.length()==0)
			throw new LoginException(CgConstants.USER_NOT_AUTHORIZED);
		String url=CgConstants.LOGIN_MAIN_URL;
		String role=rt.postForObject(url, tokenId, String.class);
		logger.info(CgConstants.ROLE + role);
		if(role==null)
			throw new LoginException(CgConstants.USE_NOT_AUTHENTICATED);
		return role;
	}
//this method displays the approved/accepted loans
	@Override
	public List<LoanRequest> viewAcceptedLoans() throws NoRequestsFoundException {
		List<LoanRequest> loanRequestList= loanRequestDao.getAcceptedRequests(CgConstants.APPROVED_REQUESTS);
		if(loanRequestList.isEmpty())
			throw new NoRequestsFoundException(CgConstants.NO_ACCEPTED_LOAN_REQUESTS);
		loanRequestList.sort((loanRequest1,loanRequest2)->loanRequest1.getDateOfRequest().compareTo(loanRequest2.getDateOfRequest()));
		return loanRequestList;
		
	}
//this method is used for displaying the rejected loans
	@Override
	public List<LoanRequest> viewRejectedLoans() throws NoRequestsFoundException {
		List<LoanRequest> loanRequestList= loanRequestDao.getRejectedRequests(CgConstants.REJECTED_REQUESTS);
		if(loanRequestList.isEmpty())
			throw new NoRequestsFoundException(CgConstants.NO_REJECTED_LOAN_REQUESTS);
		loanRequestList.sort((loanRequest1,loanRequest2)->loanRequest1.getDateOfRequest().compareTo(loanRequest2.getDateOfRequest()));
		return loanRequestList;
	}
//This method displays the account details after a loan account gets added after the approval of the loan of a customer
	@Override
	public List<Account> getUpdatedAccountList() {
		
		List<Account> account = accountDao.findAll();
		return account;
	}
}
