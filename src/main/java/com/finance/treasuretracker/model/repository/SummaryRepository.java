package com.finance.treasuretracker.model.repository;

import com.finance.treasuretracker.model.Transaction;
import com.finance.treasuretracker.model.dto.BreakoutListInterface;
import com.finance.treasuretracker.model.dto.SummaryViewDTO;
import com.finance.treasuretracker.model.dto.SummaryViewInterface;
import com.finance.treasuretracker.service.SummaryServiceInterface;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SummaryRepository extends JpaRepository<Transaction, Long> {
    @Query(value = """
            SELECT BILL.amount as billAmount,
                       BILL.name as billName,
                       ACCOUNT.display_name as billAcount,
                       dropdown.display as frequencyDisplay,
                       dropdown.dropdown_id as frequencyValue,
                       transactionRemaining.count as transactionRemaining,
                       transactionRemaining.sum as amountRemaining,
                       transactionPaid.count as transactionPaid,
                       transactionPaid.sum as amountPaid,
                       totalCount.count as totalCount,
                       bill.bill_id as billId
            FROM BILL
            JOIN ACCOUNT on ACCOUNT.account_id = BILL.account_id
            JOIN dropdown on dropdown.dropdown_id = Bill.frequency
            JOIN ( select tr.bill_id, COUNT(*) as count, SUM(b.amount) as sum from transaction_record tr\s
                    JOIN bill b on b.bill_id = tr.bill_id
                    and tr.paid = 0
                    GROUP BY tr.bill_id
            ) transactionRemaining on transactionRemaining.bill_id = bill.bill_id
            JOIN (
                    select tr.bill_id, COUNT(*) as count, SUM(b.amount) as sum from transaction_record tr\s
                     LEFT JOIN bill b on b.bill_id = tr.bill_id
                     and tr.paid = 1
                     GROUP BY tr.bill_id
             ) transactionPaid on transactionPaid.bill_id = bill.bill_id
            JOIN (
                 select tr.bill_id, COUNT(*) as count from transaction_record tr\s
                     LEFT JOIN bill b on b.bill_id = tr.bill_id
                     GROUP BY tr.bill_id
            ) totalCount on totalCount.bill_id = bill.bill_id
         ORDER BY BILL.name ASC
            """, nativeQuery = true)
    List<SummaryViewInterface> findAllSummary();

    @Query(value = """
            SELECT
                (SELECT SUM(bill.amount) FROM transaction_record
                JOIN bill ON bill.bill_id = transaction_record.bill_id
                WHERE bill.amount > 0 AND strftime('%Y', datetime(transaction_record.date / 1000, 'unixepoch')) = strftime('%Y', 'now')
                ) AS totalIncomeYear,
                (SELECT SUM(bill.amount) FROM transaction_record
                JOIN bill ON bill.bill_id = transaction_record.bill_id
                WHERE bill.amount < 0 AND strftime('%Y', datetime(transaction_record.date / 1000, 'unixepoch')) = strftime('%Y', 'now')
                ) AS totalBillYear,
                (SELECT SUM(bill.amount) FROM transaction_record
                JOIN bill ON bill.bill_id = transaction_record.bill_id
                WHERE bill.amount > 0 AND transaction_record.paid = 0 AND strftime('%Y', datetime(transaction_record.date / 1000, 'unixepoch')) = strftime('%Y', 'now')
                ) AS remIncomeYear,
                (SELECT SUM(bill.amount) FROM transaction_record
                JOIN bill ON bill.bill_id = transaction_record.bill_id
                WHERE bill.amount < 0 AND transaction_record.paid = 0 AND strftime('%Y', datetime(transaction_record.date / 1000, 'unixepoch')) = strftime('%Y', 'now')
                ) AS remBillYear
     """, nativeQuery = true)
    BreakoutListInterface findBreakoutList();
}
