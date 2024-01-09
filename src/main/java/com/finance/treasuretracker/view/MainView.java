package com.finance.treasuretracker.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.finance.treasuretracker.view.tabs.configuration.ConfigurationPanel;

public class MainView {
    public static void createAndShowGUI() {
        JFrame frame = new JFrame("Treasure Tracker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);

        Menu menu = new Menu();
        menu.setupMenu(frame);
// Creating panels for each tab

        JPanel summaryPanel = new JPanel();
        JPanel configurePanel = new JPanel();
        JPanel transactionsPanel = new JPanel();

        summaryPanel.setLayout(new BorderLayout());
        JLabel summaryLabel = new JLabel("Summary Information");
        summaryPanel.add(summaryLabel, BorderLayout.NORTH);



        transactionsPanel.setLayout(new GridLayout(1, 1));
        JTable transactionsTable = new JTable(); // You might want to populate this with data
        transactionsPanel.add(new JScrollPane(transactionsTable));


// Create an instance of ConfigurationPanel and add it to the tabbed pane
        ConfigurationPanel configPanel = new ConfigurationPanel();

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Summary", summaryPanel);
        tabbedPane.addTab("Transactions", transactionsPanel);
        tabbedPane.addTab("Configure", configPanel);
        frame.add(tabbedPane);

        frame.pack();
        // Show the frame
        frame.setVisible(true);
    }
}
