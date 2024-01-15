package com.finance.treasuretracker.model.repository;

import com.finance.treasuretracker.model.Bank;
import com.finance.treasuretracker.model.Dropdown;
import com.finance.treasuretracker.model.DropdownType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DropdownRepository extends JpaRepository<Dropdown, Long> {
    List<Dropdown> findAllByDropdownType(DropdownType dropdownType);
}
