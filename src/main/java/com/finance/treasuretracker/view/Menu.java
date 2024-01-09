package com.finance.treasuretracker.view;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Menu {
    public static void setupMenu(JFrame frame) {
        // Create the menu bar
        JMenuBar menuBar = new JMenuBar();

        // Build the first menu
        JMenu menu = new JMenu("View");
        menuBar.add(menu);

        // a group of JMenuItems
        JMenuItem menuItemSummary = new JMenuItem("Summary");
        menuItemSummary.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Logic for Summary
                System.out.println("Summary option selected");
            }
        });
        menu.add(menuItemSummary);

        JMenuItem menuItemConfigure = new JMenuItem("Configure");
        menuItemConfigure.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Logic for Configure
                System.out.println("Configure option selected");
            }
        });
        menu.add(menuItemConfigure);

        JMenuItem menuItemTransactions = new JMenuItem("Transactions");
        menuItemTransactions.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Logic for Transactions
                System.out.println("Transactions option selected");
            }
        });
        menu.add(menuItemTransactions);

        // Add the menu bar to the frame
        frame.setJMenuBar(menuBar);
    }
}
