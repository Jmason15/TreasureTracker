package com.finance.treasuretracker.view.tabs.menu;

import com.finance.treasuretracker.controller.DropdownController;
import com.finance.treasuretracker.controller.DropdownTypeController;
import com.finance.treasuretracker.model.Dropdown;
import com.finance.treasuretracker.model.DropdownType;
import com.finance.treasuretracker.view.tabs.menu.utils.DropdownButtonEditor;
import com.finance.treasuretracker.view.tabs.menu.utils.DropdownTypeRenderer;
import com.finance.treasuretracker.view.tabs.utils.ButtonRenderer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;

public class DropdownPanel extends JPanel {
    private DefaultTableModel tableModel;
    private JTable table;
    private JComboBox<DropdownType> dropdownTypeComboBox;
    private DropdownTypeController dropdownTypeController;
    private DropdownController dropdownController;

    private void initializeDropdownTypeComboBox() {
        // Fetch the list of DropdownType entities
        List<DropdownType> dropdownTypes = dropdownTypeController.getAllDropdownTypes();
        DropdownType[] dropdownTypeArray = new DropdownType[dropdownTypes.size()];
        dropdownTypes.toArray(dropdownTypeArray);
        dropdownTypeComboBox = new JComboBox<>(new DefaultComboBoxModel<>(dropdownTypeArray));
    }
    public DropdownPanel(DropdownTypeController dropdownTypeController, DropdownController dropdownController) {
        this.dropdownController = dropdownController;
        this.dropdownTypeController = dropdownTypeController;
        setPreferredSize(new Dimension(800, 600));
        setLayout(new BorderLayout());

        // Create the table model
        tableModel = new DefaultTableModel(new Object[]{"ID", "Display", "Value", "Type", "Edit", "Delete"}, 0) {
            @Override
            public Class<?> getColumnClass(int column) {
                return (column >= 4) ? JButton.class : Object.class;
            }
        };

        // Create the table with the table model
        table = new JTable(tableModel);
        table.setDefaultRenderer(JButton.class, new ButtonRenderer());

// Pass the 'table' as an argument when creating the editor
        for (int row = 0; row < table.getRowCount(); row++) {
            for (int column = 0; column < table.getColumnCount(); column++) {
                if (column >= 4) { // Assuming columns with buttons are 3 and onward
                    table.setDefaultEditor(JButton.class, new DropdownButtonEditor(new JCheckBox(), this, table));
                }
            }
        }
        table.setDefaultEditor(JButton.class, new DropdownButtonEditor(new JCheckBox(), this, table));

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Add a button to add a new Dropdown entry
        JButton addButton = new JButton("Add Dropdown");
        addButton.addActionListener(e -> addDropdown());
        add(addButton, BorderLayout.SOUTH);
        populateTableWithData();
    }
    private void populateTableWithData() {
        List<Dropdown> dropdowns = dropdownController.getAllDropdowns(); // Fetch dropdowns

        // Clear existing data
        tableModel.setRowCount(0);

        // Populate table model
        for (Dropdown dropdown : dropdowns) {
            Object[] row = new Object[]{
                    dropdown.getDropdownId(),
                    dropdown.getDisplay(),
                    dropdown.getValue(), // Assuming Dropdown has a 'value' field
                    dropdown.getDropdownType().getTypeName(),
                    "Edit",
                    "Delete"
            };
            tableModel.addRow(row);
        }
    }

