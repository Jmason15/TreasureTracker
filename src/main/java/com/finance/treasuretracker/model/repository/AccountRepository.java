package com.finance.treasuretracker.model.repository;

import com.finance.treasuretracker.model.Account;
import com.finance.treasuretracker.model.Bank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Integer> {
}
