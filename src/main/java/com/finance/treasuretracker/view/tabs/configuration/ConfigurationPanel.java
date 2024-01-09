package com.finance.treasuretracker.view.tabs.configuration;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ConfigurationPanel extends JPanel {
    private CardLayout cardLayout;
    private JPanel cards;

    public ConfigurationPanel() {
        setLayout(new BorderLayout());
        initializeSidePanel();
        initializeCards();
    }

    private void initializeSidePanel() {
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));

        JButton accountButton = new JButton("Account");
        JButton bankButton = new JButton("Bank");
        JButton billButton = new JButton("Bill");

        JPanel buttonPanel = new JPanel(new GridLayout(0, 1)); // 0 rows, 1 column
        buttonPanel.add(accountButton);
        buttonPanel.add(bankButton);
        buttonPanel.add(billButton);
        // ... add buttons for Dropdown and DropdownType

        // Add action listeners
        accountButton.addActionListener(e -> cardLayout.show(cards, "Account"));
        bankButton.addActionListener(e -> cardLayout.show(cards, "Bank"));
        billButton.addActionListener(e -> cardLayout.show(cards, "Bill"));
        // ... similar for Dropdown and DropdownType

        sidePanel.add(buttonPanel);

        // ... add the rest of the buttons

        add(sidePanel, BorderLayout.WEST);
    }

    private void initializeCards() {
        cards = new JPanel(new CardLayout());
        cardLayout = (CardLayout)(cards.getLayout());

        JPanel accountPanel = new JPanel(); // Add components for account management
        JPanel bankPanel = new JPanel();    // Add components for bank management
        JPanel billPanel = new JPanel();    // Add components for bill management
        // ... create panels for Dropdown and DropdownType

        cards.add(accountPanel, "Account");
        cards.add(bankPanel, "Bank");
        cards.add(billPanel, "Bill");
        // ... add the rest of the panels

        add(cards, BorderLayout.CENTER);
    }
}

