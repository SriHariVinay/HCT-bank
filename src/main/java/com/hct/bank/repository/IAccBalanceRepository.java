package com.hct.bank.repository;

import com.hct.bank.model.AccBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

public interface IAccBalanceRepository extends CrudRepository<AccBalance, Long>, JpaRepository<AccBalance, Long> {
    @Query("select balance from AccBalance where accId = ?1")
    Double findBalanceByAccId(Long accId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update AccBalance set balance = balance-?2 where accId = ?1")
    public void debitBalanceByAccId(long accId, double balance);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update AccBalance set balance = balance+?2 where accId = ?1")
    public void creditBalanceByAccId(long accId, double balance);
}
