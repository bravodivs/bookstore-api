package api.bookstore.inventory_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class InventoryServiceApplication {
/// tracks stock availability for books and keeps other services informed.
	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}

}
