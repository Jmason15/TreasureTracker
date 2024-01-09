package com.finance.treasuretracker.view.tabs.summary;

import javax.swing.*;
import java.awt.*;

public class SummaryPanel  extends JPanel {
    public SummaryPanel() {
        JPanel banksPanel = new JPanel();
        banksPanel.setLayout(new BorderLayout());
        JLabel summaryLabel = new JLabel("Summary");
        banksPanel.add(summaryLabel, BorderLayout.NORTH);
    }
}
