package com.finance.treasuretracker.view.tabs.transactions;

import javax.swing.*;
import java.awt.*;

public class TransactionsPanel  extends JPanel {
    public TransactionsPanel() {
        JPanel banksPanel = new JPanel();
        banksPanel.setLayout(new BorderLayout());
        JLabel summaryLabel = new JLabel("Summary");
        banksPanel.add(summaryLabel, BorderLayout.NORTH);
    }
}
