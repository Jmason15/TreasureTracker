package com.finance.treasuretracker.model.repository;

import com.finance.treasuretracker.model.Transaction;
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
            		                       dropdown.value as frequencyValue,
                         				   transactionRemaining.count as transactionRemaining,
                                           transactionRemaining.sum as amountRemaining,
                                           transactionPaid.count as transactionPaid,
                                           transactionPaid.sum as amountPaid
            		            FROM BILL
            		            JOIN ACCOUNT on ACCOUNT.account_id = BILL.account_id
            		            JOIN dropdown on dropdown.dropdown_id = Bill.frequency
                     			JOIN ( select tr.bill_id, COUNT(*) as count, SUM(b.amount) as sum from transaction_record tr
                     					JOIN bill b on b.bill_id = tr.bill_id
                     					and tr.paid = 0
                     					GROUP BY tr.bill_id
                                ) transactionRemaining on transactionRemaining.bill_id = bill.bill_id
                     			JOIN (
                                        select tr.bill_id, COUNT(*) as count, SUM(b.amount) as sum from transaction_record tr
            					         LEFT JOIN bill b on b.bill_id = tr.bill_id
            					         and tr.paid = 1
            					         GROUP BY tr.bill_id
                                 ) transactionPaid on transactionPaid.bill_id = bill.bill_id
             ORDER BY BILL.name ASC
            """, nativeQuery = true)
    List<SummaryViewInterface> findAllSummary();

}
