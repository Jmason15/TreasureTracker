package com.finance.treasuretracker.service;

import com.finance.treasuretracker.service.AccountServiceInterface;
import com.finance.treasuretracker.model.Account;
import com.finance.treasuretracker.model.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountServiceInterface {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public Account getAccountById(Integer id) {
        return accountRepository.findById(id).orElse(null);
    }

    @Override
    public void saveAccount(Account account) {
        accountRepository.save(account);
    }

    @Override
    public void updateAccount( Account account) {
            accountRepository.save(account);
    }

    @Override
    public void deleteAccount(Integer id) {
        accountRepository.deleteById(id);
    }
}

