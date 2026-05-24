package api.bookstore.order_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class OrderServiceApplication {
/// Accept orders via REST API.
///
/// Save order details in DB.
///
/// Publish order.created event to Kafka.
///
/// Later, listen for payment success events → mark order as CONFIRMED.
	public static void main(String[] args) {
		SpringApplication.run(OrderServiceApplication.class, args);
	}

}
