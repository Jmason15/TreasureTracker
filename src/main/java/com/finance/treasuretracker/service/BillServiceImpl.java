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
        try {
            // Get all existing data
            List<Bill> bills = billRepository.findAll();
            List<Transaction> currentTransactions = transactionRepository.findAll();

            // Split transactions by paid status
            List<Transaction> unpaidTransactions = currentTransactions.stream()
                    .filter(e -> !e.getPaid())
                    .toList();
            List<Transaction> paidTransactions = currentTransactions.stream()
                    .filter(Transaction::getPaid)
                    .toList();

            // Calculate end date (last day of next year)
            Date lastDayOfNextYear = calculateEndDate();
            System.out.println("End date set to: " + lastDayOfNextYear);

            // Generate new transactions
            List<Transaction> transactionsToSave = generateTransactionsForBills(bills, paidTransactions,
                    lastDayOfNextYear);

            System.out.println("Generated " + transactionsToSave.size() + " new transactions");

            // Batch update database
            transactionRepository.deleteAll(unpaidTransactions);
            transactionRepository.saveAll(transactionsToSave);
        } catch (Exception e) {
            e.printStackTrace(); // Better error logging
        }
    }

    private Date calculateEndDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, 1); // First day of current year
        calendar.add(Calendar.YEAR, 2); // Add two years
        calendar.add(Calendar.DAY_OF_YEAR, -1); // Last day of next year
        return calendar.getTime();
    }

    private List<Transaction> generateTransactionsForBills(List<Bill> bills,
            List<Transaction> paidTransactions, Date lastDayOfNextYear) {
        List<Transaction> transactionsToSave = new ArrayList<>();

        for (Bill bill : bills) {
            System.out.println("Processing bill: " + bill.getBillId() + " - Due date: " + bill.getDueDay());

            if (bill.getDueDay() == null) {
                System.out.println("Skipping bill " + bill.getBillId() + " - No due date");
                continue;
            }

            Date currentDate = bill.getDueDay();
            Date endDate = bill.getEndDate() != null ? bill.getEndDate() : lastDayOfNextYear;

            // Ensure we're working with clean dates (no time components)
            currentDate = removeTimeFromDate(currentDate);
            endDate = removeTimeFromDate(endDate);

            while (currentDate.compareTo(endDate) <= 0) { // Changed to use compareTo
                // Create transaction for current date
                Transaction newTransaction = createTransaction(bill, currentDate);
                if (!isTransactionExists(newTransaction, paidTransactions, bill)) {
                    transactionsToSave.add(newTransaction);
                    System.out.println("Added transaction for bill " + bill.getBillId() + " on date: " + currentDate);
                } else {
                    System.out.println(
                            "Skipped existing transaction for bill " + bill.getBillId() + " on date: " + currentDate);
                }

                // Calculate next date
                currentDate = calculateNextDueDate(currentDate, bill.getFrequency().getDisplay());
                currentDate = removeTimeFromDate(currentDate); // Ensure clean date after calculation
            }
        }
        return transactionsToSave;
    }

    private Date removeTimeFromDate(Date date) {
        if (date == null)
            return null;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    private boolean isTransactionExists(Transaction newTransaction,
            List<Transaction> paidTransactions, Bill bill) {
        Date transactionDate = removeTimeFromDate(newTransaction.getDate());
        return paidTransactions.stream()
                .anyMatch(t -> removeTimeFromDate(t.getDate()).equals(transactionDate)
                        && t.getBill() != null
                        && t.getBill().equals(bill));
    }

    private Transaction createTransaction(Bill bill, Date date) {
        Transaction toReturn = new Transaction();
        toReturn.setDate(date);
        toReturn.setBill(bill);
        toReturn.setPaid(false);
        toReturn.setAccount(bill.getAccount());

        return toReturn;
    }

    private Date calculateNextDueDate(Date currentDate, String frequency) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);

        switch (frequency) {
            case "Monthly":
                calendar.add(Calendar.MONTH, 1);
                break;
            case "Bi-Weekly":
                calendar.add(Calendar.WEEK_OF_YEAR, 2);
                break;
            case "Six Months":
                calendar.add(Calendar.MONTH, 6);
                break;
            case "Every Two Months":
                calendar.add(Calendar.MONTH, 2);
                break;
            case "Every Three Months":
                calendar.add(Calendar.MONTH, 3);
                break;
            case "Yearly":
                calendar.add(Calendar.YEAR, 1);
                break;
            case "One Time":
                break;
            default:
                System.out.println("Unsupported frequency for bill");
                break;
        }
        return calendar.getTime();
    }

}
