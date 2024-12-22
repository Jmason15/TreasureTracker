package com.finance.treasuretracker.view.tabs.transactions.callRenderers;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

// Custom cell renderer
public class BelowThresholdRedHighlighter extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        // Assuming the value is a formatted currency string. Parse it to Double.
        if (value instanceof String) {
            try {
                Number number = NumberFormat.getCurrencyInstance(Locale.US).parse((String) value);
                double amount = number.doubleValue();
                if (amount < 500) {
                    c.setBackground(Color.RED);
                    c.setForeground(Color.WHITE); // Optional, for better readability
                }else if (amount < 1000) {
                    c.setBackground(Color.ORANGE);
                    c.setForeground(Color.WHITE); // Optional, for better readability
                }else {
                    c.setBackground(table.getBackground());
                    c.setForeground(table.getForeground());
                }
            } catch (ParseException e) {

            }
        }

        return c;
    }
}