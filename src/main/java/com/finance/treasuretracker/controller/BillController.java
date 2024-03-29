package com.finance.treasuretracker.controller;


import com.finance.treasuretracker.service.BillServiceInterface;
import com.finance.treasuretracker.model.Bill;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bills")
public class BillController {

    private final BillServiceInterface billService;

    @Autowired
    public BillController(BillServiceInterface billService) {
        this.billService = billService;
    }

    @GetMapping
    public List<Bill> getAllBills() {
        return billService.getAllBills();
    }

    @GetMapping("/{id}")
    public Bill getBillById(@PathVariable Long id) {
        return billService.getBillById(id);
    }

    @PostMapping
    public Bill createBill(@RequestBody Bill bill) {
        return billService.createBill(bill);
    }

    @PutMapping("/{id}")
    public void updateBill(@RequestBody Bill bill) {
        billService.updateBill(bill);
    }

    @DeleteMapping("/{id}")
    public void deleteBill(@PathVariable Long id) {
        billService.deleteBill(id);
    }

    public void correctTransactions() {
        billService.correctTransations();
    }
}

