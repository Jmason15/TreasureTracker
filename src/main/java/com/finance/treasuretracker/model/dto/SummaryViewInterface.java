package com.finance.treasuretracker.model.dto;

import java.math.BigDecimal;

public interface SummaryViewInterface {
    String getBillName();
    Double getBillAmount();
    String getBillAccount();
    String getFrequencyDisplay();
    String getFrequencyValue();
    Long getTransactionRemaining();
    BigDecimal getAmountRemaining();
    Long getTransactionPaid();
    BigDecimal getAmountPaid();

}
