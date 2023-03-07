package com.hct.bank.repository;

import com.hct.bank.model.AccTransactions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAccTransactionsRepository extends JpaRepository<AccTransactions,Long> {
}
