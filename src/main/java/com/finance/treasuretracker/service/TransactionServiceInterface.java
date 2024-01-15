package com.finance.treasuretracker.service;

import com.finance.treasuretracker.model.Transaction;
import java.util.List;

public interface TransactionServiceInterface {
    List<Transaction> getAllTransactions();
    Transaction getTransactionById(Long id);
    void saveTransaction(Transaction transaction);
    void updateTransaction(Transaction transaction);
    void deleteTransaction(Long id);
}

