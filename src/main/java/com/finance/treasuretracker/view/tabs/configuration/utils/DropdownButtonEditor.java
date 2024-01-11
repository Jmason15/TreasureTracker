package com.finance.treasuretracker.view.tabs.configuration.utils;

import com.finance.treasuretracker.view.tabs.configuration.DropdownPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DropdownButtonEditor extends DefaultCellEditor {
    protected JButton button;
    private String label;
    private boolean isPushed;
    private DropdownPanel dropdownPanel; // Replace with your actual panel class

    private int editingRow;

    public DropdownButtonEditor(JCheckBox checkBox, DropdownPanel dropdownPanel) {
        super(checkBox);
        this.dropdownPanel = dropdownPanel;
        button = new JButton();
        button.setOpaque(true);

        // Add action listener to the button
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fireEditingStopped();
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if (value == null) return null;

        label = value.toString();
        button.setText(label);
        isPushed = true;
        editingRow = row;
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        if (isPushed) {
            // Handle button click action here based on the label
            if ("Edit".equals(label)) {
                dropdownPanel.editDropdown(editingRow);
            } else if ("Delete".equals(label)) {
                dropdownPanel.deleteDropdown(editingRow);
            }
        }
        isPushed = false;
        return label;
    }

    @Override
    public boolean stopCellEditing() {
        isPushed = false;
        return super.stopCellEditing();
    }
}

