package com.hct.bank.service;

import com.hct.bank.controller.BankRestController;
import com.hct.bank.model.*;
import com.hct.bank.model.request.*;
import com.hct.bank.model.response.*;
import com.hct.bank.repository.*;
import com.hct.bank.utils.IDGenerator;
import com.hct.bank.validations.PasswordValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;


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
    private static final Logger logger = LoggerFactory.getLogger(BankServiceImpl.class);


    public BankServiceImpl(ICustomerDetailsRepository iCustomerDetailsRepo, ICustomerAddressRepository iCustomerAddressRepo, IAccBalanceRepository iAccBalanceRepo, IAccToCustMapRepository iAccToCustMapRepo, ICustLoginCredRepository iCustLoginCredRepo, IAccTransactionsRepository iAccTransactionsRepo) {
        this.iCustomerDetailsRepo = iCustomerDetailsRepo;
        this.iCustomerAddressRepo = iCustomerAddressRepo;
        this.iAccBalanceRepo = iAccBalanceRepo;
        this.iAccToCustMapRepo = iAccToCustMapRepo;
        this.iCustLoginCredRepo = iCustLoginCredRepo;
        this.iAccTransactionsRepo = iAccTransactionsRepo;
    }

    /**
     * To create a customer, follow the steps below :
     *
     * Save address details by creating <i><b>Address_Id</b></i> and persist <i><b>Address_Id</b></i> into <i><b>Cust_Details</b></i> table by creating <i><b>Cust_Id</b></i>.
     * Create a new <i><b>Acc_Id</b></i> and persist it into <i><b>Acc_Balance</b></i> Table with a static opening balance of 500.0.
     * Insert <i><b>Cust_Id</b></i> and <i><b>Acc_Id</b></i> into <i><b>Cust_Acc_Map</b></i> Table.
     * @param custDetailsReq
     * @return
     */
    @Override
    public CreateCustResponse saveCustomerDetails(CustDetailsReqBody custDetailsReq) {
        logger.info("Inside savecustomerDetails()");

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
        logger.info("returning CustdetailsResponse, Exiting savecustomerDetails()");
        return mapResult ? new CreateCustResponse(custDetailsReq.getName(),custId,accId,iAccBalanceRepo.findBalanceByAccId(accId)) : null;
    }

    @Override
    public Long saveCustomerAddress(CustAddressReqBody custAdressReq) {
        logger.info("Inside saveCustomerAddress()");
        CustomerAddress customerAddress = new CustomerAddress();

        customerAddress.setAddressId(IDGenerator.getId(ADDRESS_ID_LENGTH));
        customerAddress.setCountry(custAdressReq.getCountry());
        customerAddress.setCity(custAdressReq.getCity());
        customerAddress.setAddressLane(custAdressReq.getAddressLane());
        customerAddress.setPin(custAdressReq.getPin());
        customerAddress.setLastUpdated(Timestamp.from(Instant.now()));

        logger.info("Saved Address, exiting saveCustomerAddress()");
        return iCustomerAddressRepo.save(customerAddress).getAddressId();
    }

    @Override
    public Long updateAccount(AccBalanceReqBody accBalanceReq) {
        logger.info("Inside updateAccount()");
        AccBalance accBal = new AccBalance();

        if (accBalanceReq == null) {
            accBal.setAccId(IDGenerator.getId(ACCOUNT_ID_LENGTH));
            accBal.setBalance(500.00);
        } else {
            accBal.setAccId(accBal.getAccId());
            accBal.setBalance(accBalanceReq.getBalance());
        }

        logger.info("Saved AccBalance, exiting updateAccount()");
        return iAccBalanceRepo.save(accBal).getAccId();
    }

    @Override
    public boolean mapAccIdToCustId(AccToCustMap accToCustMap) {
        logger.info("Inside mapAccIdToCustId()");
        AccToCustMap res = iAccToCustMapRepo.save(accToCustMap);
        logger.info("mapped accId to custId, exiting mapAccIdToCustId()");
        return res != null;
    }

    @Override
    public PasswordResponse savePassword(CustLoginCredReqBody custLoginCredReq) {
        logger.info("Inside savePassword()");

        CustLoginCredentials custLoginCred = new CustLoginCredentials();

        custLoginCred.setCustId(custLoginCredReq.getCustId());
        custLoginCred.setPassword(custLoginCredReq.getPassword());

        boolean res = PasswordValidation.isValidPassword(custLoginCredReq.getPassword());
        if (res) {
            iCustLoginCredRepo.save(custLoginCred);
            logger.info("password set successfully, exiting savePassword()");
            return new PasswordResponse(custLoginCred.getCustId(), " Password Updated Successfully!!");
        } else {
            String response = "Please Enter proper password.\n " +
                    "It contains at least 8 characters and at most 20 characters.\n" +
                    "It contains at least one digit.\n" +
                    "It contains at least one upper case alphabet.\n" +
                    "It contains at least one lower case alphabet.\n" +
                    "It contains at least one special character which includes !@#$%&*()-+=^.\n" +
                    "It doesn’t contain any white space.";
            logger.info("improper password, exiting savePassword()");
            return new PasswordResponse(null, response);
        }
    }


    /**
     * Get all customer details if no custId is provided or
     * get details of a specific customer based on custId.
     * @param custId {required=false}
     * @return List of Customers Details
     */
    @Override
    public CustResponses getCustomerDetails(Long custId) {
        logger.info("Inside getCustomerDetails()");
        List<CustResponse> custResponsesList = new ArrayList<>();
        if (custId != null) {
            Optional<CustomerDetails> custDetails = iCustomerDetailsRepo.findById(custId);
            if (custDetails.isPresent()) {
                CustomerDetails CustDetailsRes = custDetails.get();
                custResponsesList.add(new CustResponse(CustDetailsRes.getCustId(), CustDetailsRes.getName(),
                        CustDetailsRes.getAddressId(), CustDetailsRes.getPhone(), CustDetailsRes.getEmail()));
            }
        } else {
            List<CustomerDetails> custDetailsList = iCustomerDetailsRepo.findAll();

            for (CustomerDetails custDetails : custDetailsList) {
                custResponsesList.add(new CustResponse(custDetails.getCustId(), custDetails.getName(),
                        custDetails.getAddressId(), custDetails.getPhone(), custDetails.getEmail()));
            }

        }
        logger.info("Exiting getCustomerDetails()");
        return new CustResponses(custResponsesList);
    }

    @Override
    public IResponse retrieveBalance(Long custId, Long accId) {
        logger.info("Inside getCustomerDetails()");
        AccBalance accBalance = new AccBalance();
        if (custId == null && accId != null) {
            Optional<AccBalance> details = iAccBalanceRepo.findById(accId);
            logger.info("AccBalance fetched by accId, exiting getCustomerDetails()");
            return new AccBalanceResponse(details.get().getAccId(),
                    details.get().getBalance());
        }
        if (custId != null && accId == null) {
            Long accountId = iAccToCustMapRepo.findAccIdByCustId(custId);
            Optional<AccBalance> details = iAccBalanceRepo.findById(accountId);
            logger.info("AccBalance fetched by custId, exiting getCustomerDetails()");
            return new AccBalanceResponse(details.get().getAccId(), details.get().getBalance());
        }
        if (custId == null && accId == null) {
            String message = "Both custId and accId can't be null. Please enter valid details";
            logger.info("custId & accId not provided, exiting getCustomerDetails()");
            return new MessageResponse(message);
        }
        if (custId != null && accId != null) {
            Long accountId = iAccToCustMapRepo.findAccIdByCustId(custId);
            if (accId.equals(accountId)) {
                Optional<AccBalance> details = iAccBalanceRepo.findById(accountId);
                logger.info("AccBalance fetched by accId & custId, exiting getCustomerDetails()");
                return new AccBalanceResponse(details.get().getAccId(), details.get().getBalance());
            } else {
                String message = "Given AccId & custId doesn't mapped";
                logger.info("provided accId & custId are not mapped, exiting getCustomerDetails()");
                return new MessageResponse(message);
            }
        }
        return null;
    }

    @Override
    public IResponse saveAccTransaction(AccTransReqBody accTransReq) {
        Long fromAccId = accTransReq.getAccId();
        Long toAccId = accTransReq.getToAccId();
        Double fromAccBal = iAccBalanceRepo.findBalanceByAccId(fromAccId);
        Double toAccBal = iAccBalanceRepo.findBalanceByAccId(toAccId);

        AccTransactions fromTransaction = new AccTransactions();
        AccTransactions toTransaction = new AccTransactions();

        if (accTransReq.getType().equals("CREDIT")) {
            if (fromAccBal >= accTransReq.getAmount()) {
                //From Customer(Debit for him)
                fromTransaction.setTransactionId(IDGenerator.getId(TRANSACTION_ID_LENGTH));
                long tRefId = IDGenerator.getId(TRANSACTION_REF_ID_LENGTH);
                fromTransaction.setTransactionRefId(tRefId);
                fromTransaction.setAccId(fromAccId);
                fromTransaction.setCredit(0.00);
                fromTransaction.setDebit(accTransReq.getAmount());
                fromTransaction.setAvvBalance(fromAccBal - accTransReq.getAmount());
                fromTransaction.setLastupdated(Timestamp.from(Instant.now()));

                iAccBalanceRepo.debitBalanceByAccId(fromAccId, fromTransaction.getDebit());
                iAccTransactionsRepo.save(fromTransaction);

                //To Customer(CREDIT for him)
                toTransaction.setTransactionId(IDGenerator.getId(TRANSACTION_ID_LENGTH));
                toTransaction.setTransactionRefId(tRefId);
                toTransaction.setAccId(toAccId);
                toTransaction.setCredit(accTransReq.getAmount());
                toTransaction.setDebit(0.00);
                toTransaction.setAvvBalance(toAccBal + accTransReq.getAmount());
                toTransaction.setLastupdated(Timestamp.from(Instant.now()));

                iAccBalanceRepo.creditBalanceByAccId(toAccId, toTransaction.getCredit());
                iAccTransactionsRepo.save(toTransaction);
                return new MessageResponse("Transaction Successful & TransactionRefId is : " + tRefId);
            } else {
                return new MessageResponse("Enter valid credit amount");
            }
        }

        if (accTransReq.getType().equals("DEBIT")) {
            if (toAccBal >= accTransReq.getAmount()) {
                //From Customer(CREDIT for him)
                fromTransaction.setTransactionId(IDGenerator.getId(TRANSACTION_ID_LENGTH));
                long tRefId = IDGenerator.getId(TRANSACTION_REF_ID_LENGTH);
                fromTransaction.setTransactionRefId(tRefId);
                fromTransaction.setAccId(fromAccId);
                fromTransaction.setCredit(accTransReq.getAmount());
                fromTransaction.setDebit(0.00);
                fromTransaction.setAvvBalance(fromAccBal + accTransReq.getAmount());
                fromTransaction.setLastupdated(Timestamp.from(Instant.now()));

                iAccBalanceRepo.creditBalanceByAccId(fromAccId, fromTransaction.getCredit());
                iAccTransactionsRepo.save(fromTransaction);

                //To Customer (DEBIT for him)
                toTransaction.setTransactionId(IDGenerator.getId(TRANSACTION_ID_LENGTH));
                toTransaction.setTransactionRefId(tRefId);
                toTransaction.setAccId(toAccId);
                toTransaction.setCredit(0.00);
                toTransaction.setDebit(accTransReq.getAmount());
                toTransaction.setAvvBalance(toAccBal - accTransReq.getAmount());
                toTransaction.setLastupdated(Timestamp.from(Instant.now()));

                iAccBalanceRepo.debitBalanceByAccId(toAccId, toTransaction.getDebit());
                iAccTransactionsRepo.save(toTransaction);
                return new MessageResponse("Transaction Successful & TransactionRefId is : " + tRefId);
            } else {
                return new MessageResponse("Enter valid debit amount");
            }
        } else
            return new MessageResponse("please enter valid type of transaction i.e., CREDIT / DEBIT");
    }

    @Override
    public IResponse getTransactions(Long accId, Long transactionRefId) {
        List<AccTransactionsResponse> accTransactionsList = new ArrayList<>();
        if (transactionRefId == null && accId != null) {
           List<AccTransactions> res = iAccTransactionsRepo.findTransactionsFromAId(accId);
            if (res.size()>0) {

                res.stream().forEach(accTransactions -> accTransactionsList.add(new AccTransactionsResponse(accTransactions.getTransactionId(),
                        accTransactions.getTransactionRefId(), accTransactions.getAccId(), accTransactions.getCredit(),
                        accTransactions.getDebit(), accTransactions.getAvvBalance(), accTransactions.getLastupdated())));
            }
            return new AccTransactionListResponse(accTransactionsList);
        }

        if (transactionRefId != null && accId == null) {
            List<AccTransactions> res = iAccTransactionsRepo.findTransactionsFromTId(transactionRefId);
            if (res.size()>0) {

                res.stream().forEach(accTransactions -> accTransactionsList.add(new AccTransactionsResponse(accTransactions.getTransactionId(),
                        accTransactions.getTransactionRefId(), accTransactions.getAccId(), accTransactions.getCredit(),
                        accTransactions.getDebit(), accTransactions.getAvvBalance(), accTransactions.getLastupdated())));
            }
            return new AccTransactionListResponse(accTransactionsList);
        }
        if (transactionRefId != null && accId != null) {
            Optional<AccTransactions> res = iAccTransactionsRepo.findById(iAccTransactionsRepo.findTIdFromAIdAndTRId(accId, transactionRefId));
            if (res.isPresent()) {

                res.stream().forEach(accTransactions -> accTransactionsList.add(new AccTransactionsResponse(accTransactions.getTransactionId(),
                        accTransactions.getTransactionRefId(), accTransactions.getAccId(), accTransactions.getCredit(),
                        accTransactions.getDebit(), accTransactions.getAvvBalance(), accTransactions.getLastupdated())));
            }
            return new AccTransactionListResponse(accTransactionsList);
        }
        if (transactionRefId == null && accId == null) {
            String res = "Both Transaction ReferenceId and AccountId cannot be zero. Please give valid details.";
            return new  MessageResponse(res);
        }
        return new AccTransactionListResponse(accTransactionsList);
    }

}
