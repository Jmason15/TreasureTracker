package com.finance.treasuretracker.view.tabs.summary;

import com.finance.treasuretracker.controller.SummaryController;
import com.finance.treasuretracker.model.dto.SummaryViewInterface;
import com.finance.treasuretracker.view.tabs.summary.enums.SummaryColumnEnum;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;


public class SummaryPanel  extends JPanel {

    private DefaultTableModel tableModel;
    private final SummaryController summaryController;
    public SummaryPanel(SummaryController summaryController) {
        this.summaryController = summaryController;
        JPanel banksPanel = new JPanel();
        banksPanel.setLayout(new BorderLayout());
        JLabel summaryLabel = new JLabel("Summary");
        banksPanel.add(summaryLabel, BorderLayout.NORTH);
        initializeUi();
    }

    private void initializeUi(){
        tableModel = new DefaultTableModel(SummaryColumnEnum.getColumnNames(), 0);
        JTable table = new JTable(tableModel);
        populateTableWithData();
    }

    public void populateTableWithData() {
        List<SummaryViewInterface> summaryData = summaryController.getAllSummaryView();
        tableModel.setRowCount(0);
        for(SummaryViewInterface summary : summaryData){
            Map<SummaryColumnEnum, Object> rowdata = new EnumMap<>(SummaryColumnEnum.class);
            rowdata.put(SummaryColumnEnum.NAME, summary.getBillName());
            rowdata.put(SummaryColumnEnum.AMOUNT, summary.getBillAmount());
            rowdata.put(SummaryColumnEnum.ACCOUNT, summary.getBillAccount());

            tableModel.addRow(rowdata.values().toArray());

        }
    }
}
