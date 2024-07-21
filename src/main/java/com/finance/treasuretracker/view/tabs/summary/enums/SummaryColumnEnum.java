package com.finance.treasuretracker.view.tabs.summary.enums;

import com.finance.treasuretracker.view.tabs.bills.enums.BillColumnENUM;

import java.util.Arrays;

public enum SummaryColumnEnum {
    NAME("Name"),
    AMOUNT("Amount"),
    ACCOUNT("Account");

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
