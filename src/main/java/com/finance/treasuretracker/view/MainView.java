package com.finance.treasuretracker.view;

import javax.swing.*;
import java.awt.*;

import com.finance.treasuretracker.view.tabs.accounts.AccountsPanel;
import com.finance.treasuretracker.view.tabs.banks.BanksPanel;
import com.finance.treasuretracker.view.tabs.bills.BillsPanel;
import com.finance.treasuretracker.view.tabs.configuration.ConfigurationPanel;
import com.finance.treasuretracker.view.tabs.summary.SummaryPanel;
import com.finance.treasuretracker.view.tabs.transactions.TransactionsPanel;

public class MainView {
    public static void createAndShowGUI() {
        JFrame frame = new JFrame("Treasure Tracker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);

        Menu menu = new Menu();
        menu.setupMenu(frame);


        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Summary", new SummaryPanel());
        tabbedPane.addTab("Transactions", new TransactionsPanel());
        tabbedPane.addTab("Bills", new BillsPanel());
        tabbedPane.addTab("Banks",  new BanksPanel());
        tabbedPane.addTab("Accounts", new AccountsPanel());
        tabbedPane.addTab("Configure", new ConfigurationPanel());
        frame.add(tabbedPane);

        frame.pack();
        // Show the frame
        frame.setVisible(true);
    }

    private static void setupPanel(JPanel summaryPanel) {
        summaryPanel.setLayout(new BorderLayout());
        JLabel summaryLabel = new JLabel("Summary Information");
        summaryPanel.add(summaryLabel, BorderLayout.NORTH);
    }
}
