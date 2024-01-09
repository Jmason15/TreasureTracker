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

        JButton dropdownButton = new JButton("Dropdown");
        JButton dropdownTypeButton = new JButton("Dropdown Type");

        Dimension buttonSize = new Dimension(200, 30); // Example size, adjust as needed
        sizeButtons(dropdownButton, buttonSize);
        sizeButtons(dropdownTypeButton, buttonSize);

        // Add action listeners
        dropdownButton.addActionListener(e -> cardLayout.show(cards, "Dropdown"));
        dropdownTypeButton.addActionListener(e -> cardLayout.show(cards, "Dropdown Type"));

        sidePanel.add(dropdownButton);
        sidePanel.add(dropdownTypeButton);

        add(sidePanel, BorderLayout.WEST);
    }

    private static void sizeButtons(JButton accountButton, Dimension buttonSize) {
        accountButton.setPreferredSize(buttonSize);
        accountButton.setMinimumSize(buttonSize);
        accountButton.setMaximumSize(buttonSize);
    }

    private void initializeCards() {
        cards = new JPanel(new CardLayout());
        cardLayout = (CardLayout)(cards.getLayout());

        JPanel dropdownPanel = new JPanel(); // Add components for account management
        JPanel dropdownTypePanel = new JPanel();    // Add components for bank management

        cards.add(dropdownPanel, "Account");
        cards.add(dropdownTypePanel, "Bank");

        add(cards, BorderLayout.CENTER);
    }
}

