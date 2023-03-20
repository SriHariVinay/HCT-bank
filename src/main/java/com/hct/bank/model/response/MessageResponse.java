package com.hct.bank.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MessageResponse implements IResponse {
    private String message;

}