package com.finance.treasuretracker.view.tabs.bills.utils;
import com.finance.treasuretracker.controller.BillController;
import com.finance.treasuretracker.model.Bill;
import com.finance.treasuretracker.view.tabs.bills.BillsView;

import javax.swing.*;
import java.awt.*;

public class BillButtonEditor extends DefaultCellEditor {
    private JButton editButton;
        private JButton deleteButton;
        private BillController billController;
        private BillsView billsView; // Assuming this is the name of your view class

        private JTable table;

        public BillButtonEditor(JCheckBox checkBox, BillController billController, BillsView billsView, JTable table) {
            super(checkBox);
            this.billController = billController;
            this.billsView = billsView;
            this.table = table;
            editButton = new JButton("Edit");
            deleteButton = new JButton("Delete");
            editButton.addActionListener(e -> fireEditingStopped());
            deleteButton.addActionListener(e -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            if ("Edit".equals(value)) {
                int modelRow = table.convertRowIndexToModel(row);
                Object billIdObj = table.getModel().getValueAt(modelRow, 0); // Retrieve the ID object

                Long billId = null;
                if (billIdObj instanceof Integer) {
                    // If it's an Integer, convert to Long
                    billId = ((Integer) billIdObj).longValue();
                } else if (billIdObj instanceof Long) {
                    // If it's already a Long, cast directly
                    billId = (Long) billIdObj;
                } else {
                    // Handle other unexpected types, perhaps log a warning or error
                    System.err.println("Unexpected type for bill ID: " + billIdObj.getClass().getName());
                }

                // Fetch the bill by ID from your BillController
                Bill bill = billController.getBillById(billId);
                if (bill != null) {
                    billsView.openAddBillForm(bill); // Open the form with the selected bill for editing
                }
            } else if ("Delete".equals(value)) {
                int modelRow = table.convertRowIndexToModel(row);
                Object billIdObj = table.getModel().getValueAt(modelRow, 0); // Retrieve the ID object

                Long billId;
                if (billIdObj instanceof Long) {
                    billId = (Long) billIdObj; // Cast to Integer
                } else {
                    throw new IllegalStateException("Bill ID is not of a recognized type");
                }

                // Delete the bill by ID from your BillController
                billController.deleteBill(billId);
                billsView.populateTableWithData(); // Refresh the table
            }

            return null;
        }
    }


