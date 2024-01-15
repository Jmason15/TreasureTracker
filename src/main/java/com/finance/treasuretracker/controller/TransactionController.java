package com.finance.treasuretracker.controller;

import com.finance.treasuretracker.service.TransactionServiceInterface;
import com.finance.treasuretracker.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionServiceInterface transactionService;

    @Autowired
    public TransactionController(TransactionServiceInterface transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public List<Transaction> getAllTransactions() {
        return transactionService.getAllTransactions();
    }

    @GetMapping("/{id}")
    public Transaction getTransactionById(@PathVariable Long id) {
        return transactionService.getTransactionById(id);
    }

    @PostMapping
    public void createTransaction(@RequestBody Transaction transaction) {
        transactionService.saveTransaction(transaction);
    }

    @PutMapping
    public void saveTransaction(@RequestBody Transaction transaction) {
        transactionService.updateTransaction(transaction);
    }

    @DeleteMapping("/{id}")
    public void deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
    }
}
