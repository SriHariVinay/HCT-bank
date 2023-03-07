package com.hct.bank.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreatePasswordResponse implements IResponse{
    private long custId;
    private String report;
}
