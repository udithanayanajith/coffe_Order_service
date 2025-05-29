package com.assesment.coffee.Process.Order.Service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ProcessOrderServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProcessOrderServiceApplication.class, args);
	}

}
