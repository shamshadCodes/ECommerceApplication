package com.scaler.ECommerceProductService;

import com.scaler.ECommerceProductService.service.InitService;
import com.scaler.ECommerceProductService.service.InitServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ECommerceProductServiceApplication implements CommandLineRunner {
	@Autowired
	private InitService initService;

	public static void main(String[] args) {
		SpringApplication.run(ECommerceProductServiceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception{
//		initService.initialize();
	}
}
