package com.finance.treasuretracker.view.tabs.transactions;

import com.finance.treasuretracker.controller.AccountController;
import com.finance.treasuretracker.controller.BankRecordController;
import com.finance.treasuretracker.controller.TransactionController;
import com.finance.treasuretracker.model.BankRecord;
import com.finance.treasuretracker.model.Transaction;
import com.finance.treasuretracker.model.dto.TransactionGridInterface;
import com.finance.treasuretracker.utils.CurrencyCellRenderer;
import com.finance.treasuretracker.utils.DataReloadListener;
import com.finance.treasuretracker.view.tabs.transactions.callRenderers.BelowThresholdRedHighlighter;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.text.NumberFormat;

public class TransactionsPanel extends JPanel implements DataReloadListener {
    private final JTable transactionsTable;
    private final DefaultTableModel tableModel;
    private final TransactionController transactionController;
    private final List<String> columnNamesList;
    private final List<String> balanceColumnNames;
    private final Map<String, Double> accountBalances;

    private List<BankRecord> bankRecordList;

    private boolean showPaidTransactions = false; // Flag to toggle paid transactions
    private BankRecordController bankRecordController;

    private void togglePaidTransactions() {
        showPaidTransactions = !showPaidTransactions; // Toggle the flag
        refreshTableData(transactionController.getAllTransactionsForGrid());
    }

    public TransactionsPanel(TransactionController transactionController1, BankRecordController bankRecordController, AccountController accountController) {
        this.transactionController = transactionController1;
        this.balanceColumnNames = new ArrayList<>();
        this.columnNamesList = new ArrayList<>();
        this.accountBalances = new HashMap<>();
        this.bankRecordController = bankRecordController;
        bankRecordList = bankRecordController.getCurrentAccountFunds();
        setLayout(new BorderLayout());
        // Panel for the top section
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(new BankRecordFormPanel(bankRecordController, accountController, this), BorderLayout.CENTER);

        // Toggle switch for showing paid transactions
        JCheckBox toggleSwitch = new JCheckBox("Show Paid Transactions");
        toggleSwitch.addActionListener(e -> togglePaidTransactions());
        topPanel.add(toggleSwitch, BorderLayout.EAST);

        // Add the top panel to the main panel
        add(topPanel, BorderLayout.NORTH);



        columnNamesList.add("Paid");
        columnNamesList.add("Bill Name");
        columnNamesList.add("Amount");
        columnNamesList.add("Date");
        columnNamesList.add("account");
        columnNamesList.add("transactionId");
        columnNamesList.add("Total");
        columnNamesList.add("Lowest In Account");

        // Define column names
        for (BankRecord bankRecord : bankRecordList) {
            String columnName = bankRecord.getAccount().getDisplayName() + " Balance";
            columnNamesList.add(columnName);
            balanceColumnNames.add(columnName); // Add to balance columns list
            accountBalances.put(columnName, bankRecord.getAmount()); // Initialize balance for each account
        }

// Convert the list back to an array if needed
        String[] columnNames = columnNamesList.toArray(new String[0]);

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
        refreshTableData(transactionController.getAllTransactionsForGrid());

        // Create the table
        transactionsTable = new JTable(tableModel);
        hideTransactionIdColumn();

        // Custom rendering for the checkbox
        transactionsTable.setDefaultRenderer(Boolean.class, new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JCheckBox checkBox = new JCheckBox();
                if (value != null) {
                    checkBox.setSelected((Boolean) value);
                }
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
                    refreshTableData(transactionController.getAllTransactionsForGrid());
                }
            }
        });
        transactionsTable.setDefaultRenderer(Double.class, new CurrencyCellRenderer());
