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
         SELECT
          BILL.amount AS billAmount
        , BILL.name AS billName
        , ACCOUNT.display_name AS billAcount
        , dropdown.display AS frequencyDisplay
        , dropdown.value AS frequencyValue
        , COALESCE(transactionRemaining.count, 0) AS transactionRemaining
        , COALESCE(transactionRemaining.sum, 0) AS amountRemaining
        , COALESCE(transactionPaid.count, 0) AS transactionPaid
        , COALESCE(transactionPaid.sum, 0) AS amountPaid
        , totalCount.count AS totalCount
        , bill.bill_id AS billId
        , CAST(ABS(bill.due_day) AS TEXT) AS dueDate
      FROM BILL
      JOIN ACCOUNT ON ACCOUNT.account_id = BILL.account_id
      JOIN dropdown ON dropdown.dropdown_id = Bill.frequency
      LEFT JOIN (
          SELECT
              tr.bill_id
            , COUNT(*) AS count
            , SUM(b.amount) AS sum
          FROM transaction_record tr
          JOIN bill b ON b.bill_id = tr.bill_id
          WHERE tr.paid = 0
          AND strftime('%Y', datetime(tr.date / 1000, 'unixepoch')) = strftime('%Y', 'now')
          GROUP BY tr.bill_id
      ) transactionRemaining ON transactionRemaining.bill_id = bill.bill_id
      LEFT JOIN (
          SELECT
              tr.bill_id
            , COUNT(*) AS count
            , SUM(b.amount) AS sum
          FROM transaction_record tr
          LEFT JOIN bill b ON b.bill_id = tr.bill_id
          WHERE tr.paid = 1
          AND strftime('%Y', datetime(tr.date / 1000, 'unixepoch')) = strftime('%Y', 'now')
          GROUP BY tr.bill_id
      ) transactionPaid ON transactionPaid.bill_id = bill.bill_id
      JOIN (
          SELECT
              tr.bill_id
            , COUNT(*) AS count
          FROM transaction_record tr
          LEFT JOIN bill b ON b.bill_id = tr.bill_id
          WHERE strftime('%Y', datetime(tr.date / 1000, 'unixepoch')) = strftime('%Y', 'now')
          GROUP BY tr.bill_id
      ) totalCount ON totalCount.bill_id = bill.bill_id
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
