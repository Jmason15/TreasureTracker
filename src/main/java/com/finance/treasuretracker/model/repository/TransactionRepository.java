package com.finance.treasuretracker.model.repository;

import com.finance.treasuretracker.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
