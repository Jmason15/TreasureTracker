package com.finance.treasuretracker.view.tabs.transactions.callRenderers;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DueDateHighlighter extends DefaultTableCellRenderer {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd"); // Adjust the date format as needed

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        boolean paid = Boolean.parseBoolean(table.getValueAt(row, table.getColumn("Paid").getModelIndex()).toString());
        Date dueDate = null;
        if (table.getValueAt(row, table.getColumn("Date").getModelIndex()) == null
                || table.getValueAt(row, table.getColumn("Date").getModelIndex()).toString().isEmpty()) {
            c.setBackground(Color.BLACK);
        }else{
            try {
                dueDate = DATE_FORMAT.parse(table.getValueAt(row, table.getColumn("Date").getModelIndex()).toString());
            } catch (ParseException e) {
                c.setBackground(Color.BLACK);
            }

            if (!paid && dueDate != null && dueDate.before(new Date())) {
                c.setBackground(Color.RED);
            } else {
                c.setBackground(Color.WHITE);
            }
        }

        return c;
    }
}
