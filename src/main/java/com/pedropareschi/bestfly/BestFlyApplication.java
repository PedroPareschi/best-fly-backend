package com.pedropareschi.bestfly;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class BestFlyApplication {

	public static void main(String[] args) {
		SpringApplication.run(BestFlyApplication.class, args);
	}

}
