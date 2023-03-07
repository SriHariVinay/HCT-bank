package com.hct.bank.model.request;

import lombok.Data;

@Data
public class AccTransReqBody {
    private long fromAccountId;
    private long toAccountId;
    private double credit;
    private double debit;
}
