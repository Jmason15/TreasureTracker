package com.finance.treasuretracker.controller;

import com.finance.treasuretracker.service.BankRecordServiceInterface;
import com.finance.treasuretracker.model.BankRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bankrecords")
public class BankRecordController {

    private final BankRecordServiceInterface bankRecordService;

    @Autowired
    public BankRecordController(BankRecordServiceInterface bankRecordService) {
        this.bankRecordService = bankRecordService;
    }

    @GetMapping
    public List<BankRecord> getAllBankRecords() {
        return bankRecordService.getAllBankRecords();
    }

    @GetMapping("/{id}")
    public BankRecord getBankRecordById(@PathVariable Integer id) {
        return bankRecordService.getBankRecordById(id);
    }

    @PostMapping
    public void createBankRecord(@RequestBody BankRecord bankRecord) {
        bankRecordService.saveBankRecord(bankRecord);
    }

    @PutMapping
    public void updateBankRecord(@RequestBody BankRecord bankRecord) {
        bankRecordService.saveBankRecord(bankRecord);
    }

    @DeleteMapping("/{id}")
    public void deleteBankRecord(@PathVariable Integer id) {
        bankRecordService.deleteBankRecord(id);
    }

    public List<BankRecord> getCurrentAccountFunds() {
        return bankRecordService.getCurrentAccountFunds();
    }

    public List<BankRecord> saveAll(List<BankRecord> tosave) {
        return bankRecordService.saveAll(tosave);
    }
}

