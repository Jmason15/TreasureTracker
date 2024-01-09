package com.finance.treasuretracker.view.utils;

import javax.swing.*;
import java.awt.*;

// Button editor class
public class ButtonEditor extends DefaultCellEditor {
    protected JButton button;
    private String label;
    private boolean isPushed;

    public ButtonEditor(JCheckBox checkBox) {
        super(checkBox);
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(e -> fireEditingStopped());
    }

    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        if (value == null) return null;
        label = (value == null) ? "" : value.toString();
        button.setText(label);
        isPushed = true;
        return button;
    }

    public Object getCellEditorValue() {
        if (isPushed) {
            // Implement logic for button click here
            JOptionPane.showMessageDialog(button, label + ": Functionality to be implemented");
        }
        isPushed = false;
        return label;
    }
}
