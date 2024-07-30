package com.finance.treasuretracker.view.tabs.summary.enums;

import java.util.Arrays;

public enum YearMonthTakehomeEnum {
    DESCRIPTION("Description"),
    TOTAL_YEAR("Total Year"),
    TOTAL_MONTH("Total Month"),
    REM_YEAR("Remaining Year"),
    REM_MONTH("Remaining Month");


    private final String columnName;

    YearMonthTakehomeEnum(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnName() {
        return columnName;
    }

    public static String[] getColumnNames() {
        return Arrays.stream(YearMonthTakehomeEnum.values())
                .map(YearMonthTakehomeEnum::getColumnName)
                .toArray(String[]::new);
    }
}
