package com.hct.bank.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
public class AccTransactionsResponse {
    private Long transactionId;
    private Long transactionRefId;
    private Long accId;
    private Double credit;
    private Double debit;
    private Double avvBalance;
    private Timestamp lastupdated;
}
