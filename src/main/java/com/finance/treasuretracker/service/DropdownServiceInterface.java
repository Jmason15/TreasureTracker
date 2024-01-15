package com.finance.treasuretracker.service;

import com.finance.treasuretracker.model.Dropdown;

import java.util.List;

public interface DropdownServiceInterface {

    List<Dropdown> getAllDropdowns();

    List<Dropdown> getAllDropdownsByType(Long typeId);

    Dropdown getDropdownById(Long id);

    void saveDropdown(Dropdown dropdown);

    void updateDropdown(Dropdown dropdown);

    void deleteDropdown(Long id);
}

