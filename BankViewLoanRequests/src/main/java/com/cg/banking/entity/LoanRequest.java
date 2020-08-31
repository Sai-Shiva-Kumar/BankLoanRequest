package com.cg.banking.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name="cap_loan_request")
public class LoanRequest {
	@Id
	@Column(name="loan_req_id", length=10)
	private String loanRequestId;
	
	@Column(name="loan_amt")
	private double loanAmount;
	
	@Column(name="loan_type", length=10)
	private String loanType;
	
	@Column(name="loan_tenure")
	private Integer loanTenure;
	
	@Column(name="loan_req_status", length=15)
	private String reqStatus;
	
	@Column(name="date_of_req")
	private LocalDate dateOfRequest;
	
	@Column(name="annual_income")
	private double annualIncome;
	
	@ManyToOne
	@JoinColumn(name="customer_id", referencedColumnName = "customer_id")
	private Customer customer = new Customer();

	
	public LoanRequest() {
		super();
		
	}

	public LoanRequest(String loanRequestId, double loanAmount, String loanType, Integer loanTenure, String reqStatus,
			LocalDate dateOfRequest, double annualIncome, Customer customer) {
		super();
		this.loanRequestId = loanRequestId;
		this.loanAmount = loanAmount;
		this.loanType = loanType;
		this.loanTenure = loanTenure;
		this.reqStatus = reqStatus;
		this.dateOfRequest = dateOfRequest;
		this.annualIncome = annualIncome;
		this.customer = customer;
	}

	public String getLoanRequestId() {
		return loanRequestId;
	}

	public void setLoanRequestId(String loanRequestId) {
		this.loanRequestId = loanRequestId;
	}

	public double getLoanAmount() {
		return loanAmount;
	}

	public void setLoanAmount(double loanAmount) {
		this.loanAmount = loanAmount;
	}

	public String getLoanType() {
		return loanType;
	}

	public void setLoanType(String loanType) {
		this.loanType = loanType;
	}

	public Integer getLoanTenure() {
		return loanTenure;
	}

	public void setLoanTenure(Integer loanTenure) {
		this.loanTenure = loanTenure;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	

	public LocalDate getDateOfRequest() {
		return dateOfRequest;
	}

	public void setDateOfRequest(LocalDate dateOfRequest) {
		this.dateOfRequest = dateOfRequest;
	}

	public String getReqStatus() {
		return reqStatus;
	}

	public void setReqStatus(String reqStatus) {
		this.reqStatus = reqStatus;
	}

	public double getAnnualIncome() {
		return annualIncome;
	}

	public void setAnnualIncome(double annualIncome) {
		this.annualIncome = annualIncome;
	}
	
	
	
	
	
	
}
