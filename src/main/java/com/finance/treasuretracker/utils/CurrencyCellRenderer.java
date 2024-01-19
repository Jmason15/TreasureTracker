package com.finance.treasuretracker.utils;

import javax.swing.table.DefaultTableCellRenderer;
import java.awt.Color;
import java.awt.Component;
import java.text.NumberFormat;
import javax.swing.JTable;

public class CurrencyCellRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (value instanceof Number) {
            Number num = (Number) value;
            setText(formatAsCurrency(num.doubleValue()));
            if (num.doubleValue() < 0) {
                setForeground(new Color(139, 0, 0)); // Darker red
            } else {
                setForeground(new Color(0, 100, 0)); // Darker green
            }
            setHorizontalAlignment(RIGHT);
        }
        return c;
    }

    private String formatAsCurrency(double amount) {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
        return currencyFormat.format(amount);
    }
}

