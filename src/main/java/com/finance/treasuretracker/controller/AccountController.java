package com.finance.treasuretracker.controller;

import com.finance.treasuretracker.service.AccountServiceInterface;
import com.finance.treasuretracker.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountServiceInterface accountService;

    @Autowired
    public AccountController(AccountServiceInterface accountService) {
        this.accountService = accountService;
    }

    public List<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    public Account getAccountById(@PathVariable Integer id) {
        return accountService.getAccountById(id);
    }

    public void createAccount(@RequestBody Account account) {
        accountService.saveAccount(account);
    }

    public void saveAccount( @RequestBody Account account) {
        accountService.updateAccount(account);
    }

    public void deleteAccount(@PathVariable Integer id) {
        accountService.deleteAccount(id);
    }
}

