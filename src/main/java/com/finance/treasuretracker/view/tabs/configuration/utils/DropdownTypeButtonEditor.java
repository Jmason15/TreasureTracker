package com.finance.treasuretracker.view.tabs.configuration.utils;

import com.finance.treasuretracker.view.tabs.configuration.DropdownTypePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DropdownTypeButtonEditor extends DefaultCellEditor {
    protected JButton button;
    private String label;
    private boolean isPushed;
    private DropdownTypePanel dropdownTypePanel; // Replace with your actual panel class

    private int editingRow;

    public DropdownTypeButtonEditor(JCheckBox checkBox, DropdownTypePanel dropdownTypePanel) {
        super(checkBox);
        this.dropdownTypePanel = dropdownTypePanel;
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
                dropdownTypePanel.editDropdownType(editingRow);
            } else if ("Delete".equals(label)) {
                dropdownTypePanel.deleteDropdownType(editingRow);
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

