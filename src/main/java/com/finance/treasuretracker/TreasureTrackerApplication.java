package com.finance.treasuretracker;
import javax.swing.*;

import com.finance.treasuretracker.controller.*;
import com.finance.treasuretracker.model.repository.*;
import com.finance.treasuretracker.service.*;
import com.finance.treasuretracker.view.MainView;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
@SpringBootApplication
public class TreasureTrackerApplication {

	public static void main(String[] args) {
		FlatMacLightLaf.setup();

		// Create a Spring application context
		ConfigurableApplicationContext ctx = new SpringApplicationBuilder(TreasureTrackerApplication.class)
				.headless(false) // allows the AWT/Swing components to be instantiated
				.run(args);

		// Retrieve beans from the context
		BankRepository bankRepository = ctx.getBean(BankRepository.class);
		BankServiceInterface bankService = new BankServiceImpl(bankRepository);
		BankController bankController = new BankController(bankService);

		AccountRepository accountRepository = ctx.getBean(AccountRepository.class);
		AccountServiceInterface accountService = new AccountServiceImpl(accountRepository);
		AccountController accountController = new AccountController(accountService);

// Initialize the Dropdown components


// Initialize the DropdownType components
		DropdownTypeRepository dropdownTypeRepository = ctx.getBean(DropdownTypeRepository.class);
		BankRecordServiceInterface.DropdownTypeServiceInterface dropdownTypeService = new DropdownTypeServiceImpl(dropdownTypeRepository);
		DropdownTypeController dropdownTypeController = new DropdownTypeController(dropdownTypeService);

		DropdownRepository dropdownRepository = ctx.getBean(DropdownRepository.class);
		DropdownServiceInterface dropdownService = new DropdownServiceImpl(dropdownRepository, dropdownTypeRepository);
		DropdownController dropdownController = new DropdownController(dropdownService);

		TransactionRepository transactionRepository = ctx.getBean(TransactionRepository.class);
		TransactionServiceInterface transactionService = new TransactionServiceImpl(transactionRepository);
		TransactionController transactionController = new TransactionController(transactionService);

		BillRepository billRepository = ctx.getBean(BillRepository.class);
		BillServiceInterface billService = new BillServiceImpl(billRepository, transactionRepository);
		BillController billController = new BillController(billService);

		BankRecordRepository bankRecordRepository = ctx.getBean(BankRecordRepository.class);
		BankRecordServiceInterface bankRecordService = new BankRecordServiceImpl(bankRecordRepository);
		BankRecordController bankRecordController = new BankRecordController(bankRecordService);

		SummaryRepository summaryRepository = ctx.getBean(SummaryRepository.class);
		SummaryServiceInterface summaryServiceInterface = new SummaryServiceImpl(summaryRepository);
		SummaryController summaryController = new SummaryController(summaryServiceInterface);




		// Run the Swing UI on the Event Dispatch Thread
		SwingUtilities.invokeLater(() -> {
			// Create and display your Swing UI here, passing the bankController
			MainView.createAndShowGUI(accountController, bankController, dropdownController, dropdownTypeController,
					billController, transactionController, bankRecordController, summaryController);
		});
	}
}
