package com.finance.treasuretracker.view;

import javax.swing.*;
import java.awt.*;

import com.finance.treasuretracker.controller.AccountController;
import com.finance.treasuretracker.controller.BankController;
import com.finance.treasuretracker.controller.DropdownController;
import com.finance.treasuretracker.controller.DropdownTypeController;
import com.finance.treasuretracker.view.tabs.accounts.AccountsView;
import com.finance.treasuretracker.view.tabs.banks.BanksView;
import com.finance.treasuretracker.view.tabs.bills.BillsPanel;
import com.finance.treasuretracker.view.tabs.menu.Menu;
import com.finance.treasuretracker.view.tabs.summary.SummaryPanel;
import com.finance.treasuretracker.view.tabs.transactions.TransactionsPanel;

public class MainView {
    public static void createAndShowGUI(AccountController accountController, BankController bankController, DropdownController dropdownController, DropdownTypeController dropdownTypeController) {
        JFrame frame = new JFrame("Treasure Tracker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(2000, 1600);

        com.finance.treasuretracker.view.tabs.menu.Menu menu = new Menu();
        menu.setupMenu(frame, dropdownTypeController, dropdownController);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Summary", new SummaryPanel());
        tabbedPane.addTab("Transactions", new TransactionsPanel());
        tabbedPane.addTab("Bills", new BillsPanel());
        tabbedPane.addTab("Banks", new BanksView(bankController)); // Pass the controller here
        tabbedPane.addTab("Accounts", new AccountsView(accountController, bankController, dropdownController, dropdownTypeController));
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

