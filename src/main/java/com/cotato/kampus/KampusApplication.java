package com.cotato.kampus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class KampusApplication {

	public static void main(String[] args) {
		SpringApplication.run(KampusApplication.class, args);
	}

}
