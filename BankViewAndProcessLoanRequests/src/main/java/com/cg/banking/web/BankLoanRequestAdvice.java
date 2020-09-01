package com.cg.banking.web;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.cg.banking.dto.Error;
import com.cg.banking.exceptions.LoanProcessingException;
import com.cg.banking.exceptions.LoginException;
import com.cg.banking.exceptions.NoRequestsFoundException;
@RestControllerAdvice
@CrossOrigin(origins={"https://localhost:4200"})
public class BankLoanRequestAdvice {

@ExceptionHandler(NoRequestsFoundException.class)
@ResponseStatus(code=HttpStatus.NOT_FOUND)
public Error handleNoRequestsFoundException(NoRequestsFoundException ex) {
	return new Error(HttpStatus.NOT_FOUND.toString(),ex.getMessage(),LocalDateTime.now().toString());
	
}


@ExceptionHandler(LoanProcessingException.class)
@ResponseStatus(code=HttpStatus.BAD_REQUEST)
public Error handleLoanProcessingException(LoanProcessingException ex) {
	return new Error(HttpStatus.BAD_REQUEST.toString(),ex.getMessage(),LocalDateTime.now().toString());
	
}
@ExceptionHandler(LoginException.class)
@ResponseStatus(code = HttpStatus.FORBIDDEN)
public Error handleLoginException(LoginException ex) {
	return new Error(HttpStatus.FORBIDDEN.toString(), ex.getMessage());
}

}
