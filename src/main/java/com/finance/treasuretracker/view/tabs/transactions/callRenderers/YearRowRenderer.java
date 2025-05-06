package com.finance.treasuretracker.view.tabs.transactions.callRenderers;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class YearRowRenderer extends DefaultTableCellRenderer {
    private final DefaultTableCellRenderer defaultRenderer = new DefaultTableCellRenderer();

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = defaultRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (table.getModel().getValueAt(row, 0) instanceof String) {
            c.setFont(c.getFont().deriveFont(Font.BOLD));
            c.setBackground(Color.BLACK);
        } else {
            c.setFont(c.getFont().deriveFont(Font.PLAIN));
            c.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
        }
        return c;
    }
}