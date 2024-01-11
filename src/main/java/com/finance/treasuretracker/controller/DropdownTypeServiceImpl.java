package com.finance.treasuretracker.controller;

import com.finance.treasuretracker.model.DropdownType;
import com.finance.treasuretracker.model.repository.DropdownTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DropdownTypeServiceImpl implements DropdownTypeServiceInterface {

    private final DropdownTypeRepository dropdownTypeRepository;

    @Autowired
    public DropdownTypeServiceImpl(DropdownTypeRepository dropdownTypeRepository) {
        this.dropdownTypeRepository = dropdownTypeRepository;
    }

    @Override
    public List<DropdownType> getAllDropdownTypes() {
        return dropdownTypeRepository.findAll();
    }

    @Override
    public DropdownType getDropdownTypeById(Long id) {
        return dropdownTypeRepository.findById(id).orElse(null);
    }

    @Override
    public void saveDropdownType(DropdownType dropdownType) {
        dropdownTypeRepository.save(dropdownType);
    }

    @Override
    public void updateDropdownType(DropdownType dropdownType) {
        dropdownTypeRepository.save(dropdownType);
    }

    @Override
    public void deleteDropdownType(Long id) {
        dropdownTypeRepository.deleteById(id);
    }
}

