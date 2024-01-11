package com.finance.treasuretracker.model.repository;

import com.finance.treasuretracker.model.Bank;
import com.finance.treasuretracker.model.DropdownType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DropdownTypeRepository extends JpaRepository<DropdownType, Long> {
}
