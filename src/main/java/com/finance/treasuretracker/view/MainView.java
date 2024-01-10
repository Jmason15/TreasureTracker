package com.finance.treasuretracker.view;

import javax.swing.*;
import java.awt.*;

import com.finance.treasuretracker.controller.BankController;
import com.finance.treasuretracker.controller.BankServiceImpl;
import com.finance.treasuretracker.controller.BankServiceInterface;
import com.finance.treasuretracker.model.repository.BankRepository;
import com.finance.treasuretracker.view.tabs.accounts.AccountsPanel;
import com.finance.treasuretracker.view.tabs.banks.BanksView;
import com.finance.treasuretracker.view.tabs.bills.BillsPanel;
import com.finance.treasuretracker.view.tabs.configuration.ConfigurationPanel;
import com.finance.treasuretracker.view.tabs.summary.SummaryPanel;
import com.finance.treasuretracker.view.tabs.transactions.TransactionsPanel;

public class MainView {
    public static void createAndShowGUI(BankController bankController) {
        JFrame frame = new JFrame("Treasure Tracker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);

        Menu menu = new Menu();
        menu.setupMenu(frame);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Summary", new SummaryPanel());
        tabbedPane.addTab("Transactions", new TransactionsPanel());
        tabbedPane.addTab("Bills", new BillsPanel());
        tabbedPane.addTab("Banks", new BanksView(bankController)); // Pass the controller here
        tabbedPane.addTab("Accounts", new AccountsPanel());
        tabbedPane.addTab("Configure", new ConfigurationPanel());
        frame.add(tabbedPane);

        frame.pack();
        frame.setVisible(true);
    }

    private static void setupPanel(JPanel summaryPanel) {
        summaryPanel.setLayout(new BorderLayout());
        JLabel summaryLabel = new JLabel("Summary Information");
        summaryPanel.add(summaryLabel, BorderLayout.NORTH);
    }
}

