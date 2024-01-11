package com.finance.treasuretracker.controller;

import com.finance.treasuretracker.model.Bank;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BankServiceInterface{
    List<Bank> getAllBanks();
    void saveBank(Bank bank);

    Bank getById(Long id);

    void delete(Bank bank);

    void updateBank(Bank bank);

//    BankRepository bankRepository;
//    @Autowired
//    public BankService(BankRepository bankRepository){
//        this.bankRepository = bankRepository;
//    }
//
//    public List<Bank> getAllbanks(){
//        return bankRepository.findAll();
//    }
}
