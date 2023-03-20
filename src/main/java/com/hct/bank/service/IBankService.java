package com.hct.bank.service;

import com.hct.bank.model.AccToCustMap;
import com.hct.bank.model.request.*;
import com.hct.bank.model.response.*;

public interface IBankService {

    /**
     * To create a customer, follow the steps below :
     *
     * Save address details by creating <i><b>Address_Id</b></i> and persist <i><b>Address_Id</b></i> into <i><b>Cust_Details</b></i> table by creating <i><b>Cust_Id</b></i>.
     * Create a new <i><b>Acc_Id</b></i> and persist it into <i><b>Acc_Balance</b></i> Table with a static opening balance of 500.0.
     * Insert <i><b>Cust_Id</b></i> and <i><b>Acc_Id</b></i> into <i><b>Cust_Acc_Map</b></i> Table.
     * @param custDetailsReq
     * @return
     */
    CreateCustResponse saveCustomerDetails(CustDetailsReqBody custDetailsReq);
    Long saveCustomerAddress(CustAddressReqBody custAddressReq);
    Long updateAccount(AccBalanceReqBody accBalanceReq);
    boolean mapAccIdToCustId(AccToCustMap accToCustMap);
    PasswordResponse savePassword(CustLoginCredReqBody custLoginCredReq);
    /**
     * Get all customer details if no custId is provided or
     * get details of a specific customer based on custId.
     * @param custId {required=false}
     * @return List of Customers Details
     */
    CustResponses getCustomerDetails(Long custId);
    IResponse retrieveBalance(Long custId, Long accId);
    IResponse saveAccTransaction(AccTransReqBody accTransReq);
    IResponse getTransactions(Long accId, Long transactionRefId);
}
