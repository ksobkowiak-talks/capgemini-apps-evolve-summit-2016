package com.capgemini.resilience.costcenter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class CostCenterApplication {

	public static void main(String[] args) {
		SpringApplication.run(CostCenterApplication.class, args);
	}
}
