package com.finance.treasuretracker.view.tabs.accounts;

import com.finance.treasuretracker.controller.BankController;
import com.finance.treasuretracker.controller.DropdownController;
import com.finance.treasuretracker.controller.DropdownTypeController;
import com.finance.treasuretracker.model.Bank;
import com.finance.treasuretracker.model.Dropdown;
import com.finance.treasuretracker.view.tabs.bills.utils.BankNameCellRenderer;
import com.finance.treasuretracker.view.tabs.bills.utils.DropdownNameCellRenerer;
import com.finance.treasuretracker.view.tabs.utils.ButtonRenderer;

import com.finance.treasuretracker.controller.AccountController;
import com.finance.treasuretracker.model.Account;
import com.finance.treasuretracker.view.tabs.accounts.utils.AccountButtonEditor;
import com.finance.treasuretracker.view.tabs.utils.ComboBoxItem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AccountsView extends JPanel {

    private DefaultTableModel tableModel;
    private AccountController accountController;

    private BankController bankController;
    private DropdownController dropdownController;

    public AccountsView(AccountController accountController, BankController bankController, DropdownController dropdownController, DropdownTypeController dropdownTypeController) {
        this.accountController = accountController;
        this.bankController = bankController;
        this.dropdownController = dropdownController;
        setLayout(new BorderLayout());
        initializeUI();
    }

    private void initializeUI() {
        // Table model
        tableModel = new DefaultTableModel(new Object[]{"ID", "Bank", "Type", "Credit", "Display Name", "Edit", "Delete"}, 0);
        JTable table = new JTable(tableModel) {
            @Override
            public Class<?> getColumnClass(int column) {
                if (column == 2) { // The "Type" column
                    return Object.class; // Return Object class to display plain text
                }
                if (column == 4) { // The "Display Name" column
                    return Object.class; // Return Object class to display plain text
                }
                return (column >= 5) ? JButton.class : Object.class;
            }
        };

        // Populate table with data
        populateTableWithData();

        // Customize cell rendering to include buttons
        table.setDefaultRenderer(JButton.class, new ButtonRenderer());
        table.setDefaultEditor(JButton.class, new AccountButtonEditor(new JCheckBox(), accountController, this, table));

        // Scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Add button at the top
        JButton addButton = new JButton("Add Account");
        addButton.addActionListener(e -> openAddAccountForm(null));
        add(addButton, BorderLayout.NORTH);
    }

    private void populateTableWithData() {
        // Retrieve the list of accounts from the controller
        List<Account> accounts = accountController.getAllAccounts();

        // Clear existing data in the table
        tableModel.setRowCount(0);

        // Add each account as a row in the table model
        for (Account account : accounts) {
            Object[] row = new Object[]{
                    account.getAccountId(),
                    account.getBank().getName(),
                    account.getType().getDisplay(),
                    account.getCredit(),
                    account.getDisplayName(), // Add the display name here
                    "Edit",
                    "Delete"
            };
            tableModel.addRow(row);
        }
    }

    public void openAddAccountForm(Account account) {
        Account toSaveOrUpdate = (account != null) ? account : new Account(); // Create a new account if account is null

        // Create a dialog
        JDialog dialog = new JDialog();
        dialog.setLayout(new BorderLayout());
        dialog.setTitle("Add New Account");

        // Create a form panel with labels and text fields
        JPanel formPanel = new JPanel(new GridLayout(0, 2));
        JLabel bankLabel = new JLabel("Bank:");
        // Populate a JComboBox with available banks (you need to fetch the list of banks from the controller)
        JComboBox<ComboBoxItem<Bank>> bankComboBox = new JComboBox<>();
        bankComboBox.setRenderer(new BankNameCellRenderer());
        // Fetch the list of banks from the controller and add them to the combo box
        List<Bank> banks = bankController.getAllBanks();
        for (Bank bank : banks) {
            bankComboBox.addItem(new ComboBoxItem<>(bank, bank.getName()));
        }

        JLabel typeLabel = new JLabel("Type:");
        // Populate a JComboBox with available types (you need to fetch the list of types from the controller)
        JComboBox<ComboBoxItem<Dropdown>> typeComboBox = new JComboBox<>();
        typeComboBox.setRenderer(new DropdownNameCellRenerer());
        // Fetch the list of types from the controller and add them to the combo box
        List<Dropdown> types = dropdownController.getAllDropdownsbyType(1L);
        for (Dropdown type : types) {
            typeComboBox.addItem(new ComboBoxItem<>(type, type.getDisplay()));
        }

        JLabel displayNameLabel = new JLabel("Display Name:");
        JTextField displayNameField = new JTextField();

        JCheckBox creditCheckBox = new JCheckBox("Credit");

        formPanel.add(bankLabel);
        formPanel.add(bankComboBox);
        formPanel.add(typeLabel);
        formPanel.add(typeComboBox);
        formPanel.add(displayNameLabel);
        formPanel.add(displayNameField);
        formPanel.add(creditCheckBox);

        dialog.add(formPanel, BorderLayout.CENTER);

        // Create a panel for the buttons
        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        dialog.add(buttonPanel, BorderLayout.SOUTH);

        // Save button action
        saveButton.addActionListener(e -> {
            ComboBoxItem<Bank> selectedBankItem = (ComboBoxItem<Bank>) bankComboBox.getSelectedItem();
            ComboBoxItem<Dropdown> selectedTypeItem = (ComboBoxItem<Dropdown>) typeComboBox.getSelectedItem();
            String displayName = displayNameField.getText(); // Get the display name input from the user

            if (selectedBankItem != null && selectedTypeItem != null) {
                Bank selectedBank = selectedBankItem.getItem();
                Dropdown selectedType = selectedTypeItem.getItem();
                Boolean isCredit = creditCheckBox.isSelected();

                toSaveOrUpdate.setBank(selectedBank);
                toSaveOrUpdate.setType(selectedType);
                toSaveOrUpdate.setCredit(isCredit);
                toSaveOrUpdate.setDisplayName(displayName); // Set the display name in the account

                if (account == null) {
                    // If the account is null, it's a new account, so save it
                    accountController.createAccount(toSaveOrUpdate);
                } else {
                    // If the account is not null, it's an existing account, so update it
                    accountController.saveAccount(toSaveOrUpdate);
                }

                dialog.dispose();

                // Refresh the table or UI
                populateTableWithData();
            } else {
                JOptionPane.showMessageDialog(dialog, "Invalid bank or type selected.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Cancel button action
        cancelButton.addActionListener(e -> dialog.dispose());

        // Set dialog properties
        dialog.pack();
        dialog.setLocationRelativeTo(null); // Center on screen
        dialog.setModal(true); // Block other windows until this dialog is closed
        dialog.setVisible(true);
    }


    // Implement methods to delete and edit accounts similar to BanksView

}

