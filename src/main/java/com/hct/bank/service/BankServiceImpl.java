package com.hct.bank.service;

import com.hct.bank.model.*;
import com.hct.bank.model.request.*;
import com.hct.bank.model.response.CreateCustResponse;
import com.hct.bank.model.response.IResponse;
import com.hct.bank.repository.*;
import com.hct.bank.utils.IDGenerator;
import com.hct.bank.validations.PasswordValidation;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;


@Service
public class BankServiceImpl implements IBankService {
    private static final int ADDRESS_ID_LENGTH = 6;
    private static final int CUSTOMER_ID_LENGTH = 6;
    private static final int ACCOUNT_ID_LENGTH = 12;
    private static final int TRANSACTION_ID_LENGTH = 7;
    private static final int TRANSACTION_REF_ID_LENGTH = 7;

    private final ICustomerDetailsRepository iCustomerDetailsRepo;
    private final ICustomerAddressRepository iCustomerAddressRepo;
    private final IAccBalanceRepository iAccBalanceRepo;
    private final IAccToCustMapRepository iAccToCustMapRepo;
    private final ICustLoginCredRepository iCustLoginCredRepo;
    private final IAccTransactionsRepository iAccTransactionsRepo;


    public BankServiceImpl(ICustomerDetailsRepository iCustomerDetailsRepo,
                           ICustomerAddressRepository iCustomerAddressRepo,
                           IAccBalanceRepository iAccBalanceRepo, IAccToCustMapRepository iAccToCustMapRepo,
                           ICustLoginCredRepository iCustLoginCredRepo, IAccTransactionsRepository iAccTransactionsRepo) {
        this.iCustomerDetailsRepo = iCustomerDetailsRepo;
        this.iCustomerAddressRepo = iCustomerAddressRepo;
        this.iAccBalanceRepo = iAccBalanceRepo;
        this.iAccToCustMapRepo = iAccToCustMapRepo;
        this.iCustLoginCredRepo = iCustLoginCredRepo;
        this.iAccTransactionsRepo = iAccTransactionsRepo;
    }

    @Override
    public IResponse saveCustomerDetails(CustDetailsReqBody custDetailsReq) {

        Long addressId = saveCustomerAddress(custDetailsReq.getAddress());
        CustomerDetails customerDetails = new CustomerDetails();

        customerDetails.setCustId(IDGenerator.getId(CUSTOMER_ID_LENGTH));
        customerDetails.setName(custDetailsReq.getName());
        customerDetails.setPhone(custDetailsReq.getPhone());
        customerDetails.setEmail(custDetailsReq.getEmail());
        customerDetails.setAddressId(addressId);

        Timestamp timestamp = Timestamp.from(Instant.now());

        customerDetails.setCreated(timestamp);
        customerDetails.setLastUpdated(timestamp);

        Long custId = iCustomerDetailsRepo.save(customerDetails).getCustId();

        Long accId = updateAccount(null);

        boolean mapResult = mapAccIdToCustId(new AccToCustMap(accId, custId));
        return mapResult ? new CreateCustResponse(custId) : null;
    }

    @Override
    public Long saveCustomerAddress(CustAddressReqBody custAdressReq) {
        CustomerAddress customerAddress = new CustomerAddress();

        customerAddress.setAddressId(IDGenerator.getId(ADDRESS_ID_LENGTH));
        customerAddress.setCountry(custAdressReq.getCountry());
        customerAddress.setCity(custAdressReq.getCity());
        customerAddress.setAddressLane(custAdressReq.getAddressLane());
        customerAddress.setPin(custAdressReq.getPin());
        customerAddress.setLastUpdated(Timestamp.from(Instant.now()));

        return iCustomerAddressRepo.save(customerAddress).getAddressId();
    }

    @Override
    public Long updateAccount(AccBalanceReqBody accBalanceReq) {
        AccBalance accBal = new AccBalance();

        if (accBalanceReq == null) {
            accBal.setAccId(IDGenerator.getId(ACCOUNT_ID_LENGTH));
            accBal.setBalance(500.00);
        } else {
            accBal.setAccId(accBal.getAccId());
            accBal.setBalance(accBalanceReq.getBalance());
        }

        return iAccBalanceRepo.save(accBal).getAccId();
    }

    @Override
    public boolean mapAccIdToCustId(AccToCustMap accToCustMap) {
        AccToCustMap res = iAccToCustMapRepo.save(accToCustMap);
        return res != null;
    }

    @Override
    public String savePassword(CustLoginCredReqBody custLoginCredReq) {
        CustLoginCredentials custLoginCred = new CustLoginCredentials();

        custLoginCred.setCustId(custLoginCredReq.getCustId());
        custLoginCred.setPassword(custLoginCredReq.getPassword());


        boolean res = PasswordValidation.isValidPassword(custLoginCredReq.getPassword());
        if (res) {
            iCustLoginCredRepo.save(custLoginCred);
            return "details are saved for : " + custLoginCred.getCustId();
        } else {
            return "Please Enter proper password";
//                    "It contains at least 8 characters and at most 20 characters.\n" +
//                    "It contains at least one digit.\n" +
//                    "It contains at least one upper case alphabet.\n" +
//                    "It contains at least one lower case alphabet.\n" +
//                    "It contains at least one special character which includes !@#$%&*()-+=^.\n" +
//                    "It doesn’t contain any white space.";
        }

    }

    @Override
    public List<CustomerDetails> findAll() {
        return iCustomerDetailsRepo.findAll();
    }

    @Override
    public double retrieveBalance(long accId){
        AccBalance accBalance = new AccBalance();
        Optional<AccBalance> accountId = iAccBalanceRepo.findById(accId);
        return accBalance.getBalance();

    }

    @Override
    public String saveAccTransaction(AccTransReqBody accTransReq) {
        AccTransactions accTransactions = new AccTransactions();
        AccBalance accBal = new AccBalance();

        long fromAccId = accTransReq.getFromAccountId();
        long toAccId = accTransReq.getToAccountId();

        accTransactions.setAccId(fromAccId);
        accTransactions.setCredit(accTransReq.getCredit());
        accTransactions.setDebit(accTransReq.getDebit());

        if (accTransReq.getCredit() != 0 && accTransReq.getDebit() == 0) {
            accBal.setBalance(accBal.getBalance() + accTransReq.getCredit());
            accTransactions.setTransactionId(IDGenerator.getId(TRANSACTION_ID_LENGTH));
        }
        if (accTransReq.getDebit() != 0 && accTransReq.getCredit() == 0) {
            if (accBal.getBalance() >= accTransReq.getDebit()) {
                accBal.setBalance(accBal.getBalance() - accTransReq.getDebit());
                accTransactions.setTransactionId(IDGenerator.getId(TRANSACTION_ID_LENGTH));
            } else
                return "Insufficient Balance";
        }
        iAccBalanceRepo.save(accBal);
        long transRefId = IDGenerator.getId(TRANSACTION_REF_ID_LENGTH);
        accTransactions.setTransactionRefid(transRefId);
        accTransactions.setAvvBalance(accBal.getBalance());
        accTransactions.setLastupdated(Timestamp.from(Instant.now()));
        iAccTransactionsRepo.save(accTransactions);
        return "Transaction Successful for " + accTransactions.getAccId();
    }

}
