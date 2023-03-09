package com.hct.bank.model.request;

import com.hct.bank.model.TypeOfTransaction;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
public class AccTransReqBody {
    private Long accId;
    private Long toAccId;
//    @Enumerated(EnumType.STRING)
    private String type;
    private Double amount;

}
