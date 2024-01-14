package com.finance.treasuretracker.view.tabs.menu.utils;

import com.finance.treasuretracker.view.tabs.menu.DropdownPanel;

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
                // This method will be called when the button is clicked
                fireEditingStopped();
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        editingRow = row; // Set the row being edited
        label = (value == null) ? "" : value.toString();
        button.setText(label);
        isPushed = true;
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        if (isPushed) {
            if ("Edit".equals(label)) {
                // Call your panel's editDropdown method with the appropriate row
                dropdownPanel.editDropdown(editingRow);
            } else if ("Delete".equals(label)) {
                // Call your panel's deleteDropdown method with the appropriate row
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
