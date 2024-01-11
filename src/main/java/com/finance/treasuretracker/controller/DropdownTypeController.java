package com.finance.treasuretracker.controller;

import com.finance.treasuretracker.model.DropdownType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dropdowntypes")
public class DropdownTypeController {

    private final DropdownTypeServiceInterface dropdownTypeService;

    @Autowired
    public DropdownTypeController(DropdownTypeServiceInterface dropdownTypeService) {
        this.dropdownTypeService = dropdownTypeService;
    }

    @GetMapping("/all")
    public List<DropdownType> getAllDropdownTypes() {
        return dropdownTypeService.getAllDropdownTypes();
    }

    @GetMapping("/{id}")
    public DropdownType getDropdownTypeById(@PathVariable Long id) {
        return dropdownTypeService.getDropdownTypeById(id);
    }

    @PostMapping("/create")
    public void createDropdownType(@RequestBody DropdownType dropdownType) {
        dropdownTypeService.saveDropdownType(dropdownType);
    }

    @PutMapping("/update")
    public void saveDropdownType(@RequestBody DropdownType dropdownType) {
        dropdownTypeService.updateDropdownType(dropdownType);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteDropdownType(@PathVariable Long id) {
        dropdownTypeService.deleteDropdownType(id);
    }
}

