package com.hct.bank.model.request;

import lombok.Data;

@Data
public class CustDetailsReqBody {
    private String name;
    private long phone;
    private CustAddressReqBody address;
    private String email;
}
