package com.finance.treasuretracker.controller;

import com.finance.treasuretracker.service.DropdownServiceInterface;
import com.finance.treasuretracker.model.Dropdown;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dropdowns")
public class DropdownController {

    private final DropdownServiceInterface dropdownService;

    @Autowired
    public DropdownController(DropdownServiceInterface dropdownService) {
        this.dropdownService = dropdownService;
    }

    @GetMapping("/all")
    public List<Dropdown> getAllDropdowns() {
        return dropdownService.getAllDropdowns();
    }

    @GetMapping("/byType")
    public List<Dropdown> getAllDropdownsbyType(Long typeId) {
        return dropdownService.getAllDropdownsByType(typeId);
    }

    @GetMapping("/{id}")
    public Dropdown getDropdownById(@PathVariable Long id) {
        return dropdownService.getDropdownById(id);
    }

    @PostMapping("/create")
    public void createDropdown(@RequestBody Dropdown dropdown) {
        dropdownService.saveDropdown(dropdown);
    }

    @PutMapping("/update")
    public void saveDropdown(@RequestBody Dropdown dropdown) {
        dropdownService.updateDropdown(dropdown);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteDropdown(@PathVariable Long id) {
        dropdownService.deleteDropdown(id);
    }
}
