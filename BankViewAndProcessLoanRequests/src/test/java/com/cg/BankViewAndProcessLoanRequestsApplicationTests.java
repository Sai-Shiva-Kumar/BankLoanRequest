package com.cg;

import static org.junit.jupiter.api.Assertions.assertFalse;
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
	void getLoanRequestsTest1() throws NoRequestsFoundException, LoanProcessingException{
		List<LoanRequest> list=loanService.viewAllLoanRequests();
		assertFalse(list.isEmpty());
		}
	@Test
	void getLoanRequestsTest2() throws NoRequestsFoundException {
		List<LoanRequest> list=loanService.viewAllLoanRequests();
		assertTrue(list.isEmpty());
	}
}
