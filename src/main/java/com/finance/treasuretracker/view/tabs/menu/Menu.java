package com.finance.treasuretracker.view.tabs.menu;

import com.finance.treasuretracker.controller.DropdownTypeController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Menu {
    static DropdownTypeController dropdownTypeController;
    public void setupMenu(JFrame frame, DropdownTypeController dropdownTypeController) {
        // Create the menu bar
        this.dropdownTypeController = dropdownTypeController;
        JMenuBar menuBar = new JMenuBar();

        // Build the first menu
        JMenu menu = new JMenu("Configuration");
        menuBar.add(menu);

        // a group of JMenuItems
        JMenuItem menuItemDropdowns = new JMenuItem("Edit Dropdowns");
        menuItemDropdowns.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open a dialog or frame with the DropdownPanel
                showDropdownsDialog();
            }
        });
        menu.add(menuItemDropdowns);

        JMenuItem menuItemDropDownTypes = new JMenuItem("Edit Dropdown Types");
        menuItemDropDownTypes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open a dialog or frame with the DropdownTypePanel
                showDropdownTypesDialog();
            }
        });
        menu.add(menuItemDropDownTypes);

        // Add the menu bar to the frame
        frame.setJMenuBar(menuBar);
    }

    // Method to show a dialog or frame with the DropdownPanel
    private static void showDropdownsDialog() {
        JDialog dialog = new JDialog();
        dialog.setTitle("Edit Dropdowns");

        // Create and add the DropdownPanel to the dialog
        DropdownPanel dropdownPanel = new DropdownPanel();
        dialog.add(dropdownPanel);

        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    // Method to show a dialog or frame with the DropdownTypePanel
    private static void showDropdownTypesDialog() {
        JDialog dialog = new JDialog();
        dialog.setTitle("Edit Dropdown Types");

        // Create and add the DropdownTypePanel to the dialog
        DropdownTypePanel dropdownTypePanel = new DropdownTypePanel(dropdownTypeController);
        dialog.add(dropdownTypePanel);

        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
}
