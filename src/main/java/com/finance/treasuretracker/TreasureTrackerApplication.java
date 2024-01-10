package com.finance.treasuretracker;
import javax.swing.*;

import com.finance.treasuretracker.controller.BankController;
import com.finance.treasuretracker.controller.BankServiceImpl;
import com.finance.treasuretracker.controller.BankServiceInterface;
import com.finance.treasuretracker.model.repository.BankRepository;
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

		// Run the Swing UI on the Event Dispatch Thread
		SwingUtilities.invokeLater(() -> {
			// Create and display your Swing UI here, passing the bankController
			MainView.createAndShowGUI(bankController);
		});
	}
}
