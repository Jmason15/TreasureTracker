package com.finance.treasuretracker.view.tabs.transactions;

import com.finance.treasuretracker.controller.AccountController;
import com.finance.treasuretracker.controller.BankRecordController;
import com.finance.treasuretracker.controller.TransactionController;
import com.finance.treasuretracker.model.Transaction;
import com.finance.treasuretracker.model.Bill;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.Date;
import java.util.List;

public class TransactionsPanel extends JPanel {
    private final JTable transactionsTable;
    private final DefaultTableModel tableModel;
    private final TransactionController transactionController;

    private boolean showPaidTransactions = false; // Flag to toggle paid transactions

    private void togglePaidTransactions() {
        showPaidTransactions = !showPaidTransactions; // Toggle the flag
        refreshTableData(transactionController.getAllTransactions());
    }

    public TransactionsPanel(TransactionController transactionController1, BankRecordController bankRecordController, AccountController accountController) {
        this.transactionController = transactionController1;
        setLayout(new BorderLayout());
        // Panel for the top section
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(new BankRecordFormPanel(bankRecordController, accountController), BorderLayout.CENTER);

        // Toggle switch for showing paid transactions
        JCheckBox toggleSwitch = new JCheckBox("Show Paid Transactions");
        toggleSwitch.addActionListener(e -> togglePaidTransactions());
        topPanel.add(toggleSwitch, BorderLayout.EAST);

        // Add the top panel to the main panel
        add(topPanel, BorderLayout.NORTH);

        // Define column names
        String[] columnNames = {"Paid", "Bill Name", "Amount", "Date", "transactionId"};

        // Create a table model
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 0 ? Boolean.class : String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0; // Only the "Paid" column is editable
            }
        };

        // Fill the table model with data
        for (Transaction transaction : transactionController.getAllTransactions()) {
            Bill bill = transaction.getBill();
            Object[] row = new Object[]{
                    transaction.getPaid(),
                    bill.getName(),
                    bill.getAmount(),
                    transaction.getDate(),
                    transaction.getTransactionId() // Add Transaction ID
            };
            tableModel.addRow(row);
        }

        // Create the table
        transactionsTable = new JTable(tableModel);
        hideTransactionIdColumn();

        // Custom rendering for the checkbox
        transactionsTable.setDefaultRenderer(Boolean.class, new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JCheckBox checkBox = new JCheckBox();
                checkBox.setSelected((Boolean) value);
                checkBox.setHorizontalAlignment(JLabel.CENTER);
                return checkBox;
            }
        });

        // Add a listener to hide rows when the checkbox is clicked
        transactionsTable.getModel().addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE && e.getColumn() == 0) {
                int row = e.getFirstRow();
                Boolean paid = (Boolean) transactionsTable.getValueAt(row, 0);
                if (paid != null) {
                    // Update and save the transaction
                    Transaction transaction = getTransactionForRow(row);
                    if (transaction != null) {
                        transaction.setPaid(paid);
                        transactionController.saveTransaction(transaction); // Assuming you have a method to save the transaction
                    }
                    // Optionally, refresh the table
                    refreshTableData(transactionController.getAllTransactions());
                }
            }
        });
        // Add the table to a scroll pane (for better UI handling)
        JScrollPane scrollPane = new JScrollPane(transactionsTable);
        add(scrollPane, BorderLayout.CENTER);

        // Initially load with unpaid transactions
        refreshTableData(transactionController.getAllTransactions());

        // Refresh the UI
        revalidate();
        repaint();


    }

    private void hideTransactionIdColumn() {
        TableColumnModel columnModel = transactionsTable.getColumnModel();
        int transactionIdColumnIndex = 4; // Index of the transaction ID column

        // Check if the column index is valid before removing
        if (transactionIdColumnIndex < columnModel.getColumnCount()) {
            columnModel.removeColumn(columnModel.getColumn(transactionIdColumnIndex));
            refreshTableData(transactionController.getAllTransactions());
        } else {
            // Log or handle the case where the column index is invalid
            System.err.println("Invalid column index: " + transactionIdColumnIndex);
        }
    }

    private Transaction getTransactionForRow(int row) {
        // Retrieve the transaction ID. Assuming it's in the last column of the table model
        int idColumnName = tableModel.findColumn("transactionId");
        Object transactionIdObj = tableModel.getValueAt(row, idColumnName);

        if (transactionIdObj != null) {
            try {
                Long transactionId = ((Integer) transactionIdObj).longValue();
                return transactionController.getTransactionById(transactionId);
            } catch (ClassCastException e) {
                e.printStackTrace();
                // Handle the exception (log or throw)
            }
        } else {
            // Handle the case where transactionIdObj is null
            System.err.println("Transaction ID is null for row: " + row);
        }
        return null;
    }

    // Method to refresh the table data (if needed)
    public void refreshTableData(List<Transaction> transactions) {
        tableModel.setRowCount(0); // Clear existing data

        for (Transaction transaction : transactions) {
            if (showPaidTransactions || !transaction.getPaid()) { // Check paid status based on the flag
                Bill bill = transaction.getBill();
                Object[] row = {
                        transaction.getPaid(),
                        bill.getName(),
                        bill.getAmount(),
                        transaction.getDate(),
                        transaction.getTransactionId(),
                };
                tableModel.addRow(row);
            }
        }
    }
}

