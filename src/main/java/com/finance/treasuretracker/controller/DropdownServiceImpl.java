package com.finance.treasuretracker.controller;

import com.finance.treasuretracker.model.Dropdown;
import com.finance.treasuretracker.model.repository.DropdownRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DropdownServiceImpl implements DropdownServiceInterface {

    private final DropdownRepository dropdownRepository;

    @Autowired
    public DropdownServiceImpl(DropdownRepository dropdownRepository) {
        this.dropdownRepository = dropdownRepository;
    }

    @Override
    public List<Dropdown> getAllDropdowns() {
        return dropdownRepository.findAll();
    }

    @Override
    public Dropdown getDropdownById(Long id) {
        return dropdownRepository.findById(id).orElse(null);
    }

    @Override
    public void saveDropdown(Dropdown dropdown) {
        dropdownRepository.save(dropdown);
    }

    @Override
    public void updateDropdown(Dropdown dropdown) {
        dropdownRepository.save(dropdown);
    }

    @Override
    public void deleteDropdown(Long id) {
        dropdownRepository.deleteById(id);
    }
}

