package com.finance.treasuretracker.model.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.convert.converter.Converter;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SummaryViewDTO implements Converter<SummaryViewInterface, SummaryViewDTO> {

    String billName;
    Double billAmount;
    String billAccount;
    String frequencyDisplay;
    Integer frequencyValue;
    Long transactionRemaining;
    BigDecimal amountRemaining;
    Long transactionPaid;
    BigDecimal amountPaid;
    Long totalCount;
    Long expectedTotalCount;
    Date firstDate;


    @Override
    public SummaryViewDTO convert(SummaryViewInterface value) {
        this.billName = value.getBillName();
        this.billAmount = value.getBillAmount();
        this.billAccount = value.getBillAccount();
        this.frequencyDisplay = value.getFrequencyDisplay();
        this.frequencyValue = value.getFrequencyValue();
        this.transactionRemaining = value.getTransactionRemaining();
        this.amountRemaining = value.getAmountRemaining();
        this.transactionPaid = value.getTransactionPaid();
        this.amountPaid = value.getAmountPaid();
        this.totalCount = value.getTotalCount();
        this.expectedTotalCount = 0L;
        //this.firstDate = value.getFirstDate();
        return this;
    }

}
