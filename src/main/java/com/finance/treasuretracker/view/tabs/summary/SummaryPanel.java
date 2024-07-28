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

public class SummaryPanel extends JPanel {

    private DefaultTableModel summaryTableModel;
    private final SummaryController summaryController;

    public SummaryPanel(SummaryController summaryController) {
        this.summaryController = summaryController;
        setLayout(new BorderLayout());
        initializeUi();
    }

    private void initializeUi() {
        summaryTableModel = new DefaultTableModel(SummaryColumnEnum.getColumnNames(), 0);
        JTable table = new JTable(summaryTableModel);
        populateTableWithData();

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void populateTableWithData() {
        List<SummaryViewInterface> summaryData = summaryController.getAllSummaryView();
        summaryTableModel.setRowCount(0);
        for (SummaryViewInterface summary : summaryData) {
            Map<SummaryColumnEnum, Object> rowData = new EnumMap<>(SummaryColumnEnum.class);
            rowData.put(SummaryColumnEnum.ACCOUNT, summary.getBillAccount());
            rowData.put(SummaryColumnEnum.NAME, summary.getBillName());
            rowData.put(SummaryColumnEnum.FREQUENCY, summary.getFrequencyDisplay());
            rowData.put(SummaryColumnEnum.AMOUNT, summary.getBillAmount());
            rowData.put(SummaryColumnEnum.TIMES_PER_YEAR, summary.getFrequencyValue());

            rowData.put(SummaryColumnEnum.COUNT_REMAINING, summary.getTransactionRemaining());
            rowData.put(SummaryColumnEnum.COUNT_PAID, summary.getTransactionPaid());
            rowData.put(SummaryColumnEnum.AMOUNT_REMAINING, summary.getAmountRemaining());
            rowData.put(SummaryColumnEnum.AMOUNT_PAID, summary.getAmountPaid());



            summaryTableModel.addRow(rowData.values().toArray());
        }
    }
}
