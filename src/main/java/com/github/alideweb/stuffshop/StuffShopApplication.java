package com.github.alideweb.stuffshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class StuffShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(StuffShopApplication.class, args);
	}

}
