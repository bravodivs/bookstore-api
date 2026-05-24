package api.bookstore.shipping_service.service;

import api.bookstore.common.events.PaymentProcessedEvent;
import api.bookstore.common.events.ShipmentCreatedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ShippingEventHandler {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public ShippingEventHandler(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "payment-events", groupId = "shipping-group")
    public void handlePaymentProcessed(PaymentProcessedEvent event) {
        if (!event.success()) {
            return;
        }

        ShipmentCreatedEvent shipmentCreatedEvent = new ShipmentCreatedEvent(
                UUID.randomUUID(),
                "shipment.created",
                event.orderId(),
                event.userEmail(),
                "TRK-" + event.orderId().toString().substring(0, 8).toUpperCase(),
                "BOOKSTORE_EXPRESS",
                "CREATED"
        );

        kafkaTemplate.send("shipping-events", event.orderId().toString(), shipmentCreatedEvent);
    }
}
