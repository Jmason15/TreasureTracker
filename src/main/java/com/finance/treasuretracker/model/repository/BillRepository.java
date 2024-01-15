package com.finance.treasuretracker.model.repository;

import com.finance.treasuretracker.model.Bank;
import com.finance.treasuretracker.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillRepository extends JpaRepository<Bill, Long> {
}
