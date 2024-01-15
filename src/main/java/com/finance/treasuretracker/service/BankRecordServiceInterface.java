package com.finance.treasuretracker.service;

import com.finance.treasuretracker.model.BankRecord;
import com.finance.treasuretracker.model.DropdownType;

import java.util.List;

public interface BankRecordServiceInterface {
    List<BankRecord> getAllBankRecords();
    BankRecord getBankRecordById(Integer id);
    void saveBankRecord(BankRecord bankRecord);
    void deleteBankRecord(Integer id);

    List<BankRecord> getCurrentAccountFunds();

    List<BankRecord> saveAll(List<BankRecord> tosave);

    interface DropdownTypeServiceInterface {

        List<DropdownType> getAllDropdownTypes();

        DropdownType getDropdownTypeById(Long id);

        void saveDropdownType(DropdownType dropdownType);

        void updateDropdownType(DropdownType dropdownType);

        void deleteDropdownType(Long id);
    }
}

