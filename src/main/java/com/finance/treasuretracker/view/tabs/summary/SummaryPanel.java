package com.finance.treasuretracker.view.tabs.summary;

import com.finance.treasuretracker.controller.SummaryController;
import com.finance.treasuretracker.model.dto.SummaryViewDTO;
import com.finance.treasuretracker.model.dto.SummaryViewInterface;
import com.finance.treasuretracker.view.tabs.summary.enums.SummaryColumnEnum;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

        //JScrollPane scrollPane = new JScrollPane(table);

        JScrollPane scrollPane = new JScrollPane(table,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        JScrollBar bar = scrollPane.getVerticalScrollBar();
        bar.setPreferredSize(new Dimension(20, 0));

        add(scrollPane, BorderLayout.CENTER);
    }

    public void populateTableWithData() {
        List<SummaryViewDTO> summaryData = summaryController.getAllSummaryView();
        summaryTableModel.setRowCount(0);
        BigDecimal totalLeftForYear = BigDecimal.valueOf(0);
        BigDecimal totalPaidForYear = BigDecimal.valueOf(0);
        for (SummaryViewDTO summary : summaryData) {
            Map<SummaryColumnEnum, Object> rowData = getSummaryColumnEnumObjectMap(summary, summary.getBillName());
            if(Objects.nonNull(summary.getAmountRemaining())){
                totalLeftForYear = totalLeftForYear.add(summary.getAmountRemaining()) ;
            }
            if(Objects.nonNull(summary.getAmountPaid())){
                totalPaidForYear = totalPaidForYear.add(summary.getAmountPaid()) ;
            }

            summaryTableModel.addRow(rowData.values().toArray());
        }
        Map<SummaryColumnEnum, Object> rowData = getSummaryColumnEnumObjectMap(null, "Total");
        rowData.put(SummaryColumnEnum.AMOUNT_REMAINING, totalLeftForYear);
        rowData.put(SummaryColumnEnum.AMOUNT_PAID, totalPaidForYear);

        summaryTableModel.addRow(rowData.values().toArray());


    }

    private static Map<SummaryColumnEnum, Object> getSummaryColumnEnumObjectMap(SummaryViewDTO summary, String rowName) {
        Map<SummaryColumnEnum, Object> rowData = new EnumMap<>(SummaryColumnEnum.class);
        rowData.put(SummaryColumnEnum.ACCOUNT, Objects.nonNull(summary) ? summary.getBillAccount(): null);
        rowData.put(SummaryColumnEnum.NAME, rowName);
        rowData.put(SummaryColumnEnum.FREQUENCY, Objects.nonNull(summary) ? summary.getFrequencyDisplay(): null);
        rowData.put(SummaryColumnEnum.AMOUNT, Objects.nonNull(summary) ? summary.getBillAmount(): null);
        //rowData.put(SummaryColumnEnum.TIMES_PER_YEAR, summary.getFrequencyValue());
        rowData.put(SummaryColumnEnum.TOTAL_COUNT, Objects.nonNull(summary) ? summary.getTotalCount(): null);
        rowData.put(SummaryColumnEnum.TOTAL_EXPECTED_COUNT, Objects.nonNull(summary) ? summary.getExpectedTotalCount(): null);

        rowData.put(SummaryColumnEnum.COUNT_REMAINING, Objects.nonNull(summary) ? summary.getTransactionRemaining(): null);
        rowData.put(SummaryColumnEnum.COUNT_PAID, Objects.nonNull(summary) ? summary.getTransactionPaid(): null);

        rowData.put(SummaryColumnEnum.AMOUNT_REMAINING, Objects.nonNull(summary) ? summary.getAmountRemaining(): null);
        rowData.put(SummaryColumnEnum.AMOUNT_PAID, Objects.nonNull(summary) ? summary.getAmountPaid(): null);
        return rowData;
    }
}
