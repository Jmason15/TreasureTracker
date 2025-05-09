package com.finance.treasuretracker.view.tabs.bills.enums;

import com.fasterxml.jackson.databind.util.EnumValues;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum BillColumnENUM {
    ID("ID"),
    NAME("Name"),
    DUE_DAY("Start Date"),
    END_DATE("End Date"),
    FREQUENCY("Frequency"),
    ACCOUNT("Account"),
    BANK("Bank"),
    AMOUNT("Amount"),
    ALTERNATE("Alternate"),
    EDIT("Edit"),
    DELETE("Delete");

    private final String columnName;

    BillColumnENUM(String columnName) {
        this.columnName = columnName;
    }

    public static String[] getColumnNames() {
        return Arrays.stream(BillColumnENUM.values())
                .map(BillColumnENUM::getColumnName)
                .toArray(String[]::new);
    }
}
