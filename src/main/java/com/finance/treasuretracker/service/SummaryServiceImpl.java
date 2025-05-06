package com.finance.treasuretracker.service;

import com.finance.treasuretracker.model.Bill;
import com.finance.treasuretracker.model.dto.BreakoutListDTO;
import com.finance.treasuretracker.model.dto.BreakoutListInterface;
import com.finance.treasuretracker.model.dto.SummaryViewDTO;
import com.finance.treasuretracker.model.dto.SummaryViewInterface;
import com.finance.treasuretracker.model.repository.BillRepository;
import com.finance.treasuretracker.model.repository.SummaryRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class SummaryServiceImpl implements SummaryServiceInterface {
    private final SummaryRepository summaryRepository;
    private final BillRepository billRepository;

    public SummaryServiceImpl(SummaryRepository summaryRepository, BillRepository billRepository) {
        this.summaryRepository = summaryRepository;
        this.billRepository = billRepository;
    }

    @Override
    public List<SummaryViewDTO> findAllSummary() {
        System.out.println("=== Starting findAllSummary ===");
        List<SummaryViewInterface> data = summaryRepository.findAllSummary();
        List<SummaryViewDTO> toReturn = new ArrayList<>();

        System.out.println("Found " + data.size() + " records to process");

        for (SummaryViewInterface summaryRecord : data) {
            SummaryViewDTO toAdd = new SummaryViewDTO();
            toAdd = toAdd.convert(summaryRecord);
            if (toAdd != null) {
                Long count = findYearlyCountForBill(summaryRecord);
                System.out.println("Count for " + summaryRecord.getBillName() + ": " + count);
                toAdd.setExpectedTotalCount(count);
                toReturn.add(toAdd);
            }
        }
        return toReturn;
    }

    @Override
    public List<BreakoutListDTO> getBreakoutList() {
        BreakoutListInterface toReturn = summaryRepository.findBreakoutList();
        List<BreakoutListDTO> toReturnList = new ArrayList<>();

        BreakoutListDTO income = new BreakoutListDTO();
        income.setDesctiption("Income");
        income.setTotalYear(toReturn.getTotalIncomeYear());
        income.setTotalMonth(toReturn.getTotalIncomeYear() != null
                ? toReturn.getTotalIncomeYear().divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO);
        income.setRemYear(toReturn.getRemIncomeYear());
        income.setRemMonth(toReturn.getRemIncomeYear() != null
                ? toReturn.getRemIncomeYear().divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO);

        BreakoutListDTO bill = new BreakoutListDTO();
        bill.setDesctiption("Bills");
        bill.setTotalYear(toReturn.getTotalBillYear());
        bill.setTotalMonth(toReturn.getTotalBillYear() != null
                ? toReturn.getTotalBillYear().divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO);
        bill.setRemYear(toReturn.getRemBillYear());
        bill.setRemMonth(toReturn.getRemBillYear() != null
                ? toReturn.getRemBillYear().divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO);

        BreakoutListDTO takeHome = new BreakoutListDTO();
        takeHome.setDesctiption("Take Home");
        takeHome.setTotalYear(toReturn.getTotalIncomeYear() != null && toReturn.getTotalBillYear() != null
                ? toReturn.getTotalIncomeYear().add(toReturn.getTotalBillYear())
                : BigDecimal.ZERO);
        takeHome.setTotalMonth(toReturn.getTotalIncomeYear() != null && toReturn.getTotalBillYear() != null
                ? toReturn.getTotalIncomeYear().add(toReturn.getTotalBillYear()).divide(BigDecimal.valueOf(12), 2,
                        RoundingMode.HALF_UP)
                : BigDecimal.ZERO);
        takeHome.setRemYear(toReturn.getRemIncomeYear() != null && toReturn.getRemBillYear() != null
                ? toReturn.getRemIncomeYear().add(toReturn.getRemBillYear())
                : BigDecimal.ZERO);
        takeHome.setRemMonth(toReturn.getRemIncomeYear() != null && toReturn.getRemBillYear() != null
                ? toReturn.getRemIncomeYear().add(toReturn.getRemBillYear()).divide(BigDecimal.valueOf(12), 2,
                        RoundingMode.HALF_UP)
                : BigDecimal.ZERO);

        toReturnList.add(income);
        toReturnList.add(bill);
        toReturnList.add(takeHome);

        return toReturnList;
    }

    private enum BillFrequency {
        MONTHLY("Monthly", 12L, 1),
        BI_WEEKLY("Bi-Weekly", 26L, 2.167),
        SIX_MONTHS("Six Months", 2L, 6),
        TWO_MONTHS("Every Two Months", 6L, 2),
        THREE_MONTHS("Every Three Months", 4L, 3),
        YEARLY("Yearly", 1L, 12),
        ONE_TIME("One Time", 1L, 0);

        private final String display;
        private final Long yearlyCount;
        private final double monthInterval;

        BillFrequency(String display, Long yearlyCount, double monthInterval) {
            this.display = display;
            this.yearlyCount = yearlyCount;
            this.monthInterval = monthInterval;
        }

        static BillFrequency fromDisplay(String display) {
            return Arrays.stream(values())
                    .filter(freq -> freq.display.equals(display))
                    .findFirst()
                    .orElse(null);
        }
    }

    public Long findYearlyCountForBill(SummaryViewInterface record) {
        System.out.println("=== Starting findYearlyCountForBill ===");

        if (record == null) {
            System.out.println("Record is null");
            return 0L;
        }

        System.out.println("Processing bill: " + record.getBillName());
        System.out.println("Frequency: " + record.getFrequencyDisplay());
        System.out.println("Raw due date from DB: " + record.getDueDate() +
                " Class type: " + (record.getDueDate() != null ? record.getDueDate().getClass().getName() : "null"));

        if (record.getFrequencyDisplay() == null) {
            System.out.println("Frequency is null");
            return 0L;
        }

        BillFrequency frequency = BillFrequency.fromDisplay(record.getFrequencyDisplay());
        if (frequency == null) {
            System.out.println("Unsupported frequency for bill: " + record.getBillName());
            return 0L;
        }

        Date dueDate = null;
        if (record.getDueDate() != null) {
            try {
                long timestamp = Long.parseLong(record.getDueDate());
                System.out.println("Bill: " + record.getBillName());
                System.out.println("Raw timestamp from DB: " + timestamp);
                System.out.println("Expected date: " + new Date(timestamp));
                dueDate = new Date(timestamp);
            } catch (Exception e) {
                System.out.println("Error converting date for " + record.getBillName() + ": " + e.getMessage());
                e.printStackTrace();
            }
        }

        // If due date is null, return the standard yearly count
        if (dueDate == null) {
            System.out.println("Using default yearly count for " + record.getBillName() + ": " + frequency.yearlyCount);
            return frequency.yearlyCount;
        }

        // Get the year we're interested in (current year)
        Calendar currentCal = Calendar.getInstance();
        int targetYear = currentCal.get(Calendar.YEAR);

        // Get bill's start date info
        Calendar billCal = Calendar.getInstance();
        billCal.setTime(dueDate);
        int billYear = billCal.get(Calendar.YEAR);

        System.out.println("Bill: " + record.getBillName() +
                " Target Year: " + targetYear +
                " Bill Year: " + billYear +
                " Due Date: " + dueDate);

        // If bill starts in a future year, return 0
        if (billYear > targetYear) {
            System.out.println("Bill starts in future year, returning 0");
            return 0L;
        }

        // If bill starts in a past year, return full year count
        if (billYear < targetYear) {
            System.out.println("Bill started in past year, returning full count: " + frequency.yearlyCount);
            return frequency.yearlyCount;
        }

        // For bills starting in current year
        Calendar endCal = Calendar.getInstance();
        endCal.set(targetYear, Calendar.DECEMBER, 31, 23, 59, 59);

        // Count occurrences
        long count = 0;
        Date currentDate = dueDate;
        Date endDate = endCal.getTime();

        System.out.println("Counting occurrences from " + currentDate + " to " + endDate);

        while (!currentDate.after(endDate)) {
            count++;
            System.out.println("Found occurrence " + count + " on " + currentDate);
            currentDate = getNextDueDate(currentDate, frequency);
        }

        System.out.println("Final count for " + record.getBillName() + ": " + count);
        return count;
    }

    private Date getNextDueDate(Date currentDate, BillFrequency frequency) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);

        switch (frequency) {
            case MONTHLY:
                cal.add(Calendar.MONTH, 1);
                break;
            case BI_WEEKLY:
                cal.add(Calendar.WEEK_OF_YEAR, 2);
                break;
            case SIX_MONTHS:
                cal.add(Calendar.MONTH, 6);
                break;
            case TWO_MONTHS:
                cal.add(Calendar.MONTH, 2);
                break;
            case THREE_MONTHS:
                cal.add(Calendar.MONTH, 3);
                break;
            case YEARLY:
                cal.add(Calendar.YEAR, 1);
                break;
            case ONE_TIME:
                cal.add(Calendar.YEAR, 99);
                break;
        }
        return cal.getTime();
    }
}
