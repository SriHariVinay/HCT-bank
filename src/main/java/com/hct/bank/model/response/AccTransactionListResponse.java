package com.hct.bank.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AccTransactionListResponse implements IResponse{
    List<AccTransactionsResponse> accTransactionsList;
}
