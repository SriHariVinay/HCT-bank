package com.hct.bank.model.request;

import lombok.Data;

@Data
public class CustAddressReqBody {
    private String country;
    private String city;
    private String addressLane;
    private long pin;

}
