package com.finance.treasuretracker.service;

import com.finance.treasuretracker.model.Account;

import java.util.List;

public interface AccountServiceInterface {
    List<Account> getAllAccounts();
    Account getAccountById(Integer id);
    void saveAccount(Account account);
    void updateAccount(Account account);
    void deleteAccount(Integer id);
}
