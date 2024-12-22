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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
@Service
public class SummaryServiceImpl implements SummaryServiceInterface{
    private final SummaryRepository summaryRepository;
    private final BillRepository billRepository;
    public SummaryServiceImpl(SummaryRepository summaryRepository, BillRepository billRepository) {
        this.summaryRepository = summaryRepository;
        this.billRepository = billRepository;
    }

    @Override
    public List<SummaryViewDTO> findAllSummary() {
        List<SummaryViewInterface> data = summaryRepository.findAllSummary();
        List<SummaryViewDTO> toReturn = new ArrayList<>();
        for(SummaryViewInterface summaryRecord : data){
            SummaryViewDTO toAdd = new SummaryViewDTO();
            toAdd = toAdd.convert(summaryRecord);
            assert toAdd != null;
            toAdd.setExpectedTotalCount(findYearlyCountForBill(summaryRecord));
            toReturn.add(toAdd);
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
        income.setTotalMonth(toReturn.getTotalIncomeYear() != null ?
                toReturn.getTotalIncomeYear().divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO);
        income.setRemYear(toReturn.getRemIncomeYear());
        income.setRemMonth(toReturn.getRemIncomeYear() != null ?
                toReturn.getRemIncomeYear().divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO);

        BreakoutListDTO bill = new BreakoutListDTO();
        bill.setDesctiption("Bills");
        bill.setTotalYear(toReturn.getTotalBillYear());
        bill.setTotalMonth(toReturn.getTotalBillYear() != null ?
                toReturn.getTotalBillYear().divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO);
        bill.setRemYear(toReturn.getRemBillYear());
        bill.setRemMonth(toReturn.getRemBillYear() != null ?
                toReturn.getRemBillYear().divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO);

        BreakoutListDTO takeHome = new BreakoutListDTO();
        takeHome.setDesctiption("Take Home");
        takeHome.setTotalYear(toReturn.getTotalIncomeYear() != null && toReturn.getTotalBillYear() != null ?
                toReturn.getTotalIncomeYear().add(toReturn.getTotalBillYear()) : BigDecimal.ZERO);
        takeHome.setTotalMonth(toReturn.getTotalIncomeYear() != null && toReturn.getTotalBillYear() != null ?
                toReturn.getTotalIncomeYear().add(toReturn.getTotalBillYear()).divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO);
        takeHome.setRemYear(toReturn.getRemIncomeYear() != null && toReturn.getRemBillYear() != null ?
                toReturn.getRemIncomeYear().add(toReturn.getRemBillYear()) : BigDecimal.ZERO);
        takeHome.setRemMonth(toReturn.getRemIncomeYear() != null && toReturn.getRemBillYear() != null ?
                toReturn.getRemIncomeYear().add(toReturn.getRemBillYear()).divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO);

        toReturnList.add(income);
        toReturnList.add(bill);
        toReturnList.add(takeHome);

        return toReturnList;
    }

    public Long findYearlyCountForBill(SummaryViewInterface record){
        Bill bill = billRepository.findById(record.getBillId()).get();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, 1); // First day of the current year
        calendar.add(Calendar.YEAR, 1);
        calendar.add(Calendar.DAY_OF_YEAR, -1); // Last day of the current year
        Date lastDayOfYear = calendar.getTime();
        Long count = 0L;
        calendar.setTime(bill.getDueDay());
        Date current = calendar.getTime();
        while (current.before(lastDayOfYear)) {
            count++;
            switch (record.getFrequencyValue()) {
                case 3: // Monthly
                    calendar.add(Calendar.MONTH, 1);
                    current = calendar.getTime();
                    break;
                case 7: // Bi-weekly
                    calendar.add(Calendar.WEEK_OF_YEAR, 2);
                    current = calendar.getTime();
                    break;
                case 8: // Every Six Months
                    calendar.add(Calendar.MONTH, 6);
                    current = calendar.getTime();
                    break;
                case 9: // Every Two Months
                    calendar.add(Calendar.MONTH, 2);
                    current = calendar.getTime();
                    break;
                case 10: // Every Three Months
                    calendar.add(Calendar.MONTH, 3);
                    current = calendar.getTime();
                    break;
                case 11: // Yearly
                    calendar.add(Calendar.YEAR, 1);
                    current = calendar.getTime();
                    break;
                default:
                    System.out.println("Unsupported frequency for bill: " + record.getBillName());
                    break;
            }
        }
        return count;
    }
}
