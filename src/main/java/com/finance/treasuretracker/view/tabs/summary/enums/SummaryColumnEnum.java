package com.finance.treasuretracker.view.tabs.summary.enums;

import java.util.Arrays;

public enum SummaryColumnEnum {
    NAME("Name"),
    ACCOUNT("Account"),
    FREQUENCY("Frequency"),
    AMOUNT("Amount"),
    //TIMES_PER_YEAR("Times per Year"),
    TOTAL_COUNT("Total Count"),
    TOTAL_EXPECTED_COUNT("Total Expected Count"),
    COUNT_REMAINING("Count Remaining"),
    COUNT_PAID("Count Paid"),
    AMOUNT_REMAINING("Amount Remaining"),
    AMOUNT_PAID("Amount Paid");

    private final String columnName;

    SummaryColumnEnum(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnName() {
        return columnName;
    }

    public static String[] getColumnNames() {
        return Arrays.stream(SummaryColumnEnum.values())
                .map(SummaryColumnEnum::getColumnName)
                .toArray(String[]::new);
    }
}
