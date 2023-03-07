package com.hct.bank.exceptions;

import com.hct.bank.model.response.IResponse;
//import org.springframework.http.HttpStatus;

public interface IException extends IResponse {
    void setMessage(String message, int status);
//    public void AccountNotExistException(String message, String errorCode);
//    public void AccountNotExistException(String message, String errorCode, HttpStatus httpStatus);

}
