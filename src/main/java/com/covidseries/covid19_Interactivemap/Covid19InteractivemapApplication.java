package com.covidseries.covid19_Interactivemap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Covid19InteractivemapApplication {

	public static void main(String[] args) {
		SpringApplication.run(Covid19InteractivemapApplication.class, args);
	}

}
