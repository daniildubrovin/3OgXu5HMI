package com.example.OrderService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class OrderServiceApplication {

	public static void main(String[] args) {
		log.info("start service");
		SpringApplication.run(OrderServiceApplication.class, args);
	}
}
