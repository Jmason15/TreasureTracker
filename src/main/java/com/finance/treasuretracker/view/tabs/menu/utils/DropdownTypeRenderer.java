package com.finance.treasuretracker.view.tabs.menu.utils;

import com.finance.treasuretracker.model.DropdownType;

import javax.swing.*;
import java.awt.*;

public class DropdownTypeRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(
            JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        if (value instanceof DropdownType) {
            DropdownType dropdownType = (DropdownType) value;
            setText(dropdownType.getTypeName());
        }

        return this;
    }
}
