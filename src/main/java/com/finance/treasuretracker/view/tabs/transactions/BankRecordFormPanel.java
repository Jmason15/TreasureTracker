package com.finance.treasuretracker.view.tabs.transactions;

import com.finance.treasuretracker.controller.AccountController;
import com.finance.treasuretracker.controller.BankRecordController;
import com.finance.treasuretracker.model.Account;
import com.finance.treasuretracker.model.BankRecord;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BankRecordFormPanel extends JPanel {
    private final List<JTextField> amountFields = new ArrayList<>();
    public BankRecordFormPanel(BankRecordController bankRecordController, AccountController accountController) {

        List<BankRecord> bankRecordList = bankRecordController.getCurrentAccountFunds();
        if (bankRecordList.isEmpty()) {
            List<Account> accountList = accountController.getAllAccounts();
            List<BankRecord> toSave = new ArrayList<>();
            for(Account account: accountList){
                BankRecord newRecord = new BankRecord();
                newRecord.setAccount(account);
                newRecord.setAmount(0.0);
                newRecord.setDate(new Date());
                toSave.add(newRecord);
            }
            try{
                bankRecordController.saveAll(toSave);
                bankRecordList = bankRecordController.getCurrentAccountFunds();
            }catch (Exception e){
                System.out.println(e);
            }
        }

        setLayout(new GridLayout(0, 2)); // Adjust layout as needed

        for(BankRecord bankRecord: bankRecordList){
            add(new JLabel(bankRecord.getAccount().getDisplayName()));
            JTextField amountField = new JTextField(bankRecord.getAmount().toString(), 10);
            amountFields.add(amountField);
            add(amountField);
        }

        JButton saveButton = new JButton("Save");
        List<BankRecord> finalBankRecordList = bankRecordList;
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle save action
                for (int i = 0; i < finalBankRecordList.size(); i++) {
                    BankRecord bankRecord = finalBankRecordList.get(i);
                    JTextField amountField = amountFields.get(i);
                    try {
                        double newAmount = Double.parseDouble(amountField.getText());
                        bankRecord.setAmount(newAmount);
                        // Optionally, update the bank record in the database here
                        // bankRecordController.saveBankRecord(bankRecord);
                    } catch (NumberFormatException ex) {
                        System.err.println("Invalid number format for bank record amount");
                    }
                }
            }
        });
        add(saveButton);
    }
}
