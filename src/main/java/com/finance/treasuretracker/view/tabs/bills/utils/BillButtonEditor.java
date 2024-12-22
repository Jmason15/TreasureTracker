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
            Object billIdObj = table.getModel().getValueAt(modelRow, 0);

            Long billId = getBillId(billIdObj);
            if (billId != null) {
                Bill bill = billController.getBillById(billId);
                if (bill != null) {
                    billsView.openAddBillForm(bill);
                }
            }
        } else if ("Delete".equals(value)) {
            int modelRow = table.convertRowIndexToModel(row);
            Object billIdObj = table.getModel().getValueAt(modelRow, 0);

            Long billId = getBillId(billIdObj);
            if (billId != null) {
                billController.deleteBill(billId);
                billsView.populateTablesWithData();
            }
        }

        return null;
    }
    private Long getBillId(Object billIdObj) {
        if (billIdObj instanceof Integer) {
            return ((Integer) billIdObj).longValue();
        } else if (billIdObj instanceof Long) {
            return (Long) billIdObj;
        } else {
            System.err.println("Unexpected type for bill ID: " + billIdObj.getClass().getName());
            return null;
        }
    }
    }


