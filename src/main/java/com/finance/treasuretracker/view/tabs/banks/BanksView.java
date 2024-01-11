package com.finance.treasuretracker.view.tabs.banks;

import com.finance.treasuretracker.controller.BankController;
import com.finance.treasuretracker.model.Bank;
import com.finance.treasuretracker.view.tabs.banks.utils.ButtonEditor;
import com.finance.treasuretracker.view.tabs.utils.ButtonRenderer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class BanksView extends JPanel {

    private DefaultTableModel tableModel;
    private BankController bankController;

    public BanksView(BankController bankController) {
        this.bankController = bankController;
        setLayout(new BorderLayout());
        initializeUI();
    }

    private void initializeUI() {
        // Table model
        tableModel = new DefaultTableModel(new Object[]{"ID", "Name", "Edit", "Delete"}, 0);
        JTable table = new JTable(tableModel) {
            @Override
            public Class<?> getColumnClass(int column) {
                return (column >= 2) ? JButton.class : Object.class;
            }
        };

        // Populate table with data
        populateTableWithData();

        // Customize cell rendering to include buttons
        table.setDefaultRenderer(JButton.class, new ButtonRenderer());
        table.setDefaultEditor(JButton.class, new ButtonEditor(new JCheckBox(), bankController, this, table));



        // Scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Add button at the top
        JButton addButton = new JButton("Add Bank");
        addButton.addActionListener(e -> openAddBankForm());
        add(addButton, BorderLayout.NORTH);
    }

    private void populateTableWithData() {
        // Retrieve the list of banks from the controller
        List<Bank> banks = bankController.getAllBanks();

        // Clear existing data in the table
        tableModel.setRowCount(0);

        // Add each bank as a row in the table model
        for (Bank bank : banks) {
            Object[] row = new Object[] {
                    bank.getBankId(),    // Assuming Bank has an 'id' field
                    bank.getName(),  // Assuming Bank has a 'name' field
                    "Edit",          // Placeholder for Edit button
                    "Delete"         // Placeholder for Delete button
            };
            tableModel.addRow(row);
        }
    }

    private void openAddBankForm() {
        // Create a dialog
        JDialog dialog = new JDialog();
        dialog.setLayout(new BorderLayout());
        dialog.setTitle("Add New Bank");

        // Create a form panel with labels and text fields
        JPanel formPanel = new JPanel(new GridLayout(0, 2));
        JLabel nameLabel = new JLabel("Bank Name:");
        JTextField nameField = new JTextField(20);
        formPanel.add(nameLabel);
        formPanel.add(nameField);

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
            String bankName = nameField.getText().trim();
            Bank bank = new Bank();
            if (!bankName.isEmpty()) {
                bank.setName(bankName);
                // Call controller to save the new bank
                bankController.saveBank(bank); // Assume this method exists in your controller
                dialog.dispose();

                // Refresh the table or UI
                populateTableWithData();
            } else {
                JOptionPane.showMessageDialog(dialog, "Bank name cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
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

    public void deleteBank(Long bankId) {
        int response = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this bank?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (response == JOptionPane.YES_OPTION) {
            Bank todelete = bankController.getById(bankId);
            bankController.deleteBank(todelete); // Assume this method exists in your controller
            populateTableWithData(); // Refresh the table
        }
    }

    public void openEditBankForm(Bank bank) {
        // Create a dialog for editing a bank
        JDialog dialog = new JDialog();
        dialog.setLayout(new BorderLayout());
        dialog.setTitle("Edit Bank");

        // Create a form panel with labels and text fields
        JPanel formPanel = new JPanel(new GridLayout(0, 2));
        JLabel nameLabel = new JLabel("Bank Name:");
        JTextField nameField = new JTextField(20);
        nameField.setText(bank.getName());  // Pre-populate with bank's current name
        formPanel.add(nameLabel);
        formPanel.add(nameField);

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
            String bankName = nameField.getText().trim();
            if (!bankName.isEmpty()) {
                bank.setName(bankName);
                // Call controller to update the bank
                bankController.updateBank(bank); // Assume this method exists in your controller
                dialog.dispose();

                // Refresh the table or UI
                populateTableWithData();
            } else {
                JOptionPane.showMessageDialog(dialog, "Bank name cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
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


    // Inner classes for button rendering and editing will be added here
}
