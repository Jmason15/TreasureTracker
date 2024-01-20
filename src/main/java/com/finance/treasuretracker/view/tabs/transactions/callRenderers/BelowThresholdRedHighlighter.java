package com.finance.treasuretracker.view.tabs.transactions.callRenderers;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

// Custom cell renderer
public class BelowThresholdRedHighlighter extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        // Assuming the value is Double. Cast or convert accordingly.
        if (value instanceof Double) {
            Double amount = (Double) value;
            if (amount < 1000) {
                c.setBackground(Color.RED);
                c.setForeground(Color.WHITE); // Optional, for better readability
            } else {
                c.setBackground(table.getBackground());
                c.setForeground(table.getForeground());
            }
        }

        return c;
    }
}

