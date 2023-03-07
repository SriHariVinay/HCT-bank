package com.hct.bank.repository;

import com.hct.bank.model.AccBalance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAccBalanceRepository extends JpaRepository<AccBalance,Long> {
}
