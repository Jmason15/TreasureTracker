package com.finance.treasuretracker.view.tabs.configuration;

import com.finance.treasuretracker.view.tabs.configuration.utils.DropdownTypeButtonEditor;
import com.finance.treasuretracker.view.tabs.utils.ButtonRenderer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Vector;

public class DropdownTypePanel extends JPanel {
    private DefaultTableModel tableModel;
    private JTable table;

    public DropdownTypePanel() {
        setLayout(new BorderLayout());

        // Create the table model for DropdownType
        tableModel = new DefaultTableModel(new Object[]{"ID", "TypeName", "Edit", "Delete"}, 0) {
            @Override
            public Class<?> getColumnClass(int column) {
                return (column >= 2) ? JButton.class : Object.class;
            }
        };

        // Create the table with the table model
        table = new JTable(tableModel);
        table.setDefaultRenderer(JButton.class, new ButtonRenderer());
        table.setDefaultEditor(JButton.class, new DropdownTypeButtonEditor(new JCheckBox(), this));

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Add a button to add a new DropdownType entry
        JButton addButton = new JButton("Add Dropdown Type");
        addButton.addActionListener(e -> addDropdownType());
        add(addButton, BorderLayout.SOUTH);
    }

    // Method to add a new DropdownType entry to the table
    public void addDropdownType() {
        // Create a new row in the table
        Vector<Object> row = new Vector<>();
        row.add(""); // ID placeholder
        row.add(""); // TypeName placeholder
        row.add("Edit"); // Edit button placeholder
        row.add("Delete"); // Delete button placeholder

        tableModel.addRow(row);
    }

    // Method to edit a DropdownType entry
    public void editDropdownType(int row) {
        // Implement the edit functionality here
    }

    // Method to delete a DropdownType entry
    public void deleteDropdownType(int row) {
        // Implement the delete functionality here
    }
}

