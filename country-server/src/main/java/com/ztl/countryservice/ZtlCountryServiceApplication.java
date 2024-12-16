package com.ztl.countryservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ZtlCountryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZtlCountryServiceApplication.class, args);
	}

}
