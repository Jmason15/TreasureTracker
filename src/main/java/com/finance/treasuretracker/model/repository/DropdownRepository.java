package com.finance.treasuretracker.model.repository;

import com.finance.treasuretracker.model.Bank;
import com.finance.treasuretracker.model.Dropdown;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DropdownRepository extends JpaRepository<Dropdown, Long> {
}