    public void editDropdown(Long dropdownId) {
        // Fetch the Dropdown object by ID
        Dropdown dropdownToEdit = dropdownController.getDropdownById(dropdownId);
        if (dropdownToEdit != null) {
            // Open a dialog to edit this Dropdown (similar to your addDropdown method but pre-fill the fields)
            JDialog dialog = new JDialog();
            dialog.setLayout(new BorderLayout());
            dialog.setTitle("Edit Dropdown");

            JPanel formPanel = new JPanel(new GridLayout(0, 2));
            formPanel.add(new JLabel("Display:"));
            JTextField displayField = new JTextField(20);
            displayField.setText(dropdownToEdit.getDisplay()); // Pre-fill with existing value
            formPanel.add(displayField);

            // Value field
            formPanel.add(new JLabel("Value:"));
            JTextField valueField = new JTextField(20);
            valueField.setText(dropdownToEdit.getValue() != null ? dropdownToEdit.getValue().toString() : ""); // Check for null here
            formPanel.add(valueField);

            // Initialize JComboBox here
            List<DropdownType> dropdownTypes = dropdownTypeController.getAllDropdownTypes();
            JComboBox<DropdownType> localDropdownTypeComboBox = new JComboBox<>();
            for (DropdownType type : dropdownTypes) {
                localDropdownTypeComboBox.addItem(type);
            }
            localDropdownTypeComboBox.setRenderer(new DropdownTypeRenderer());
            formPanel.add(new JLabel("Dropdown Type:"));
            localDropdownTypeComboBox.setSelectedItem(dropdownToEdit.getDropdownType()); // Pre-select existing value
            formPanel.add(localDropdownTypeComboBox);

            // Similar for other fields...

            dialog.add(formPanel, BorderLayout.CENTER);

            JPanel buttonPanel = new JPanel();
            JButton saveButton = new JButton("Save");
            saveButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Retrieve values from form fields
                    String displayValue = displayField.getText();
                    String value = valueField.getText(); // Get the updated value from the text field
                    DropdownType selectedType = (DropdownType) localDropdownTypeComboBox.getSelectedItem();

                    // Update Dropdown object with new values
                    dropdownToEdit.setDisplay(displayValue);
                    dropdownToEdit.setValue(value); // Update the value here
                    dropdownToEdit.setDropdownType(selectedType);

                    dropdownController.saveDropdown(dropdownToEdit); // Save the updated Dropdown

                    dialog.dispose();
                    populateTableWithData();
                }
            });
            buttonPanel.add(saveButton);
            dialog.add(buttonPanel, BorderLayout.SOUTH);

            dialog.pack();
            dialog.setLocationRelativeTo(null); // Center on screen
            dialog.setVisible(true);
        }
    }

    public void deleteDropdown(Long dropdownId) {
        int response = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            // Delete the Dropdown object
            dropdownController.deleteDropdown(dropdownId);
            // Refresh the table data
            populateTableWithData();
        }
    }

    // Method to add a new Dropdown entry to the table
    public void addDropdown() {
        JDialog dialog = new JDialog();
        dialog.setLayout(new BorderLayout());
        dialog.setTitle("Add New Dropdown");

        JPanel formPanel = new JPanel(new GridLayout(0, 2));
        formPanel.add(new JLabel("Display:"));
        JTextField displayField = new JTextField(20);
        formPanel.add(displayField);

        // Initialize JComboBox here
        List<DropdownType> dropdownTypes = dropdownTypeController.getAllDropdownTypes();
        JComboBox<DropdownType> localDropdownTypeComboBox = new JComboBox<>();
        for (DropdownType type : dropdownTypes) {
            localDropdownTypeComboBox.addItem(type);
        }
        localDropdownTypeComboBox.setRenderer(new DropdownTypeRenderer());
        formPanel.add(new JLabel("Dropdown Type:"));
        formPanel.add(localDropdownTypeComboBox);

        // Similar for other fields...

        dialog.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Retrieve values from form fields
                String displayValue = displayField.getText();
                DropdownType selectedType = (DropdownType) localDropdownTypeComboBox.getSelectedItem();

                // Create or update Dropdown object
                Dropdown newDropdown = new Dropdown();
                newDropdown.setDisplay(displayValue);
                newDropdown.setDropdownType(selectedType);
                dropdownController.saveDropdown(newDropdown);

                // TODO: Add other necessary fields and checks

                // Save the Dropdown object - call the method in your controller or service
                // dropdownController.saveDropdown(newDropdown); // Replace with actual call

                // Close the dialog after saving
                dialog.dispose();

                // Optionally, refresh the list or table showing dropdowns
            }
        });
        buttonPanel.add(saveButton);
        // Add action listener to save button...
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.pack();
        dialog.setLocationRelativeTo(null); // Center on screen
        dialog.setVisible(true);
    }

    // Method to edit a Dropdown entry

}

