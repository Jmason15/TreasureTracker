package com.finance.treasuretracker.service;

import com.finance.treasuretracker.model.BankRecord;
import com.finance.treasuretracker.model.repository.BankRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankRecordServiceImpl implements BankRecordServiceInterface {

    private final BankRecordRepository bankRecordRepository;

    @Autowired
    public BankRecordServiceImpl(BankRecordRepository bankRecordRepository) {
        this.bankRecordRepository = bankRecordRepository;
    }

    @Override
    public List<BankRecord> getAllBankRecords() {
        return bankRecordRepository.findAll();
    }

    @Override
    public BankRecord getBankRecordById(Integer id) {
        return bankRecordRepository.findById(id).orElse(null);
    }

    @Override
    public void saveBankRecord(BankRecord bankRecord) {
        bankRecordRepository.save(bankRecord);
    }

    @Override
    public void deleteBankRecord(Integer id) {
        bankRecordRepository.deleteById(id);
    }

    @Override
    public List<BankRecord> getCurrentAccountFunds() {
        return bankRecordRepository.getCurrentAccountFunds();
    }

    @Override
    public List<BankRecord> saveAll(List<BankRecord> tosave){
        return bankRecordRepository.saveAll(tosave);
    }
}

