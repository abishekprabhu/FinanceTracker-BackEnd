package com.abishek.financeapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
//@EnableCaching
public class FinanceTrackerBackEndApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinanceTrackerBackEndApplication.class, args);
	}

}
