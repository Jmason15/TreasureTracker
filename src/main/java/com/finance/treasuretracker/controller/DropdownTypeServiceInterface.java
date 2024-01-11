package com.finance.treasuretracker.controller;

import com.finance.treasuretracker.model.DropdownType;

import java.util.List;

public interface DropdownTypeServiceInterface {

    List<DropdownType> getAllDropdownTypes();

    DropdownType getDropdownTypeById(Long id);

    void saveDropdownType(DropdownType dropdownType);

    void updateDropdownType(DropdownType dropdownType);

    void deleteDropdownType(Long id);
}

