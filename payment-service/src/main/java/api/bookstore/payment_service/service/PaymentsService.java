package api.bookstore.payment_service.service;

import api.bookstore.payment_service.events.OrderCreatedEvent;
import api.bookstore.payment_service.events.PaymentProcessedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PaymentsService {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public PaymentsService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "order-events", groupId = "payment-group")
    public void handleOrderCreated(OrderCreatedEvent event) {
        System.out.println("Processing payment for Order: " + event.orderId());

        // Dummy logic → always succeed
        boolean success = true;

        PaymentProcessedEvent paymentEvent = new PaymentProcessedEvent(
                UUID.randomUUID(),
                success ? "order.paid" : "order.failed",
                event.orderId(),
                success
        );

        kafkaTemplate.send("payment-events", event.orderId().toString(), paymentEvent);
        System.out.println("Payment event published: " + paymentEvent);
    }
}
