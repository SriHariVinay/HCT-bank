package com.hct.bank.model.request;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustDetailsReqBody {
    @NotNull
    private String name;
    private long phone;
    private CustAddressReqBody address;
    private String email;
}
