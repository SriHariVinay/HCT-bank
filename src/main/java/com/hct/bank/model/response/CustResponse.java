package com.hct.bank.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustResponse {
    private long custId;
    private String name;
    private Long addressId;
    private long phone;
    private String email;
}
