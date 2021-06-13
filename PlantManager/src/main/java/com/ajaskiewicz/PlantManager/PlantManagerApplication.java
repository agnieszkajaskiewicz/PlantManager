package com.ajaskiewicz.PlantManager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PlantManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlantManagerApplication.class, args);
	}

}
