package com.finance.treasuretracker.view.tabs.banks;

import com.finance.treasuretracker.view.utils.ButtonEditor;
import com.finance.treasuretracker.view.utils.ButtonRenderer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BanksPanel extends JPanel {

    private DefaultTableModel tableModel;

    public BanksPanel() {
        setLayout(new BorderLayout());
        initializeUI();
    }

    private void initializeUI() {
        // Table model
        tableModel = new DefaultTableModel(new Object[]{"ID", "Name", "Edit", "Delete"}, 0);
        JTable table = new JTable(tableModel) {
            @Override
            public Class<?> getColumnClass(int column) {
                return (column >= 2) ? JButton.class : Object.class;
            }
        };

        // Adding sample data
        addSampleData();

        // Customize cell rendering to include buttons
        table.setDefaultRenderer(JButton.class, new ButtonRenderer());
        table.setDefaultEditor(JButton.class, new ButtonEditor(new JCheckBox()));

        // Scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Add button at the top
        JButton addButton = new JButton("Add Bank");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open the form to add a new bank
                openAddBankForm();
            }
        });
        add(addButton, BorderLayout.NORTH);
    }

    private void addSampleData() {
        // Sample data - replace with actual data from your database
        tableModel.addRow(new Object[]{"1", "Bank A", "Edit", "Delete"});
        tableModel.addRow(new Object[]{"2", "Bank B", "Edit", "Delete"});
        // ... add more rows as needed
    }

    private void openAddBankForm() {
        // Implement the logic to open a form for adding a new bank
        JOptionPane.showMessageDialog(this, "Add bank form goes here.");
    }

    // Inner classes for button rendering and editing will be added here
}
