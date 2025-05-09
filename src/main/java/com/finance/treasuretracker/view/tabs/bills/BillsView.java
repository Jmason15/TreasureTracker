package com.finance.treasuretracker.view.tabs.bills;

import javax.swing.*;
import java.awt.*;

import com.finance.treasuretracker.controller.AccountController;
import com.finance.treasuretracker.controller.BillController;
import com.finance.treasuretracker.controller.DropdownController;
import com.finance.treasuretracker.model.Account;
import com.finance.treasuretracker.model.Bill;
import com.finance.treasuretracker.model.Dropdown;
import com.finance.treasuretracker.view.tabs.bills.enums.BillColumnENUM;
import com.finance.treasuretracker.view.tabs.bills.utils.BillButtonEditor;
import com.finance.treasuretracker.view.tabs.utils.ButtonRenderer;
import com.finance.treasuretracker.view.tabs.utils.ComboBoxItem;
import org.jdesktop.swingx.JXDatePicker;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class BillsView extends JPanel {

    private DefaultTableModel recurringBillsTableModel;
    private DefaultTableModel oneTimeBillsTableModel;
    private DefaultTableModel recurringIncomeTableModel;
    private DefaultTableModel oneTimeIncomeTableModel;
    private final BillController billController;
    private final DropdownController dropdownController;

    private JTable recurringBillsTable;
    private JTable oneTimeBillsTable;
    private JTable recurringIncomeTable;
    private JTable oneTimeIncomeTable;

    private final AccountController accountController;

    public BillsView(BillController billController, DropdownController dropdownController, AccountController accountController) {
        this.billController = billController;
        this.dropdownController = dropdownController;
        this.accountController = accountController;
        setLayout(new BorderLayout());
        initializeUI();
    }

    private void initializeUI() {
        // Table models
        recurringBillsTableModel = new DefaultTableModel(BillColumnENUM.getColumnNames(), 0);
        oneTimeBillsTableModel = new DefaultTableModel(BillColumnENUM.getColumnNames(), 0);
        recurringIncomeTableModel = new DefaultTableModel(BillColumnENUM.getColumnNames(), 0);
        oneTimeIncomeTableModel = new DefaultTableModel(BillColumnENUM.getColumnNames(), 0);

        recurringBillsTable = createTable(recurringBillsTableModel);
        oneTimeBillsTable = createTable(oneTimeBillsTableModel);
        recurringIncomeTable = createTable(recurringIncomeTableModel);
        oneTimeIncomeTable = createTable(oneTimeIncomeTableModel);

        // Hide the ID column in all tables
        hideIdColumn(recurringBillsTable);
        hideIdColumn(oneTimeBillsTable);
        hideIdColumn(recurringIncomeTable);
        hideIdColumn(oneTimeIncomeTable);

        // Populate tables with data
        populateTablesWithData();

        // Create tabbed pane
        JTabbedPane tabbedPane = new JTabbedPane();

        // Add tables to tabbed pane
        tabbedPane.addTab("Recurring Bills", new JScrollPane(recurringBillsTable));
        tabbedPane.addTab("One-Time Bills", new JScrollPane(oneTimeBillsTable));
        tabbedPane.addTab("Recurring Income", new JScrollPane(recurringIncomeTable));
        tabbedPane.addTab("One-Time Income", new JScrollPane(oneTimeIncomeTable));

        // Add tabbed pane to the UI
        add(tabbedPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        // Add button at the top
        JButton addButton = new JButton("Add Bill");
        addButton.setBackground(new Color(0, 128, 0)); // Green color
        addButton.setForeground(Color.WHITE); // White text
        addButton.addActionListener(e -> openAddBillForm(null));
        buttonPanel.add(addButton);

        JButton correctTransactionsButton = new JButton("Correct Transactions");
        correctTransactionsButton.setBackground(new Color(0, 0, 255)); // Blue color
        correctTransactionsButton.setForeground(Color.WHITE); // White text
        correctTransactionsButton.addActionListener(e -> showConfirmationDialog());
        buttonPanel.add(correctTransactionsButton);

        add(buttonPanel, BorderLayout.NORTH);
    }

    private JTable createTable(DefaultTableModel tableModel) {
        JTable table = new JTable(tableModel) {
            @Override
            public Class<?> getColumnClass(int column) {
                BillColumnENUM columnEnum = BillColumnENUM.values()[column];
                switch (columnEnum) {
                    case EDIT:   // If your enum has an EDIT field
                    case DELETE: // If your enum has a DELETE field
                        return JButton.class;
                    default:
                        return Object.class;
                }
            }
        };

        // Customize cell rendering to include buttons
        table.setDefaultRenderer(JButton.class, new ButtonRenderer());
        table.setDefaultEditor(JButton.class, new BillButtonEditor(new JCheckBox(), billController, this, table));

        return table;
    }

    private void showConfirmationDialog() {
        int result = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to correct transactions?",
                "Confirm Transaction Correction",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (result == JOptionPane.YES_OPTION) {
            billController.correctTransactions();
        }
        // No additional action needed for the NO_OPTION as the dialog will simply close
    }

    public void populateTablesWithData() {
        List<Bill> bills = billController.getAllBills();
        recurringBillsTableModel.setRowCount(0);
        oneTimeBillsTableModel.setRowCount(0);
        recurringIncomeTableModel.setRowCount(0);
        oneTimeIncomeTableModel.setRowCount(0);

        for (Bill bill : bills) {
            Map<BillColumnENUM, Object> rowData = new EnumMap<>(BillColumnENUM.class);
            rowData.put(BillColumnENUM.ID, bill.getBillId());
            rowData.put(BillColumnENUM.NAME, bill.getName());
            rowData.put(BillColumnENUM.DUE_DAY, bill.getDueDay());
            rowData.put(BillColumnENUM.END_DATE, bill.getEndDate());
            rowData.put(BillColumnENUM.FREQUENCY, bill.getFrequency().getDisplay());
            rowData.put(BillColumnENUM.ACCOUNT, (bill.getAccount() != null) ? bill.getAccount().getDisplayName() : "N/A");
            rowData.put(BillColumnENUM.BANK, (bill.getAccount() != null && bill.getAccount().getBank() != null) ? bill.getAccount().getBank().getName() : "N/A");
            rowData.put(BillColumnENUM.AMOUNT, bill.getAmount());
            rowData.put(BillColumnENUM.ALTERNATE, bill.getAlternate());
            rowData.put(BillColumnENUM.EDIT, "Edit");
            rowData.put(BillColumnENUM.DELETE, "Delete");

            if (bill.getAmount() < 0) {
                if (!bill.getFrequency().getValue().equals("365")) {
                    recurringBillsTableModel.addRow(rowData.values().toArray());
                } else {
                    oneTimeBillsTableModel.addRow(rowData.values().toArray());
                }
            } else {
                if (!bill.getFrequency().getValue().equals("365")) {
                    recurringIncomeTableModel.addRow(rowData.values().toArray());
                } else {
                    oneTimeIncomeTableModel.addRow(rowData.values().toArray());
                }
            }
        }

        // Hide the ID column in all tables
        hideIdColumn(recurringBillsTable);
        hideIdColumn(oneTimeBillsTable);
        hideIdColumn(recurringIncomeTable);
        hideIdColumn(oneTimeIncomeTable);
    }

    private void hideIdColumn(JTable table) {
        TableColumn idColumn = table.getColumnModel().getColumn(0);
        idColumn.setMinWidth(0);
        idColumn.setMaxWidth(0);
        idColumn.setPreferredWidth(0);
        idColumn.setResizable(false);
    }

    public void openAddBillForm(Bill bill) {
        Bill toSaveOrUpdate = (bill != null) ? bill : new Bill(); // Create a new bill if bill is null

        // Create a dialog
        JDialog dialog = new JDialog();
        dialog.setLayout(new BorderLayout());
        dialog.setTitle(bill != null ? "Edit Bill" : "Add Bill");

        // Create a form panel with labels and text fields
        JPanel formPanel = new JPanel(new GridLayout(0, 2));
        JLabel nameLabel = new JLabel("Name:");
        JTextField nameTextField = new JTextField();
        JXDatePicker datePickerStartDate = new JXDatePicker();
        JXDatePicker datePickerEndDate = new JXDatePicker();
        datePickerStartDate.setFormats(new SimpleDateFormat("dd/MM/yyyy"));
        datePickerEndDate.setFormats(new SimpleDateFormat("dd/MM/yyyy"));

        JLabel dueDayLabel = new JLabel(BillColumnENUM.DUE_DAY.getColumnName());
        JLabel endDayLabel = new JLabel(BillColumnENUM.END_DATE.getColumnName());
        JLabel amountLabel = new JLabel("Amount:");
        JTextField amountTextField = new JTextField();
        JLabel alternateLabel = new JLabel("Alternate Amount:");
        JTextField alternateTextField = new JTextField();

        // Populate a JComboBox with available frequencies (you need to fetch the list of frequencies from the controller)
        JLabel frequencyLabel = new JLabel("Frequency:");
        JComboBox<ComboBoxItem<Dropdown>> frequencyComboBox = new JComboBox<>();

        // Fetch the list of frequencies from the controller and add them to the combo box
        for (Dropdown frequency : dropdownController.getAllDropdownsbyType(2L)) {
            frequencyComboBox.addItem(new ComboBoxItem<>(frequency, frequency.getDisplay()));
        }

        JLabel accountsLabel = new JLabel("Account:");
        JComboBox<ComboBoxItem<Account>> accountComboBox = new JComboBox<>();
        for (Account account : accountController.getAllAccounts()) {
            accountComboBox.addItem(new ComboBoxItem<>(account, account.getDisplayName()));
        }

        // Populate the fields if editing
        if (bill != null) {
            nameTextField.setText(bill.getName());
            datePickerStartDate.setDate(bill.getDueDay());
            datePickerEndDate.setDate(bill.getEndDate());
            amountTextField.setText(bill.getAmount().toString());
            alternateTextField.setText(bill.getAlternate() != null ? bill.getAlternate().toString() : "");

            // Set the frequency dropdown
            for (int i = 0; i < frequencyComboBox.getItemCount(); i++) {
                ComboBoxItem<Dropdown> item = frequencyComboBox.getItemAt(i);
                if (item.getItem().equals(bill.getFrequency())) {
                    frequencyComboBox.setSelectedIndex(i);
                    break;
                }
            }

            for (int i = 0; i < accountComboBox.getItemCount(); i++) {
                ComboBoxItem<Account> item = accountComboBox.getItemAt(i);
                if (item.getItem().equals(bill.getAccount())) {
                    frequencyComboBox.setSelectedIndex(i);
                    break;
                }
            }
        }

        formPanel.add(nameLabel);
        formPanel.add(nameTextField);
        formPanel.add(dueDayLabel);
        formPanel.add(datePickerStartDate);
        formPanel.add(endDayLabel);
        formPanel.add(datePickerEndDate);
        formPanel.add(amountLabel);
        formPanel.add(amountTextField);
        formPanel.add(alternateLabel);
        formPanel.add(alternateTextField);
        formPanel.add(frequencyLabel);
        formPanel.add(frequencyComboBox);
        formPanel.add(accountsLabel);
        formPanel.add(accountComboBox);

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
            String name = nameTextField.getText();
            Date dueDate = datePickerStartDate.getDate();
            Date endDate = datePickerEndDate.getDate();
            Double amount = getaDouble(amountTextField);
            Double alternate = getaDouble(alternateTextField);
            ComboBoxItem<Dropdown> selectedDropdown = (ComboBoxItem<Dropdown>) frequencyComboBox.getSelectedItem();
            ComboBoxItem<Account> accountDropdown = (ComboBoxItem<Account>) accountComboBox.getSelectedItem();
            Long selectedFrequencyId = null;
            Dropdown selectedFrequency = null;

            Integer selectedAccountId = null;
            Account selectedAccount = null;

            if (selectedDropdown != null) {
                selectedFrequencyId = selectedDropdown.getItem().getDropdownId();
                selectedFrequency = dropdownController.getDropdownById(selectedFrequencyId);
            }
            if (accountDropdown != null) {
                selectedAccountId = accountDropdown.getItem().getAccountId();
                selectedAccount = accountController.getAccountById(selectedAccountId);
            }

            if (selectedFrequency != null) {
                toSaveOrUpdate.setName(name);
                toSaveOrUpdate.setDueDay(dueDate);
                toSaveOrUpdate.setEndDate(endDate);
                toSaveOrUpdate.setAmount(amount);
                toSaveOrUpdate.setAlternate(alternate);
                toSaveOrUpdate.setFrequency(selectedFrequency);
                toSaveOrUpdate.setAccount(selectedAccount);

                if (bill == null) {
                    billController.createBill(toSaveOrUpdate);
                } else {
                    billController.updateBill(toSaveOrUpdate);
                }

                dialog.dispose();

                // Refresh the tables
                populateTablesWithData();
            } else {
                JOptionPane.showMessageDialog(dialog, "Invalid frequency selected.", "Error", JOptionPane.ERROR_MESSAGE);
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

    private static Double getaDouble(JTextField alternateTextField) {
        String alternateText = alternateTextField.getText();
        Double alternate;
        if (alternateText != null && !alternateText.trim().isEmpty()) {
            try {
                alternate = Double.parseDouble(alternateText);
            } catch (NumberFormatException e) {
                alternate = 0.0; // Set to zero if the text is not a valid double
                // Optionally show an error message or log the exception
            }
        } else {
            alternate = 0.0; // Set to zero if the text field is null or empty
        }
        return alternate;
    }


    // Create a custom cell editor for the Edit and Delete buttons
//    class BillButtonEditor extends DefaultCellEditor {
//        private JButton editButton;
//        private JButton deleteButton;
//        private BillController billController;
//        private BillsView billsView; // Assuming this is the name of your view class
//
//        private JTable table;
//
//        public BillButtonEditor(JCheckBox checkBox, BillController billController, BillsView billsView, JTable table) {
//            super(checkBox);
//            this.billController = billController;
//            this.billsView = billsView;
//            this.table = table;
//            editButton = new JButton("Edit");
//            deleteButton = new JButton("Delete");
//            editButton.addActionListener(e -> fireEditingStopped());
//            deleteButton.addActionListener(e -> fireEditingStopped());
//        }
//
//        @Override
//        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
//            if ("Edit".equals(value)) {
//                int modelRow = table.convertRowIndexToModel(row);
//                Object billIdObj = table.getModel().getValueAt(modelRow, 0); // Retrieve the ID object
//
//                Long billId = null;
//                if (billIdObj instanceof Integer) {
//                    // If it's an Integer, convert to Long
//                    billId = ((Integer) billIdObj).longValue();
//                } else if (billIdObj instanceof Long) {
//                    // If it's already a Long, cast directly
//                    billId = (Long) billIdObj;
//                } else {
//                    // Handle other unexpected types, perhaps log a warning or error
//                    System.err.println("Unexpected type for bill ID: " + billIdObj.getClass().getName());
//                }
//
//                // Fetch the bill by ID from your BillController
//                Bill bill = billController.getBillById(billId);
//                if (bill != null) {
//                    billsView.openAddBillForm(bill); // Open the form with the selected bill for editing
//                }
//            } else if ("Delete".equals(value)) {
//                int modelRow = table.convertRowIndexToModel(row);
//                Object billIdObj = table.getModel().getValueAt(modelRow, 0); // Retrieve the ID object
//
//                Long billId;
//                if (billIdObj instanceof Long) {
//                    billId = (Long) billIdObj; // Cast to Integer
//                } else {
//                    throw new IllegalStateException("Bill ID is not of a recognized type");
//                }
//
//                // Delete the bill by ID from your BillController
//                billController.deleteBill(billId);
//                billsView.populateTableWithData(); // Refresh the table
//            }
//
//            return null;
//        }
//    }

    public JTable getRecurringBillsTable() {
        return recurringBillsTable;
    }

    public JTable getOneTimeBillsTable() {
        return oneTimeBillsTable;
    }

    public JTable getRecurringIncomeTable() {
        return recurringIncomeTable;
    }

    public JTable getOneTimeIncomeTable() {
        return oneTimeIncomeTable;
    }
}
