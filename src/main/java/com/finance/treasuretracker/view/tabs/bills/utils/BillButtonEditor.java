package com.finance.treasuretracker.view.tabs.bills.utils;
import com.finance.treasuretracker.controller.BillController;
import com.finance.treasuretracker.model.Bill;
import com.finance.treasuretracker.view.tabs.bills.BillsView;

import javax.swing.*;
import java.awt.*;

public class BillButtonEditor extends DefaultCellEditor {
    protected JButton button;
    private String label;
    private boolean isPushed;
    private BillController billController;
    private BillsView billsView; // Assuming this is the name of your view class

    private JTable table;

    public BillButtonEditor(JCheckBox checkBox, BillController billController, BillsView billsView, JTable table) {
        super(checkBox);
        this.billController = billController;
        this.billsView = billsView;
        this.table = table;
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(e -> fireEditingStopped());
    }

    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        if (value == null) return null;
        label = value.toString();
        button.setText(label);
        isPushed = true;
        return button;
    }

    public Object getCellEditorValue() {
        if (isPushed) {
            int row = table.convertRowIndexToModel(table.getEditingRow());
            Object billIdObj = table.getModel().getValueAt(row, 0); // Retrieve the ID object

            Long billId;
            if (billIdObj instanceof Integer) {
                billId = (Long) billIdObj; // Cast to Integer
            } else {
                throw new IllegalStateException("Bill ID is not of a recognized type");
            }

            if ("Edit".equals(label)) {
                Bill bill = billController.getBillById(billId);
                billsView.openAddBillForm(bill);
            } else if ("Delete".equals(label)) {
                billController.deleteBill(billId);
            }
        }
        isPushed = false;
        return label;
    }
}

