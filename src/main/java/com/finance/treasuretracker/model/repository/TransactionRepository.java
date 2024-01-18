package com.finance.treasuretracker.model.repository;

import com.finance.treasuretracker.model.Transaction;
import com.finance.treasuretracker.model.dto.TransactionGridInterface;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query(value = """
             SELECT t.transaction_Id as transactionId,
                  t.paid as paid,
                  bll.name as billName,
                  bll.amount as billAmount,
                  bnk.name as bankName,
                  bnk.bank_id as bankId,
                  t.date as transactionDate,
                  a.display_Name accountDisplayName
              FROM TRANSACTION_RECORD t
              LEFT JOIN BILL bll on bll.bill_Id = t.bill_id
              LEFT JOIN ACCOUNT a on a.account_Id = t.account_id
              LEFT JOIN BANK bnk on bnk.bank_id = a.bankid
              ORDER BY t.date DESC
            """, nativeQuery = true)
    List<TransactionGridInterface> getAllTransactionsForGrid();
}
