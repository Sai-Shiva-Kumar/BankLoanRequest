package com.cg.banking.service;

import java.util.List;

import com.cg.banking.entity.Account;
import com.cg.banking.entity.LoanRequest;
import com.cg.banking.exceptions.LoanProcessingException;
import com.cg.banking.exceptions.LoginException;
import com.cg.banking.exceptions.NoRequestsFoundException;

public interface ILoanService {
 public List<LoanRequest> viewAllLoanRequests() throws NoRequestsFoundException;
 public String processLoanRequest(String loanRequestId) throws NoRequestsFoundException,LoanProcessingException;
 public String validateTokenInAdminLoginService(String tokenId) throws LoginException;
 public List<LoanRequest> viewAcceptedLoans() throws NoRequestsFoundException;
 public List<LoanRequest> viewRejectedLoans() throws NoRequestsFoundException;
 public List<Account> getUpdatedAccountList();
}
