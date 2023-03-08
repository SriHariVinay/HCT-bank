package com.hct.bank.controller;

import com.hct.bank.exceptions.InvalidInputException;
import com.hct.bank.model.CustomerDetails;
import com.hct.bank.model.request.AccTransReqBody;
import com.hct.bank.model.request.CustDetailsReqBody;
import com.hct.bank.model.request.CustLoginCredReqBody;
import com.hct.bank.model.response.IResponse;
import com.hct.bank.service.IBankService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/hctbank")
public class BankRestController {
    private IBankService iBankService;

    public BankRestController(IBankService iBankService) {
        this.iBankService = iBankService;
    }

    @GetMapping("/")
    public String welcome() {
        return "<h1>welcome to HCT Bank</h1>";

    }

    @PostMapping("/customers")
    public ResponseEntity<IResponse> saveCustomerDetails(@RequestBody CustDetailsReqBody custDetailsReqBody) {
        IResponse response = iBankService.saveCustomerDetails(custDetailsReqBody);
        return new ResponseEntity<IResponse>(response != null ? response : new InvalidInputException("Invalid Details", HttpStatus.BAD_REQUEST.value()), HttpStatus.CREATED);
    }

    @PostMapping("/password")
    public ResponseEntity<String> savePassword(@RequestBody CustLoginCredReqBody custLoginCredReqBody) {
        String response = iBankService.savePassword(custLoginCredReqBody);
        return new ResponseEntity<String>((response != null ? response : new InvalidInputException("Invalid Password", HttpStatus.BAD_REQUEST.value())).toString(), HttpStatus.CREATED);
    }

    @GetMapping("/customers")
    public List<CustomerDetails> all() {
        return iBankService.findAll();
    }

    @GetMapping("/balances/{accId}")
    public ResponseEntity<String> getBalance(@PathVariable("accId") long accId){
        double balance = iBankService.retrieveBalance(accId);
        return new ResponseEntity<String>("Acc balance is: "+balance,HttpStatus.OK);
    }

    @PostMapping("/transaction")
    public ResponseEntity<String> saveAccTransaction(@RequestBody AccTransReqBody accTransReqBody) {
        String response = iBankService.saveAccTransaction(accTransReqBody);
        return new ResponseEntity<String>((response != null ? response : new InvalidInputException("Invalid Details",
                HttpStatus.BAD_REQUEST.value())).toString(), HttpStatus.CREATED);
    }


}
