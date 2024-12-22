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
import com.finance.treasuretracker.view.tabs.transactions.callRenderers.DueDateHighlighter;
import com.finance.treasuretracker.view.tabs.transactions.callRenderers.YearRowRenderer;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.*;
import java.util.List;
import java.text.NumberFormat;
import java.util.stream.Collectors;

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
                if (value instanceof Boolean) {
                    JCheckBox checkBox = new JCheckBox();
                    checkBox.setSelected((Boolean) value);
                    checkBox.setHorizontalAlignment(JLabel.CENTER);
                    return checkBox;
                } else {
                    return new JLabel(value != null ? value.toString() : "");
                }
            }
        });

        // Apply the custom renderer to the "Date" column
        int dateColumnIndex = tableModel.findColumn("Date");
        transactionsTable.getColumnModel().getColumn(dateColumnIndex).setCellRenderer(new DueDateHighlighter());

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

        transactionsTable.setDefaultRenderer(Object.class, new YearRowRenderer());

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

    private void refreshTableData(List<TransactionGridInterface> transactions) {
        updateAccountBalances(transactions);
        tableModel.setRowCount(0); // Clear existing data

        // Create a map for column name to index
        Map<String, Integer> columnIndexMap = new HashMap<>();
        for (int i = 0; i < columnNamesList.size(); i++) {
            columnIndexMap.put(columnNamesList.get(i), i);
        }

        // Sort transactions by date
        transactions.sort(Comparator.comparing(TransactionGridInterface::getTransactionDate));

        // Group transactions by year and month
        Map<String, List<TransactionGridInterface>> transactionsByMonth = transactions.stream()
                .collect(Collectors.groupingBy(transaction -> {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(transaction.getTransactionDate());
                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH) + 1; // Months are 0-based in Calendar
                    return year + "-" + String.format("%02d", month);
                }));

        // Sort the map entries by year and month
        List<Map.Entry<String, List<TransactionGridInterface>>> sortedEntries = new ArrayList<>(transactionsByMonth.entrySet());
        sortedEntries.sort(Comparator.comparing(entry -> {
            String[] parts = entry.getKey().split("-");
            int year = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            return LocalDate.of(year, month, 1);
        }));

        for (Map.Entry<String, List<TransactionGridInterface>> entry : sortedEntries) {
            String yearMonth = entry.getKey();
            List<TransactionGridInterface> monthlyTransactions = entry.getValue();

            // Filter out paid transactions if showPaidTransactions is false
            List<TransactionGridInterface> filteredTransactions = monthlyTransactions.stream()
                    .filter(transaction -> showPaidTransactions || !transaction.getPaid())
                    .sorted(Comparator.comparing(TransactionGridInterface::getTransactionDate)) // Sort filtered transactions by date
                    .toList();

            // Skip adding a row for the month if there are no transactions to show
            if (filteredTransactions.isEmpty()) {
                continue;
            }

            // Add a row for the month
            tableModel.addRow(new Object[]{yearMonth, "", "", "", "", "", "", ""});

            for (TransactionGridInterface transaction : filteredTransactions) {
                Object[] row = new Object[columnNamesList.size()];

                // Set values for each column based on the transaction
                row[columnIndexMap.get("Paid")] = transaction.getPaid();
                row[columnIndexMap.get("Bill Name")] = transaction.getBillName();
                row[columnIndexMap.get("Amount")] = formatAsCurrency(transaction.getBillAmount() != null ? transaction.getBillAmount() : 0.0);
                row[columnIndexMap.get("Date")] = transaction.getTransactionDate();
                row[columnIndexMap.get("account")] = transaction.getAccountDisplayName();
                row[columnIndexMap.get("transactionId")] = transaction.getTransactionId();
                double total = 0.0;

                // Handle dynamic balance columns
                for (String balanceColumnName : balanceColumnNames) {
                    Double currentBalance = accountBalances.get(balanceColumnName);
                    if (Objects.equals(balanceColumnName, transaction.getAccountDisplayName() + " Balance") && !transaction.getPaid()) {
                        currentBalance += transaction.getBillAmount() != null ? transaction.getBillAmount() : 0.0;
                        accountBalances.put(balanceColumnName, currentBalance);
                    }
                    row[columnIndexMap.get(balanceColumnName)] = formatAsCurrency(accountBalances.getOrDefault(balanceColumnName, 0.0));
                    total += accountBalances.getOrDefault(balanceColumnName, 0.0);
                }

                row[columnIndexMap.get("Total")] = formatAsCurrency(total);
                tableModel.addRow(row);
            }
        }

        double lowestInAccount = Double.MAX_VALUE;
        for (int i = tableModel.getRowCount() - 1; i >= 0; i--) {
            String totalStr = tableModel.getValueAt(i, columnIndexMap.get("Total")).toString().replaceAll("[^\\d.-]", "");
            if (!totalStr.isEmpty()) {
                double total = Double.parseDouble(totalStr);
                if (total < lowestInAccount) {
                    lowestInAccount = total;
                }
                tableModel.setValueAt(formatAsCurrency(lowestInAccount), i, columnIndexMap.get("Lowest In Account"));
            }
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
        currencyFormat.setMaximumFractionDigits(0); // Set to 0 to display whole dollar amounts
        return currencyFormat.format(Math.round(amount)); // Round the amount to the nearest integer
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

