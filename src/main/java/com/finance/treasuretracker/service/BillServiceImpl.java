package com.finance.treasuretracker.service;

import com.finance.treasuretracker.service.BillServiceInterface;
import com.finance.treasuretracker.model.Bill;

import com.finance.treasuretracker.model.Transaction;
import com.finance.treasuretracker.model.repository.BillRepository;
import com.finance.treasuretracker.model.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Calendar;

@Service
public class BillServiceImpl implements BillServiceInterface {

    private final BillRepository billRepository;
    private final TransactionRepository transactionRepository;

    @Autowired
    public BillServiceImpl(BillRepository billRepository, TransactionRepository transactionRepository) {
        this.billRepository = billRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public List<Bill> getAllBills() {
        return billRepository.findAll();
    }

    @Override
    public Bill getBillById(Long id) {
        Optional<Bill> optionalBill = billRepository.findById(id);
        return optionalBill.orElse(null);
    }

    @Override
    public Bill createBill(Bill bill) {
        return billRepository.save(bill);
    }

    @Override
    public Bill updateBill(Bill bill) {
        return billRepository.save(bill);
    }

    @Override
    public void deleteBill(Long id) {
        billRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void correctTransations() {
        List<Bill> listOfBills = billRepository.findAll();
        List<Transaction> listOfCurrentTransactions = transactionRepository.findAll();
        List<Transaction> listOfCurrentNotPaidTransactions = listOfCurrentTransactions.stream()
                .filter(e -> !e.getPaid())
                .toList();
        List<Transaction> listOfCurrentPaidTransactions = listOfCurrentTransactions.stream()
                .filter(Transaction::getPaid)
                .toList();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, 1); // First day of the current year
        calendar.add(Calendar.YEAR, 2);
        calendar.add(Calendar.DAY_OF_YEAR, -1); // Last day of the next year
        Date lastDayOfNextYear = calendar.getTime();

        List<Transaction> transactionsToSave = new ArrayList<>();
        try {
            for (Bill bill : listOfBills) {
                int dropdownId = bill.getFrequency().getDropdownId().intValue();
                Date nextDate = bill.getDueDay();
                Date endDate = bill.getEndDate() != null ? bill.getEndDate() : lastDayOfNextYear;

                while (!nextDate.after(endDate)) {
                    calendar.setTime(nextDate);
                    switch (dropdownId) {
                        case 3: // Monthly
                            calendar.add(Calendar.MONTH, 1);
                            break;
                        case 7: // Bi-weekly
                            calendar.add(Calendar.WEEK_OF_YEAR, 2);
                            break;
                        case 8: // Every Six Months
                            calendar.add(Calendar.MONTH, 6);
                            break;
                        case 9: // Every Two Months
                            calendar.add(Calendar.MONTH, 2);
                            break;
                        case 10: // Every Three Months
                            calendar.add(Calendar.MONTH, 3);
                            break;
                        case 11: // Yearly
                            calendar.add(Calendar.YEAR, 1);
                            break;
                        case 12: // One time
                            break;
                        default:
                            System.out.println("Unsupported frequency for bill ID: " + bill.getBillId());
                            break;
                    }
                    nextDate = calendar.getTime();
                    if (!nextDate.after(endDate)) {
                        Transaction toAdd = createTransaction(bill, nextDate);
                        boolean transactionExists = listOfCurrentPaidTransactions.stream()
                                .anyMatch(t -> t.getDate().equals(toAdd.getDate()) && t.getBill() != null && t.getBill().equals(bill));

                        if (!transactionExists) {
                            transactionsToSave.add(toAdd);
                        }
                    }
                }
            }

            transactionRepository.deleteAll(listOfCurrentNotPaidTransactions);
            transactionRepository.saveAll(transactionsToSave);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    private Transaction createTransaction(Bill bill, Date date) {
        Transaction toReturn = new Transaction();
        toReturn.setDate(date);
        toReturn.setBill(bill);
        toReturn.setPaid(false);
        toReturn.setAccount(bill.getAccount());

        return toReturn;
    }

}
