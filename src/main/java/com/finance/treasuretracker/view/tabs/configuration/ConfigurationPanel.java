package com.finance.treasuretracker.view.tabs.configuration;

import com.finance.treasuretracker.view.tabs.utils.ButtonRenderer;
import com.finance.treasuretracker.view.tabs.configuration.DropdownPanel;
import com.finance.treasuretracker.view.tabs.configuration.DropdownTypePanel;

import javax.swing.*;
import java.awt.*;

public class ConfigurationPanel extends JPanel {
    private CardLayout cardLayout;
    private JPanel cards;
    private DropdownPanel dropdownPanel;
    private DropdownTypePanel dropdownTypePanel;

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
        dropdownTypeButton.addActionListener(e -> cardLayout.show(cards, "DropdownType"));

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
        cardLayout = (CardLayout) (cards.getLayout());

        // Create DropdownPanel and DropdownTypePanel
        dropdownPanel = new DropdownPanel();
        dropdownTypePanel = new DropdownTypePanel();

        // Add DropdownPanel and DropdownTypePanel to the cards
        cards.add(dropdownPanel, "Dropdown");
        cards.add(dropdownTypePanel, "DropdownType");

        add(cards, BorderLayout.CENTER);
    }
}

