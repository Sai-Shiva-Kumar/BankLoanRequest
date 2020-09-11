package com.cg.banking.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cg.banking.entity.LoanRequest;
@Repository
public interface LoanRequestDao extends JpaRepository<LoanRequest, String>{

//for viewing all the pending loan requests
@Query(value="from LoanRequest lq where  reqStatus=:status")
public List<LoanRequest> getPendingRequests(@Param("status") String req);

//for checking the availed loans of customer
@Query(value="select count(loanRequestId) from LoanRequest lq where lq.customer.customerId=:custId and lq.reqStatus=:status")
public int getAvailedLoans(@Param("custId") String customerId, @Param("status") String reqStatus);

//for checking number of loans availed by a customer
@Query(value="select count(loanRequestId) from LoanRequest lq where lq.customer.customerId=:custId")
public int countLoansOfCustomer(@Param("custId") String customerId);

//for viewing all the approved loans
@Query(value="from LoanRequest lq where  reqStatus=:status")
public List<LoanRequest> getAcceptedRequests(@Param("status") String req);

//for viewing all the rejected loans
@Query(value="from LoanRequest lq where  reqStatus=:status")
public List<LoanRequest> getRejectedRequests(@Param("status") String req);

}
