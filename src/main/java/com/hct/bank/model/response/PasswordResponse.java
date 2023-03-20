package com.hct.bank.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class PasswordResponse implements IResponse{
    private Long custId;
    private String response;
}
