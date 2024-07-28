package com.finance.treasuretracker.service;

import com.finance.treasuretracker.model.Bill;
import com.finance.treasuretracker.model.dto.SummaryViewDTO;
import com.finance.treasuretracker.model.dto.SummaryViewInterface;
import com.finance.treasuretracker.model.repository.BillRepository;
import com.finance.treasuretracker.model.repository.SummaryRepository;
import org.springframework.stereotype.Service;

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
