package com.hct.bank.exceptions;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public class InvalidInputException implements IException {
    public String message = null;
//    public String errorCode;
//    public HttpStatus httpStatus;
    @Override
    public void setMessage(String message, int status) {
        this.message = message;
    }

//    @Override
//    public void AccountNotExistException(String message, String errorCode) {
//        this.message = message;
//        this.errorCode = errorCode;
//    }
//
//    @Override
//    public void AccountNotExistException(String message, String errorCode, HttpStatus httpStatus) {
//        this.message = message;
//        this.errorCode = errorCode;
//        this.httpStatus = httpStatus;
//    }

}
