package com.finance.treasuretracker.view.tabs.accounts;

import javax.swing.*;
import java.awt.*;

public class AccountsPanel  extends JPanel {
    public AccountsPanel() {
        JPanel banksPanel = new JPanel();
        banksPanel.setLayout(new BorderLayout());
        JLabel summaryLabel = new JLabel("Account Information");
        banksPanel.add(summaryLabel, BorderLayout.NORTH);
    }
}
