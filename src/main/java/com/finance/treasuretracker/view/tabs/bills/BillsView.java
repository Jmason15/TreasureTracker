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

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class BillsView extends JPanel {

    private DefaultTableModel tableModel;
    private final BillController billController;
    private final DropdownController dropdownController;

    private final AccountController accountController;

    public BillsView(BillController billController, DropdownController dropdownController, AccountController accountController) {
        this.billController = billController;
        this.dropdownController = dropdownController;
        this.accountController = accountController;
        setLayout(new BorderLayout());
        initializeUI();
    }

    private void initializeUI() {
        // Table model
        tableModel = new DefaultTableModel(BillColumnENUM.getColumnNames(), 0);
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

        // Populate table with data
        populateTableWithData();

        // Customize cell rendering to include buttons
        table.setDefaultRenderer(JButton.class, new ButtonRenderer());
        table.setDefaultEditor(JButton.class, new BillButtonEditor(new JCheckBox(), billController, this, table));

        // Scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
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

        // Scroll pane
        add(scrollPane, BorderLayout.CENTER);
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

    public void populateTableWithData() {
        List<Bill> bills = billController.getAllBills();
        tableModel.setRowCount(0);

        for (Bill bill : bills) {
            Map<BillColumnENUM, Object> rowData = new EnumMap<>(BillColumnENUM.class);
            rowData.put(BillColumnENUM.ID, bill.getBillId());
            rowData.put(BillColumnENUM.NAME, bill.getName());
            rowData.put(BillColumnENUM.DUE_DAY, bill.getDueDay());
            rowData.put(BillColumnENUM.FREQUENCY, bill.getFrequency().getDisplay());
            rowData.put(BillColumnENUM.ACCOUNT, (bill.getAccount() != null) ? bill.getAccount().getDisplayName() : "N/A");
            rowData.put(BillColumnENUM.BANK, (bill.getAccount() != null && bill.getAccount().getBank() != null) ? bill.getAccount().getBank().getName() : "N/A");
            rowData.put(BillColumnENUM.AMOUNT, bill.getAmount());
            rowData.put(BillColumnENUM.ALTERNATE, bill.getAlternate());
            rowData.put(BillColumnENUM.EDIT, "Edit");
            rowData.put(BillColumnENUM.DELETE, "Delete");

            tableModel.addRow(rowData.values().toArray());
        }
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
        JXDatePicker datePicker = new JXDatePicker();
        datePicker.setFormats(new SimpleDateFormat("dd/MM/yyyy"));

        JLabel dueDayLabel = new JLabel("Due Day:");
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
            datePicker.setDate(bill.getDueDay());
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
        formPanel.add(datePicker);
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
            Date dueDate = datePicker.getDate();
            Double amount = getaDouble(amountTextField);
            Double alternate = getaDouble(alternateTextField);
            ComboBoxItem<Dropdown> selectedDropdown = (ComboBoxItem<Dropdown>)  frequencyComboBox.getSelectedItem();
            ComboBoxItem<Account> accountDropdown = (ComboBoxItem<Account>)  accountComboBox.getSelectedItem();
            Long selectedFrequencyId = null;
            Dropdown selectedFrequency = null;

            Integer selectedAccountId = null;
            Account selectedAccount = null;

            if (selectedDropdown != null) {
                selectedFrequencyId = selectedDropdown.getItem().getDropdownId();
                selectedFrequency = dropdownController.getDropdownById(selectedFrequencyId);
                // Now you have the ID, you can use it as needed
                // ...
            }
            if (accountDropdown != null) {
                selectedAccountId = accountDropdown.getItem().getAccountId();
                selectedAccount = accountController.getAccountById(selectedAccountId);
                // Now you have the ID, you can use it as needed
                // ...
            }

            if (selectedFrequency != null) {
                toSaveOrUpdate.setName(name);
                toSaveOrUpdate.setDueDay(dueDate);
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

                // Refresh the table or UI
                populateTableWithData(); // You should implement this method to update the table
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
}
