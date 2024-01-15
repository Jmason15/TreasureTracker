package com.finance.treasuretracker.service;

import com.finance.treasuretracker.model.Bill;

import java.util.List;

public interface BillServiceInterface {
    List<Bill> getAllBills();

    Bill getBillById(Long id);

    Bill createBill(Bill bill);

    Bill updateBill(Bill bill);

    void deleteBill(Long id);

    void correctTransations();

}
