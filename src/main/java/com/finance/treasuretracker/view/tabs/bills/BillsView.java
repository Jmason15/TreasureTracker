package com.finance.treasuretracker.view.tabs.bills;

import javax.swing.*;
import java.awt.*;

import com.finance.treasuretracker.controller.BillController;
import com.finance.treasuretracker.controller.DropdownController;
import com.finance.treasuretracker.model.Bill;
import com.finance.treasuretracker.model.Dropdown;
import com.finance.treasuretracker.view.tabs.bills.utils.BillButtonEditor;
import com.finance.treasuretracker.view.tabs.utils.ButtonRenderer;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class BillsView extends JPanel {

    private DefaultTableModel tableModel;
    private BillController billController;
    private DropdownController dropdownController;

    public BillsView(BillController billController, DropdownController dropdownController) {
        this.billController = billController;
        this.dropdownController = dropdownController;
        setLayout(new BorderLayout());
        initializeUI();
    }

    private void initializeUI() {
        // Table model
        tableModel = new DefaultTableModel(new Object[]{"ID", "Name", "Due Day", "Amount", "Alternate", "Edit", "Delete"}, 0);
        JTable table = new JTable(tableModel) {
            @Override
            public Class<?> getColumnClass(int column) {
                return (column >= 5) ? JButton.class : Object.class;
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

        // Add button at the top
        JButton addButton = new JButton("Add Bill");
        addButton.addActionListener(e -> openAddBillForm(null));
        add(addButton, BorderLayout.NORTH);
    }

    private void populateTableWithData() {
        // Retrieve the list of bills from the controller
        List<Bill> bills = billController.getAllBills();

        // Clear existing data in the table
        tableModel.setRowCount(0);

        // Add each bill as a row in the table model
        for (Bill bill : bills) {
            Object[] row = new Object[]{
                    bill.getBillId(),
                    bill.getName(),
                    bill.getDueDay(),
                    bill.getAmount(),
                    bill.getAlternate(),
                    "Edit",
                    "Delete"
            };
            tableModel.addRow(row);
        }
    }

    public void openAddBillForm(Bill bill) {
        Bill toSaveOrUpdate = (bill != null) ? bill : new Bill(); // Create a new bill if bill is null

        // Create a dialog
        JDialog dialog = new JDialog();
        dialog.setLayout(new BorderLayout());
        dialog.setTitle("Add/Edit Bill");

        // Create a form panel with labels and text fields
        JPanel formPanel = new JPanel(new GridLayout(0, 2));
        JLabel nameLabel = new JLabel("Name:");
        JTextField nameTextField = new JTextField();
        JLabel dueDayLabel = new JLabel("Due Day:");
        JTextField dueDayTextField = new JTextField();
        JLabel amountLabel = new JLabel("Amount:");
        JTextField amountTextField = new JTextField();
        JLabel alternateLabel = new JLabel("Alternate Amount:");
        JTextField alternateTextField = new JTextField();

        // Populate a JComboBox with available frequencies (you need to fetch the list of frequencies from the controller)
        JLabel frequencyLabel = new JLabel("Frequency:");
        JComboBox<String> frequencyComboBox = new JComboBox<>();
        // Fetch the list of frequencies from the controller and add them to the combo box
        List<Dropdown> frequencies = dropdownController.getAllDropdowns();
        for (Dropdown frequency : frequencies) {
            frequencyComboBox.addItem(frequency.getDisplay());
        }

        formPanel.add(nameLabel);
        formPanel.add(nameTextField);
        formPanel.add(dueDayLabel);
        formPanel.add(dueDayTextField);
        formPanel.add(amountLabel);
        formPanel.add(amountTextField);
        formPanel.add(alternateLabel);
        formPanel.add(alternateTextField);
        formPanel.add(frequencyLabel);
        formPanel.add(frequencyComboBox);

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
            Integer dueDay = Integer.parseInt(dueDayTextField.getText());
            Double amount = Double.parseDouble(amountTextField.getText());
            Double alternate = Double.parseDouble(alternateTextField.getText());
            Long selectedFrequencyId = (Long) frequencyComboBox.getSelectedItem();

            // Fetch the selected frequency based on its ID from the controller
            Dropdown selectedFrequency = dropdownController.getDropdownById(selectedFrequencyId);

            if (selectedFrequency != null) {
                toSaveOrUpdate.setName(name);
                toSaveOrUpdate.setDueDay(dueDay);
                toSaveOrUpdate.setAmount(amount);
                toSaveOrUpdate.setAlternate(alternate);
                toSaveOrUpdate.setFrequency(selectedFrequency);

                if (bill == null) {
                    // If the bill is null, it's a new bill, so save it
                    // You can call a method from your BillController to save the bill
                    // Example: billController.createBill(toSaveOrUpdate);
                } else {
                    // If the bill is not null, it's an existing bill, so update it
                    // You can call a method from your BillController to update the bill
                    // Example: billController.updateBill(bill.getBillId(), toSaveOrUpdate);
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


    // Create a custom cell renderer for the Edit and Delete buttons
    class BillButtonRenderer extends DefaultTableCellRenderer {
        private JButton editButton;
        private JButton deleteButton;

        public BillButtonRenderer() {
            editButton = new JButton("Edit");
            deleteButton = new JButton("Delete");
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));

            if ("Edit".equals(value)) {
                panel.add(editButton);
            } else if ("Delete".equals(value)) {
                panel.add(deleteButton);
            }

            return panel;
        }
    }

    // Create a custom cell editor for the Edit and Delete buttons
    class BillButtonEditor extends DefaultCellEditor {
        private JButton editButton;
        private JButton deleteButton;
        private BillController billController;
        private BillsView billsView; // Assuming this is the name of your view class

        private JTable table;

        public BillButtonEditor(JCheckBox checkBox, BillController billController, BillsView billsView, JTable table) {
            super(checkBox);
            this.billController = billController;
            this.billsView = billsView;
            this.table = table;
            editButton = new JButton("Edit");
            deleteButton = new JButton("Delete");
            editButton.addActionListener(e -> fireEditingStopped());
            deleteButton.addActionListener(e -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            if ("Edit".equals(value)) {
                int modelRow = table.convertRowIndexToModel(row);
                Object billIdObj = table.getModel().getValueAt(modelRow, 0); // Retrieve the ID object

                Long billId;
                if (billIdObj instanceof Integer) {
                    billId = (Long) billIdObj; // Cast to Integer
                } else {
                    throw new IllegalStateException("Bill ID is not of a recognized type");
                }

                // Fetch the bill by ID from your BillController
                Bill bill = billController.getBillById(billId);
                if (bill != null) {
                    billsView.openAddBillForm(bill); // Open the form with the selected bill for editing
                }
            } else if ("Delete".equals(value)) {
                int modelRow = table.convertRowIndexToModel(row);
                Object billIdObj = table.getModel().getValueAt(modelRow, 0); // Retrieve the ID object

                Long billId;
                if (billIdObj instanceof Integer) {
                    billId = (Long) billIdObj; // Cast to Integer
                } else {
                    throw new IllegalStateException("Bill ID is not of a recognized type");
                }

                // Delete the bill by ID from your BillController
                billController.deleteBill(billId);
                billsView.populateTableWithData(); // Refresh the table
            }

            return null;
        }
    }
}
