package com.finance.treasuretracker.view.tabs.configuration;

import com.finance.treasuretracker.view.tabs.configuration.utils.DropdownButtonEditor;
import com.finance.treasuretracker.view.tabs.utils.ButtonRenderer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Vector;

public class DropdownPanel extends JPanel {
    private DefaultTableModel tableModel;
    private JTable table;

    public DropdownPanel() {
        setLayout(new BorderLayout());

        // Create the table model
        tableModel = new DefaultTableModel(new Object[]{"ID", "Display", "Value", "Edit", "Delete"}, 0) {
            @Override
            public Class<?> getColumnClass(int column) {
                return (column >= 3) ? JButton.class : Object.class;
            }
        };

        // Create the table with the table model
        table = new JTable(tableModel);
        table.setDefaultRenderer(JButton.class, new ButtonRenderer());
        table.setDefaultEditor(JButton.class, new DropdownButtonEditor(new JCheckBox(), this));

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Add a button to add a new Dropdown entry
        JButton addButton = new JButton("Add Dropdown");
        addButton.addActionListener(e -> addDropdown());
        add(addButton, BorderLayout.SOUTH);
    }

    // Method to add a new Dropdown entry to the table
    public void addDropdown() {
        // Create a new row in the table
        Vector<Object> row = new Vector<>();
        row.add(""); // ID placeholder
        row.add(""); // Display placeholder
        row.add(""); // Value placeholder
        row.add("Edit"); // Edit button placeholder
        row.add("Delete"); // Delete button placeholder

        tableModel.addRow(row);
    }

    // Method to edit a Dropdown entry
    public void editDropdown(int row) {
        // Implement the edit functionality here
    }

    // Method to delete a Dropdown entry
    public void deleteDropdown(int row) {
        // Implement the delete functionality here
    }
}

