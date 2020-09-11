package com.cg;
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
	void approvedLoanRequestsFoundTest() throws NoRequestsFoundException {
		List<LoanRequest> list=loanService.viewAcceptedLoans();
		assertTrue(!list.isEmpty());
	}
	@Test
	void rejectedLoanRequestsFoundTest() throws NoRequestsFoundException {
		List<LoanRequest> list=loanService.viewRejectedLoans();
		assertTrue(!list.isEmpty());;
	}
	@Test
	void getLoanRequestsFailTest() throws NoRequestsFoundException {
		assertThrows(NoRequestsFoundException.class,()->loanService.viewAllLoanRequests());
	}
	@Test
	public void testProcessByIdFound() throws NoRequestsFoundException, LoanProcessingException {
		assertThrows(NoRequestsFoundException.class,()->loanService.processLoanRequest("lN121212"));
	}
	}
