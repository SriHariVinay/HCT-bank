package com.hct.bank.controller;

import com.hct.bank.exceptions.InvalidInputException;
import com.hct.bank.model.request.AccTransReqBody;
import com.hct.bank.model.request.CustDetailsReqBody;
import com.hct.bank.model.request.CustLoginCredReqBody;
import com.hct.bank.model.response.*;
import com.hct.bank.service.IBankService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/hctbank")
public class BankRestController {
    private static final Logger logger = LoggerFactory.getLogger(BankRestController.class);
    private final IBankService iBankService;

    public BankRestController(IBankService iBankService) {
        this.iBankService = iBankService;
    }

    @GetMapping("/")
    public String welcome() {
        logger.info("[welcome] inside welcome method");
        return "welcome to HCT Bank";

    }

    /**Helps to create a customer, follow the steps below :
     *
     * Save address details by creating <i><b>Address_Id</b></i> and persist <i><b>Address_Id</b></i> into <i><b>Cust_Details</b></i> table by creating <i><b>Cust_Id</b></i>.
     * Create a new <i><b>Acc_Id</b></i> and persist it into <i><b>Acc_Balance</b></i> Table with a static opening balance of 500.0.
     * Insert <i><b>Cust_Id</b></i> and <i><b>Acc_Id</b></i> into <i><b>Cust_Acc_Map</b></i> Table.
     *
     * @param custDetailsReqBody
     * @return response
     */
    @PostMapping("/customers")
    public ResponseEntity<IResponse> saveCustomerDetails(@RequestBody CustDetailsReqBody custDetailsReqBody) {
        logger.info("invoked /customers API");
        CreateCustResponse response = iBankService.saveCustomerDetails(custDetailsReqBody);
        if (response == null) {
            return new ResponseEntity<>(new InvalidInputException("Invalid Details"), HttpStatus.BAD_REQUEST);
        }
        logger.info("exiting /customers API");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/password")
    public ResponseEntity<IResponse> updatePassword(@RequestBody CustLoginCredReqBody custLoginCredReqBody) {
        logger.info("invoked /password API");
        PasswordResponse response = iBankService.savePassword(custLoginCredReqBody);
        if (response.getCustId() == null) {
            return new ResponseEntity<>(new InvalidInputException(response.getResponse()), HttpStatus.BAD_REQUEST);
        }
        logger.info("exiting /password API");
        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }

    /**
     * Helps in Getting all customer details if no custId is provided or
     * get details of a specific customer based on custId.
     * @param custId
     * @return Customers Details List
     */
    @GetMapping("/customers")
    public ResponseEntity<IResponse> getAllCustomers(@RequestParam(name = "custId", required = false) Long custId) {
        logger.info("invoked /customers GET API");
        CustResponses custResponse = iBankService.getCustomerDetails(custId);
        if (custResponse.getCustomersList().isEmpty()) {
            return new ResponseEntity<>(new InvalidInputException("Invalid CustomerId"), HttpStatus.BAD_REQUEST);
        }
        logger.info("exiting /customers GET API");
        return new ResponseEntity<>(custResponse, HttpStatus.OK);
    }

    /**
     *
     * @param custId
     * @param accId
     * @return accId, balance
     */
    @GetMapping("/balances")
    public ResponseEntity<IResponse> getBalance(@RequestParam(name = "custId", required = false) Long custId, @RequestParam(name = "accId", required = false) Long accId) {
        logger.info("invoked /balances GET API");
        IResponse response = iBankService.retrieveBalance(custId, accId);
        if (response == null) {
            return new ResponseEntity<>(new InvalidInputException("Invalid Account Details"), HttpStatus.BAD_REQUEST);
        }
        logger.info("exiting /balances GET API");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/transactions")
    public ResponseEntity<IResponse> saveAccTransaction(@RequestBody AccTransReqBody accTransReqBody) {
        logger.info("invoked /transactions POST API");
        IResponse response = iBankService.saveAccTransaction(accTransReqBody);
        if (response == null) {
            return new ResponseEntity<>(new InvalidInputException("Invalid Details"), HttpStatus.BAD_REQUEST);
        }
        logger.info("exiting /transactions POST API");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/transactions")
    public ResponseEntity<IResponse> getTransactions(@RequestParam(name = "accId",required = false) Long accId, @RequestParam(name = "transactionRefId",required = false) Long transactionRefId) {
        logger.info("invoked /transactions GET API");
        IResponse response = iBankService.getTransactions(accId, transactionRefId);
        if (response == null) {
            return new ResponseEntity<>(new InvalidInputException("Invalid Details"), HttpStatus.BAD_REQUEST);
        }
        logger.info("exiting /transactions GET API");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