// Assuming 'transactionsTable' is your JTable and 'tableModel' is the DefaultTableModel
        TableColumnModel columnModel = transactionsTable.getColumnModel();
        BelowThresholdRedHighlighter totalColumnRenderer = new BelowThresholdRedHighlighter();
        int totalColumnIndex = tableModel.findColumn("Total");
        int lowestColumnIndex = tableModel.findColumn("Lowest In Account");
        columnModel.getColumn(totalColumnIndex).setCellRenderer(totalColumnRenderer);
        columnModel.getColumn(lowestColumnIndex).setCellRenderer(totalColumnRenderer);

        // Add the table to a scroll pane (for better UI handling)
        JScrollPane scrollPane = new JScrollPane(transactionsTable);
        add(scrollPane, BorderLayout.CENTER);

        // Initially load with unpaid transactions
        refreshTableData(transactionController.getAllTransactionsForGrid());

        // Refresh the UI
        revalidate();
        repaint();


    }

    private void hideTransactionIdColumn() {
        TableColumnModel columnModel = transactionsTable.getColumnModel();
        int transactionIdColumnIndex = tableModel.findColumn("transactionId");
        // Index of the transaction ID column

        // Check if the column index is valid before removing
        columnModel.removeColumn(columnModel.getColumn(transactionIdColumnIndex));
        refreshTableData(transactionController.getAllTransactionsForGrid());

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
    public void refreshTableData(List<TransactionGridInterface> transactions) {
        updateAccountBalances(transactions);
        tableModel.setRowCount(0); // Clear existing data

        // Create a map for column name to index
        Map<String, Integer> columnIndexMap = new HashMap<>();
        for (int i = 0; i < columnNamesList.size(); i++) {
            columnIndexMap.put(columnNamesList.get(i), i);
        }
        boolean firstAccount = true;

        transactions.sort(Comparator.comparing(TransactionGridInterface::getTransactionDate));
        for (TransactionGridInterface transaction : transactions) {
            if (showPaidTransactions || !transaction.getPaid()) {
                Object[] row = new Object[columnNamesList.size()];

                // Set values for each column based on the transaction
                row[columnIndexMap.get("Paid")] = transaction.getPaid();
                row[columnIndexMap.get("Bill Name")] = transaction.getBillName();
                row[columnIndexMap.get("Amount")] = formatAsCurrency(transaction.getBillAmount());
                row[columnIndexMap.get("Date")] = transaction.getTransactionDate();
                row[columnIndexMap.get("account")] = transaction.getAccountDisplayName();
                row[columnIndexMap.get("transactionId")] = transaction.getTransactionId();
                double total = 0.0;
                // Handle dynamic balance columns
                // Assume the balance column names are stored in a list/set
                for (String balanceColumnName : balanceColumnNames) {
                    Double currentBalance = accountBalances.get(balanceColumnName);
                    if(Objects.equals(balanceColumnName, transaction.getAccountDisplayName() + " Balance")
                      && !transaction.getPaid()) {
                            currentBalance += transaction.getBillAmount(); // Assuming bill amount is subtracted from balance
                            accountBalances.put(balanceColumnName, currentBalance);
                    }
                    row[columnIndexMap.get(balanceColumnName)] = accountBalances.getOrDefault(balanceColumnName, 0.0);
                    total += accountBalances.getOrDefault(balanceColumnName, 0.0);
                }

                row[columnIndexMap.get("Total")] = total;
//                row[columnIndexMap.get("Lowest In Account")] = lowestInAccount;
                tableModel.addRow(row);
            }
        }
        // Assuming 'tableModel' is your DefaultTableModel
        double lowestInAccount = Double.MAX_VALUE; // Initialize to a very high value

// Loop through the table model in reverse
        for (int i = tableModel.getRowCount() - 1; i >= 0; i--) {
            // Assuming the 'Total' is stored in a specific column, let's say the last column
            double total = (Double) tableModel.getValueAt(i, columnIndexMap.get("Total"));

            // Update the lowest value encountered so far
            if (total < lowestInAccount) {
                lowestInAccount = total;
            }

            // Update the 'Lowest In Account' value for the current row
            // Assuming 'Lowest In Account' is in a specific column, for example, second last
            tableModel.setValueAt(lowestInAccount, i, columnIndexMap.get("Lowest In Account"));
        }
    }
    private void updateAccountBalances(List<TransactionGridInterface> transactions) {
        // Reset balances to initial values from bank records
        accountBalances.clear();
        for (BankRecord bankRecord : bankRecordList) {
            String columnName = bankRecord.getAccount().getDisplayName() + " Balance";
            accountBalances.put(columnName, bankRecord.getAmount()); // Initialize balance for each account
        }
    }

    private String formatAsCurrency(double amount) {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
        return currencyFormat.format(amount);
    }

    public void reloadData() {
        bankRecordList = bankRecordController.getCurrentAccountFunds();

        // Clear existing data
        columnNamesList.clear();
        balanceColumnNames.clear();
        accountBalances.clear();

        // Repopulate columnNamesList, balanceColumnNames, and accountBalances
        columnNamesList.addAll(Arrays.asList("Paid", "Bill Name", "Amount", "Date", "account", "transactionId", "Total", "Lowest In Account"));
        for (BankRecord bankRecord : bankRecordList) {
            String columnName = bankRecord.getAccount().getDisplayName() + " Balance";
            columnNamesList.add(columnName);
            balanceColumnNames.add(columnName);
            accountBalances.put(columnName, bankRecord.getAmount());
        }

        // Fetch new transactions data
        List<TransactionGridInterface> newTransactions = transactionController.getAllTransactionsForGrid();

        // Refresh the table with new data
        refreshTableData(newTransactions);
    }
}

