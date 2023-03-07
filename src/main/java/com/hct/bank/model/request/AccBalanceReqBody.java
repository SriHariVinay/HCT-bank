package com.hct.bank.model.request;

import lombok.Data;

@Data
public class AccBalanceReqBody {
    private long accId;
    private double balance;

}
