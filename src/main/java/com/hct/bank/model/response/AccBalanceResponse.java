package com.hct.bank.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccBalanceResponse implements IResponse {
    private Long accId;
    private Double balance;

}

