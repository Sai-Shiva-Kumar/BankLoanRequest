package com.cg;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cg.banking.entity.LoanRequest;
import com.cg.banking.exceptions.LoanProcessingException;
import com.cg.banking.exceptions.NoRequestsFoundException;
import com.cg.banking.service.ILoanService;
@SpringBootTest
class BankViewAndProcessLoanRequestsApplicationTests {
	@Autowired
	ILoanService loanService;
	
	@Test
	void getLoanRequestsTest1() throws NoRequestsFoundException {
		List<LoanRequest> list=loanService.viewAllLoanRequests();
		assertTrue(!list.isEmpty());
	}
	@Test
	void approvedLoanRequestsNotFoundTest2() throws NoRequestsFoundException {
		
	   assertThrows(NoRequestsFoundException.class,()->loanService.viewAcceptedLoans());
	}
	@Test
	public void testProcessByIdFound() throws NoRequestsFoundException, LoanProcessingException {
		
		assertThrows(NoRequestsFoundException.class,()->loanService.processLoanRequest("lN121212"));
	}
	
}
