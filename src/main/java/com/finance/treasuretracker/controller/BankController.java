package com.finance.treasuretracker.controller;

import com.finance.treasuretracker.model.Bank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class BankController {
   private final BankServiceInterface bankService;

   @Autowired
   public BankController(BankServiceInterface bankService) {
      this.bankService = bankService;
   }

   public List<Bank> getAllBanks() {
      return bankService.getAllBanks();
   }

   public void saveBank(Bank bank){
      try{
         bankService.saveBank(bank);
      } catch (Exception e){

      }
   }

   public Bank getById(Long id){
      return bankService.getById(id);
   }

   public void deleteBank(Bank bank) {
       bankService.delete(bank);
   }
}