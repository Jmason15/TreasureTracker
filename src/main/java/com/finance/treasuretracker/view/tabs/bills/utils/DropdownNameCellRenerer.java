package com.finance.treasuretracker.view.tabs.bills.utils;

import com.finance.treasuretracker.model.Bank;
import com.finance.treasuretracker.model.Dropdown;

import javax.swing.*;
import java.awt.*;

public class DropdownNameCellRenerer extends DefaultListCellRenderer {
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if (value instanceof Dropdown) {
            value = ((Dropdown) value).getDisplay(); // Replace 'YourClass' with the actual class name and 'getName' with the method to get the name
        }
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        return this;
    }
}