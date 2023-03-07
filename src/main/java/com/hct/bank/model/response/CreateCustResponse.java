package com.hct.bank.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
//@AllArgsConstructor
public class CreateCustResponse implements IResponse {
    private Long custId;

    public CreateCustResponse(Long custId) {
        this.custId = custId;
    }
}
