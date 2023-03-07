package com.hct.bank.model.request;

import lombok.Data;

@Data
public class CustLoginCredReqBody {
    private long custId;
    private String password;
}
