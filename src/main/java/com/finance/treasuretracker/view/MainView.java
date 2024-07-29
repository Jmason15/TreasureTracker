package com.finance.treasuretracker.view;

import javax.swing.*;
import java.awt.*;

import com.finance.treasuretracker.controller.*;
import com.finance.treasuretracker.view.tabs.accounts.AccountsView;
import com.finance.treasuretracker.view.tabs.banks.BanksView;
import com.finance.treasuretracker.view.tabs.bills.BillsView;
import com.finance.treasuretracker.view.tabs.menu.Menu;
import com.finance.treasuretracker.view.tabs.summary.SummaryPanel;
import com.finance.treasuretracker.view.tabs.transactions.TransactionsPanel;

public class MainView {
    public static void createAndShowGUI(AccountController accountController, BankController bankController,
                                        DropdownController dropdownController,
                                        DropdownTypeController dropdownTypeController, BillController billController,
                                        TransactionController transactionController, BankRecordController bankRecordController,
                                        SummaryController summaryController) {
        JFrame frame = new JFrame("Treasure Tracker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(1500, 900));
        frame.pack();

        com.finance.treasuretracker.view.tabs.menu.Menu menu = new Menu();
        menu.setupMenu(frame, dropdownTypeController, dropdownController);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Summary", new SummaryPanel(summaryController));
        tabbedPane.addTab("Transactions", new TransactionsPanel(transactionController, bankRecordController, accountController));
        tabbedPane.addTab("Bills", new BillsView(billController, dropdownController, accountController));
        tabbedPane.addTab("Banks", new BanksView(bankController)); // Pass the controller here
        tabbedPane.addTab("Accounts", new AccountsView(accountController, bankController, dropdownController, dropdownTypeController));

        tabbedPane.addChangeListener(e -> {
            if (tabbedPane.getSelectedComponent() instanceof TransactionsPanel) {
                ((TransactionsPanel) tabbedPane.getSelectedComponent()).reloadData();
            }
        });

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

