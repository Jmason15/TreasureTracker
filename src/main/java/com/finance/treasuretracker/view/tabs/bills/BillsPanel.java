package com.finance.treasuretracker.view.tabs.bills;

import javax.swing.*;
import java.awt.*;

public class BillsPanel extends JPanel {
    public BillsPanel() {
        JPanel banksPanel = new JPanel();
        banksPanel.setLayout(new BorderLayout());
        JLabel summaryLabel = new JLabel("Bills Information");
        banksPanel.add(summaryLabel, BorderLayout.NORTH);
    }
}
