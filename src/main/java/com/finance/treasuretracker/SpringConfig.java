package com.finance.treasuretracker;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.finance.treasuretracker.model.repository")
@EntityScan("com.finance.treasuretracker.model")
public class SpringConfig {

    // Define any additional beans or configurations if necessary
}
