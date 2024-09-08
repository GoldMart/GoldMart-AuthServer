package com.goldmarket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class GoldMarketApplication {

	public static void main(String[] args) {
		SpringApplication.run(GoldMarketApplication.class, args);
	}

}
