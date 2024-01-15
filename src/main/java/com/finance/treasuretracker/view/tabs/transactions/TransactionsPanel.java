package com.finance.treasuretracker.view.tabs.transactions;

import com.finance.treasuretracker.controller.AccountController;
import com.finance.treasuretracker.controller.BankRecordController;
import com.finance.treasuretracker.controller.TransactionController;
import com.finance.treasuretracker.model.Transaction;
import com.finance.treasuretracker.model.Bill;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.List;

public class TransactionsPanel extends JPanel {
    private final JTable transactionsTable;
    private final DefaultTableModel tableModel;

    public TransactionsPanel(TransactionController transactionController, BankRecordController bankRecordController, AccountController accountController) {
        setLayout(new BorderLayout());
        add(new BankRecordFormPanel(bankRecordController, accountController), BorderLayout.NORTH);
        List<Transaction> transactions = transactionController.getAllTransactions();
        // Define column names
        String[] columnNames = {"Paid", "Bill Name", "Amount", "Date"};

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
        for (Transaction transaction : transactions) {
            Bill bill = transaction.getBill();
            Object[] row = new Object[]{
                    transaction.getPaid(),
                    bill.getName(),
                    bill.getAmount(),
                    transaction.getDate()
            };
            tableModel.addRow(row);
        }

        // Create the table
        transactionsTable = new JTable(tableModel);

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
            if (e.getColumn() == 0) {
                int row = e.getFirstRow();
                Boolean paid = (Boolean) transactionsTable.getValueAt(row, 0);
                if (paid) {
                    //hide the row (you can remove it from the model or implement a filtering mechanism)
                }
            }
        });
        // Add the table to a scroll pane (for better UI handling)
        JScrollPane scrollPane = new JScrollPane(transactionsTable);
        add(scrollPane, BorderLayout.CENTER);


    }

    // Method to refresh the table data (if needed)
    public void refreshTableData(List<Transaction> newTransactions) {
        // Clear existing data
        tableModel.setRowCount(0);

        // Add new data
        for (Transaction transaction : newTransactions) {
            Bill bill = transaction.getBill();
            Object[] row = {
                    transaction.getPaid(),
                    bill.getName(),
                    bill.getAmount(),
                    transaction.getDate()
            };
            tableModel.addRow(row);
        }
    }
}

