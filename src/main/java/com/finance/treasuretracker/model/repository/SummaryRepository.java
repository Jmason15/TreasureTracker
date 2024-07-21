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
                       ACCOUNT.display_name as account
            FROM BILL
            LEFT JOIN ACCOUNT on ACCOUNT.account_id = BILL.account_id
            """, nativeQuery = true)
    List<SummaryViewInterface> findAllSummary();

}
