package com.hct.bank.repository;

import com.hct.bank.model.AccTransactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IAccTransactionsRepository extends JpaRepository<AccTransactions,Long> {
    @Query(value = "SELECT transaction_id FROM acc_transactions WHERE acc_id = ?1 AND transaction_ref_id = ?2",nativeQuery = true)
    Long findTIdFromAIdAndTRId(long accId, long transactionRefId);

    @Query(value = "SELECT *  FROM acc_transactions WHERE acc_id = ?1", nativeQuery = true)
    List<AccTransactions> findTransactionsFromAId(long accId);

    @Query(value = "SELECT * FROM acc_transactions WHERE transaction_ref_id = ?1",nativeQuery = true)
    List<AccTransactions> findTransactionsFromTId(Long transactionRefId);
}
