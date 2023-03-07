package com.hct.bank.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Data
@Table(name = "acc_transactions")
public class AccTransactions {
    @Id
    @Column(name ="transaction_id")
    private long transactionId;
    @Column(name = "transaction_ref_id",nullable = false)
    private long transactionRefid;
    @Column(name = "acc_id")
    private long accId;
    private double credit;
    private double debit;
    @Column(name = "avv_balance ")
    private double avvBalance;
    private Timestamp lastupdated;
}
