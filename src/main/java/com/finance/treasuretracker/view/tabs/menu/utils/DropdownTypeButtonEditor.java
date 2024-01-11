package com.finance.treasuretracker.view.tabs.menu.utils;

import com.finance.treasuretracker.controller.DropdownTypeController;
import com.finance.treasuretracker.model.DropdownType;
import com.finance.treasuretracker.view.tabs.menu.DropdownTypePanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class DropdownTypeButtonEditor extends DefaultCellEditor {
    protected JButton button;
    private String label;
    private boolean isPushed;
    private DropdownTypePanel dropdownTypePanel;
    private DefaultTableModel tableModel;
    private List<DropdownType> dropdownTypes; // Add this variable

    private int editingRow;

    public DropdownTypeButtonEditor(JCheckBox checkBox, DefaultTableModel tableModel, List<DropdownType> dropdownTypes, DropdownTypePanel dropdownTypePanel) {
        super(checkBox);
        this.tableModel = tableModel;
        this.dropdownTypes = dropdownTypes; // Initialize the dropdownTypes variable
        this.dropdownTypePanel = dropdownTypePanel;
        button = new JButton();
        button.setOpaque(true);

        // Add action listener to the button
        button.addActionListener(e -> fireEditingStopped());
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
                // Get the DropdownType associated with the selected row
                DropdownType selectedDropdownType = dropdownTypes.get(editingRow);
                dropdownTypePanel.openEditDropdownTypeForm(selectedDropdownType);
            } else if ("Delete".equals(label)) {
                // Get the DropdownType associated with the selected row
                DropdownType selectedDropdownType = dropdownTypes.get(editingRow);
                dropdownTypePanel.deleteDropdownType(selectedDropdownType.getDropdownTypeId());
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
