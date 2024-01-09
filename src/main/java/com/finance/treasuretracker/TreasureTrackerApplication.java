package com.finance.treasuretracker;
import javax.swing.*;

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

		// Run the Swing UI on the Event Dispatch Thread
		SwingUtilities.invokeLater(() -> {
			// Create and display your Swing UI here
			MainView.createAndShowGUI();
		});


	}

}
