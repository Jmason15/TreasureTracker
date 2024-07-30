package com.finance.treasuretracker.model.dto;

import java.math.BigDecimal;

public interface BreakoutListInterface {
    BigDecimal getTotalIncomeYear();
    BigDecimal getTotalBillYear();
    BigDecimal getRemIncomeYear();
    BigDecimal getRemBillYear();
}
