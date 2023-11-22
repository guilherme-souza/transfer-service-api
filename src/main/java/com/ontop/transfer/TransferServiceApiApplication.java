package com.ontop.transfer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class TransferServiceApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(TransferServiceApiApplication.class, args);
	}

}
