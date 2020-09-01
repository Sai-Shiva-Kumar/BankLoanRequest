package com.cg.banking.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cg.banking.entity.Account;
@Repository
public interface IAccountDao extends JpaRepository<Account, String>{
//account dao
}
