package com.prakash.LogFetch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
public class LogFetchApplication {

	public static void main(String[] args) {
		SpringApplication.run(LogFetchApplication.class, args);
	}

}
