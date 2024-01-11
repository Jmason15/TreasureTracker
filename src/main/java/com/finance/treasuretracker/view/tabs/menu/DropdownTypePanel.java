package com.finance.treasuretracker.view.tabs.menu;

import com.finance.treasuretracker.controller.DropdownTypeController;
import com.finance.treasuretracker.model.DropdownType;
import com.finance.treasuretracker.view.tabs.menu.utils.DropdownTypeButtonEditor;
import com.finance.treasuretracker.view.tabs.utils.ButtonRenderer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DropdownTypePanel extends JPanel {

    private DefaultTableModel tableModel;
    private DropdownTypeController dropdownTypeController;

    public DropdownTypePanel(DropdownTypeController dropdownTypeController) {
        this.dropdownTypeController = dropdownTypeController;
        setLayout(new BorderLayout());
        initializeUI(); // Initialize UI components here, no need to create a new instance
    }

    private void initializeUI() {
        // Table model
        tableModel = new DefaultTableModel(new Object[]{"ID", "TypeName", "Edit", "Delete"}, 0);
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
        List<DropdownType> dropdownTypes = dropdownTypeController.getAllDropdownTypes();
        table.setDefaultEditor(JButton.class, new DropdownTypeButtonEditor(new JCheckBox(), tableModel, dropdownTypes, this)); // Pass the current instance

        // Scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Add button at the top
        JButton addButton = new JButton("Add Dropdown Type");
        addButton.addActionListener(e -> openAddDropdownTypeForm());
        add(addButton, BorderLayout.NORTH);
    }

    private void populateTableWithData() {
        // Retrieve the list of Dropdown Types from the controller
        List<DropdownType> dropdownTypes = dropdownTypeController.getAllDropdownTypes();

        // Clear existing data in the table
        tableModel.setRowCount(0);

        // Add each Dropdown Type as a row in the table model
        for (DropdownType dropdownType : dropdownTypes) {
            Object[] row = new Object[]{
                    dropdownType.getDropdownTypeId(),    // Assuming DropdownType has an 'id' field
                    dropdownType.getTypeName(),  // Assuming DropdownType has a 'typeName' field
                    "Edit",          // Placeholder for Edit button
                    "Delete"         // Placeholder for Delete button
            };
            tableModel.addRow(row);
        }
    }

    private void openAddDropdownTypeForm() {
        // Create a dialog
        JDialog dialog = new JDialog();
        dialog.setLayout(new BorderLayout());
        dialog.setTitle("Add New Dropdown Type");

        // Create a form panel with labels and text fields
        JPanel formPanel = new JPanel(new GridLayout(0, 2));
        JLabel typeNameLabel = new JLabel("Type Name:");
        JTextField typeNameField = new JTextField(20);
        formPanel.add(typeNameLabel);
        formPanel.add(typeNameField);

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
            String typeName = typeNameField.getText().trim();
            DropdownType dropdownType = new DropdownType();
            if (!typeName.isEmpty()) {
                dropdownType.setTypeName(typeName);
                // Call controller to save the new Dropdown Type
                dropdownTypeController.saveDropdownType(dropdownType); // Assume this method exists in your controller
                dialog.dispose();

                // Refresh the table or UI
                populateTableWithData();
            } else {
                JOptionPane.showMessageDialog(dialog, "Type name cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
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

    public void deleteDropdownType(Long dropdownTypeId) {
        int response = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this Dropdown Type?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (response == JOptionPane.YES_OPTION) {
            DropdownType toDelete = dropdownTypeController.getDropdownTypeById(dropdownTypeId);
            dropdownTypeController.deleteDropdownType(toDelete.getDropdownTypeId()); // Assume this method exists in your controller
            populateTableWithData(); // Refresh the table
        }
    }

    public void openEditDropdownTypeForm(DropdownType dropdownType) {
        // Create a dialog for editing a Dropdown Type
        JDialog dialog = new JDialog();
        dialog.setLayout(new BorderLayout());
        dialog.setTitle("Edit Dropdown Type");

        // Create a form panel with labels and text fields
        JPanel formPanel = new JPanel(new GridLayout(0, 2));
        JLabel typeNameLabel = new JLabel("Type Name:");
        JTextField typeNameField = new JTextField(20);
        typeNameField.setText(dropdownType.getTypeName());  // Pre-populate with Dropdown Type's current name
        formPanel.add(typeNameLabel);
        formPanel.add(typeNameField);

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
            String typeName = typeNameField.getText().trim();
            if (!typeName.isEmpty()) {
                dropdownType.setTypeName(typeName);
                // Call controller to update the Dropdown Type
                dropdownTypeController.saveDropdownType(dropdownType); // Assume this method exists in your controller
                dialog.dispose();

                // Refresh the table or UI
                populateTableWithData();
            } else {
                JOptionPane.showMessageDialog(dialog, "Type name cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
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
}
