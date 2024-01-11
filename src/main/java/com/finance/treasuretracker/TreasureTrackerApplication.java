package com.finance.treasuretracker;
import javax.swing.*;

import com.finance.treasuretracker.controller.*;
import com.finance.treasuretracker.model.repository.AccountRepository;
import com.finance.treasuretracker.model.repository.BankRepository;
import com.finance.treasuretracker.model.repository.DropdownRepository;
import com.finance.treasuretracker.model.repository.DropdownTypeRepository;
import com.finance.treasuretracker.view.MainView;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class TreasureTrackerApplication {

	public static void main(String[] args) {
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
		DropdownRepository dropdownRepository = ctx.getBean(DropdownRepository.class);
		DropdownServiceInterface dropdownService = new DropdownServiceImpl(dropdownRepository);
		DropdownController dropdownController = new DropdownController(dropdownService);

// Initialize the DropdownType components
		DropdownTypeRepository dropdownTypeRepository = ctx.getBean(DropdownTypeRepository.class);
		DropdownTypeServiceInterface dropdownTypeService = new DropdownTypeServiceImpl(dropdownTypeRepository);
		DropdownTypeController dropdownTypeController = new DropdownTypeController(dropdownTypeService);



		// Run the Swing UI on the Event Dispatch Thread
		SwingUtilities.invokeLater(() -> {
			// Create and display your Swing UI here, passing the bankController
			MainView.createAndShowGUI(accountController, bankController, dropdownController, dropdownTypeController);
		});
	}
}
