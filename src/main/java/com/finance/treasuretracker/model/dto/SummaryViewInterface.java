package com.finance.treasuretracker.model.dto;

import java.math.BigDecimal;
import java.sql.Date;

public interface SummaryViewInterface {
    String getBillName();

    Double getBillAmount();

    String getBillAccount();

    String getFrequencyDisplay();

    Integer getFrequencyValue();

    Long getTransactionRemaining();

    BigDecimal getAmountRemaining();

    Long getTransactionPaid();

    BigDecimal getAmountPaid();

    Long getTotalCount();

    Long getBillId();

    String getDueDate();

}
