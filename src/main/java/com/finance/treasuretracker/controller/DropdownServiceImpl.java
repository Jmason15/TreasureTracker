package com.finance.treasuretracker.controller;

import com.finance.treasuretracker.model.Dropdown;
import com.finance.treasuretracker.model.DropdownType;
import com.finance.treasuretracker.model.repository.DropdownRepository;
import com.finance.treasuretracker.model.repository.DropdownTypeRepository;
import com.finance.treasuretracker.utils.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DropdownServiceImpl implements DropdownServiceInterface {

    private final DropdownRepository dropdownRepository;
    private final DropdownTypeRepository dropdownTypeRepository;

    @Autowired
    public DropdownServiceImpl(DropdownRepository dropdownRepository, DropdownTypeRepository dropdownTypeRepository) {
        this.dropdownRepository = dropdownRepository;
        this.dropdownTypeRepository = dropdownTypeRepository;
    }

    @Override
    public List<Dropdown> getAllDropdowns() {
        return dropdownRepository.findAll();
    }

    @Override
    public List<Dropdown> getAllDropdownsByType(Long typeId) {
        DropdownType dropdownType = dropdownTypeRepository.findById(typeId)
                .orElseThrow(() -> new NotFoundException("DropdownType with ID " + typeId + " not found"));
        return dropdownRepository.findAllByDropdownType(dropdownType);
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

