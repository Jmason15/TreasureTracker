package com.finance.treasuretracker.view.tabs.banks.utils;

import com.finance.treasuretracker.controller.BankController;
import com.finance.treasuretracker.model.Bank;
import com.finance.treasuretracker.view.tabs.banks.BanksView;

import javax.swing.*;
import java.awt.*;

public class ButtonEditor extends DefaultCellEditor {
    protected JButton button;
    private String label;
    private boolean isPushed;
    private BankController bankController;
    private BanksView banksView; // Assuming this is the name of your view class

    private JTable table;

    public ButtonEditor(JCheckBox checkBox, BankController bankController, BanksView banksView, JTable table) {
        super(checkBox);
        this.bankController = bankController;
        this.banksView = banksView;
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
            Object bankIdObj = table.getModel().getValueAt(row, 0); // Retrieve the ID object

            Long bankId;
            if (bankIdObj instanceof Integer) {
                bankId = ((Integer) bankIdObj).longValue(); // Convert Integer to Long
            } else if (bankIdObj instanceof Long) {
                bankId = (Long) bankIdObj; // Cast to Long
            } else {
                throw new IllegalStateException("Bank ID is not of a recognized type");
            }

            if ("Edit".equals(label)) {
                Bank bank = bankController.getById(bankId);
                banksView.openEditBankForm(bank);
            } else if ("Delete".equals(label)) {
                banksView.deleteBank(bankId);
            }
        }
        isPushed = false;
        return label;
    }
}
