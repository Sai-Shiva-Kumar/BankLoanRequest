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
	
	@Override
	public List<LoanRequest> viewAllLoanRequests() throws NoRequestsFoundException {
		List<LoanRequest> loanRequestList= loanRequestDao.getPendingRequests(CgConstants.PENDING_REQUESTS);
		if(loanRequestList.isEmpty())
			throw new NoRequestsFoundException(CgConstants.NO_LOAN_REQUESTS);
		loanRequestList.sort((loanRequest1,loanRequest2)->loanRequest1.getDateOfRequest().compareTo(loanRequest2.getDateOfRequest()));
		return loanRequestList;
	}
	
	@Override
	@Transactional
	public String processLoanRequest(String loanRequestId) throws NoRequestsFoundException, LoanProcessingException {
		
		Optional<LoanRequest> optLoan = loanRequestDao.findById(loanRequestId);
		if(!optLoan.isPresent())throw new NoRequestsFoundException(CgConstants.NO_LOAN_REQUESTS);
		LoanRequest req = optLoan.get();
		String custId=req.getCustomer().getCustomerId();
		int count=loanRequestDao.getAvailedLoans(custId, CgConstants.LOAN_APPROVED);
		
		if(count>0) {
			req.setReqStatus(CgConstants.LOAN_REJECTED);
			loanRequestDao.save(req);
			throw new LoanProcessingException(CgConstants.LOAN_UNDERGOING);
		}
		double ci =calculateCompoundInt(req.getLoanTenure(), req.getLoanAmount());
		double emi = calculateEmi(ci, req.getLoanTenure());
		logger.info(emi+CgConstants.EMI);
		double fiftyPercentOfAnnualIncome = req.getAnnualIncome() * 0.5 /12;
		logger.info(fiftyPercentOfAnnualIncome+CgConstants.FIFTY_PERCENT_OF_SALARY);
		if(emi > fiftyPercentOfAnnualIncome) {
			req.setReqStatus(CgConstants.LOAN_REJECTED);
			loanRequestDao.save(req);
			throw new LoanProcessingException(CgConstants.NOT_ENOUGH_INCOME);}
		req.setReqStatus(CgConstants.LOAN_APPROVED);
		loanRequestDao.save(req);
		Account acc=new Account();
		int loanCount=loanRequestDao.countLoansOfCustomer(custId)+1;
		acc.setAccountId(CgConstants.LOAN+custId+""+loanCount);
		acc.setAccountName(CgConstants.PERSONAL_LOAN);
		acc.setAccountStatus(CgConstants.ACTIVE);
		acc.setCreatedDt(LocalDate.now());
		acc.setCustomer(req.getCustomer());
		acc.setAccountBalance(req.getLoanAmount());
		acc.setLastUpdated(LocalDate.now());
		accountDao.save(acc);
		return CgConstants.APPROVED;
			}
	public double calculateCompoundInt(int years,double amt) {
		return amt*Math.pow((1+0.1), years);
	}
	public double calculateEmi(double amt,int years) {
		return amt/(years*12);
	}
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

	@Override
	public List<LoanRequest> viewAcceptedLoans() throws NoRequestsFoundException {
		List<LoanRequest> loanRequestList= loanRequestDao.getAcceptedRequests(CgConstants.APPROVED_REQUESTS);
		if(loanRequestList.isEmpty())
			throw new NoRequestsFoundException(CgConstants.NO_ACCEPTED_LOAN_REQUESTS);
		loanRequestList.sort((loanRequest1,loanRequest2)->loanRequest1.getDateOfRequest().compareTo(loanRequest2.getDateOfRequest()));
		return loanRequestList;
		
	}

	@Override
	public List<LoanRequest> viewRejectedLoans() throws NoRequestsFoundException {
		List<LoanRequest> loanRequestList= loanRequestDao.getRejectedRequests(CgConstants.REJECTED_REQUESTS);
		if(loanRequestList.isEmpty())
			throw new NoRequestsFoundException(CgConstants.NO_REJECTED_LOAN_REQUESTS);
		loanRequestList.sort((loanRequest1,loanRequest2)->loanRequest1.getDateOfRequest().compareTo(loanRequest2.getDateOfRequest()));
		return loanRequestList;
	}

	@Override
	public List<Account> getUpdatedAccountList() {
		
		List<Account> account = accountDao.findAll();
		return account;
	}
}
