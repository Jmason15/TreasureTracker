package com.finance.treasuretracker.view.tabs.accounts.utils;

import com.finance.treasuretracker.controller.AccountController;
import com.finance.treasuretracker.model.Account;
import com.finance.treasuretracker.view.tabs.accounts.AccountsView;

import javax.swing.*;
import java.awt.*;

public class AccountButtonEditor extends DefaultCellEditor {
    protected JButton button;
    private String label;
    private boolean isPushed;
    private final AccountController accountController;
    private final AccountsView accountsView; // Assuming this is the name of your view class

    private final JTable table;

    public AccountButtonEditor(JCheckBox checkBox, AccountController accountController, AccountsView accountsView, JTable table) {
        super(checkBox);
        this.accountController = accountController;
        this.accountsView = accountsView;
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
            Object accountIdObj = table.getModel().getValueAt(row, 0); // Retrieve the ID object

            Long accountId = null;

            if (accountIdObj != null) {
                if (accountIdObj instanceof Long) {
                    accountId = (Long) accountIdObj;
                } else if (accountIdObj instanceof Integer) {
                    accountId = ((Integer) accountIdObj).longValue();
                } else if (accountIdObj instanceof String) {
                    try {
                        accountId = Long.parseLong((String) accountIdObj);
                    } catch (NumberFormatException e) {
                        throw new IllegalStateException("Invalid Account ID format");
                    }
                }
            } else {
                throw new IllegalStateException("Account ID is null");
            }

            if (accountId != null) {
                if ("Edit".equals(label)) {
                    Account account = accountController.getAccountById(accountId);
                    accountsView.openAddAccountForm(account);
                } else if ("Delete".equals(label)) {
                    accountController.deleteAccount(accountId);
                }
            }
        }
        isPushed = false;
        return label;
    }
}
