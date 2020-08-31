package com.cg.banking.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cg.banking.entity.LoanRequest;
@Repository
public interface LoanRequestDao extends JpaRepository<LoanRequest, String>{
/*@Query(value="update LoanRequest lr set lr.reqStatus=:status where lr.loanRequestId=:Id")
public LoanRequest processLoanRequest(@Param("status") String reqStatus, @Param("Id") String loanRequestId);

*/	

@Query(value="from LoanRequest lq where  reqStatus=:status")
public List<LoanRequest> getPendingRequests(@Param("status") String req);
@Query(value="select count(loanRequestId) from LoanRequest lq where lq.customer.customerId=:custId and lq.reqStatus=:status")
public int getAvailedLoans(@Param("custId") String customerId, @Param("status") String reqStatus);
@Query(value="select count(loanRequestId) from LoanRequest lq where lq.customer.customerId=:custId")
public int countLoansOfCustomer(@Param("custId") String customerId);
}
