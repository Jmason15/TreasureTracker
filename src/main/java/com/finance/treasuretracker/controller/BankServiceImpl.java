package com.finance.treasuretracker.controller;

import com.finance.treasuretracker.model.Bank;
import com.finance.treasuretracker.model.repository.BankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class BankServiceImpl implements BankServiceInterface {
    private final BankRepository bankRepository;

    @Autowired
    public BankServiceImpl(BankRepository bankRepository) {
        this.bankRepository = bankRepository;
    }

    @Override
    public List<Bank> getAllBanks() {
        // Business logic if any
        return bankRepository.findAll();
    }

    @Override
    public void saveBank(Bank bank) {
        bankRepository.save(bank);
    }

    @Override
    public Bank getById(Long id) {
        return bankRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Bank with ID " + id + " not found."));
    }

    @Override
    public void delete(Bank bank){
        bankRepository.delete(bank);
    }

    @Override
    public void updateBank(Bank bank){
        bankRepository.save(bank);
    }
}
