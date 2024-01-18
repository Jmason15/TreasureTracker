package com.finance.treasuretracker.model.dto;

import java.util.Date;

public interface TransactionGridInterface {
    Integer getTransactionId();
    Boolean getPaid();
    String getBillName();
    Double getBillAmount();
    String getBankName();
    Integer getBankId();
    Date getTransactionDate();
    String getAccountDisplayName();
}
