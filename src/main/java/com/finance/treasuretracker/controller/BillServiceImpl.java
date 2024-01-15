package com.finance.treasuretracker.controller;

import com.finance.treasuretracker.model.Bill;

import com.finance.treasuretracker.model.repository.BillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BillServiceImpl implements BillServiceInterface{

    private final BillRepository billRepository;

    @Autowired
    public BillServiceImpl(BillRepository billRepository) {
        this.billRepository = billRepository;
    }

    @Override
    public List<Bill> getAllBills() {
        return billRepository.findAll();
    }

    @Override
    public Bill getBillById(Long id) {
        Optional<Bill> optionalBill = billRepository.findById(id);
        return optionalBill.orElse(null);
    }

    @Override
    public Bill createBill(Bill bill) {
        return billRepository.save(bill);
    }

    @Override
    public Bill updateBill(Bill bill) {
        return billRepository.save(bill);
    }

    @Override
    public void deleteBill(Long id) {
        billRepository.deleteById(id);
    }
}
