package com.hct.bank.service;

import com.hct.bank.model.AccToCustMap;
import com.hct.bank.model.CustomerDetails;
import com.hct.bank.model.request.*;
import com.hct.bank.model.response.IResponse;

import java.util.List;

public interface IBankService {

    IResponse saveCustomerDetails(CustDetailsReqBody custDetailsReq);
    Long saveCustomerAddress(CustAddressReqBody custAddressReq);
    Long updateAccount(AccBalanceReqBody accBalanceReq);
    boolean mapAccIdToCustId(AccToCustMap accToCustMap);
    String savePassword(CustLoginCredReqBody custLoginCredReq);
    List<CustomerDetails> findAll();
//    long saveAccTransaction(AccTransReqBody accTransReq);


}
