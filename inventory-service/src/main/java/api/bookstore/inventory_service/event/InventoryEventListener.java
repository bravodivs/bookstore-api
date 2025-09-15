package api.bookstore.inventory_service.event;

import api.bookstore.inventory_service.services.InventoryService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class InventoryEventListener {
    private final InventoryService service;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public InventoryEventListener(InventoryService service, KafkaTemplate<String, Object> kafkaTemplate) {
        this.service = service;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "payment-events", groupId = "inventory-service")
    public void handlePaymentSuccessful(PaymentSuccessfulEvent event) {
        service.reduceStock(event.productId(), event.quantity());
        kafkaTemplate.send("inventory-events",
                event.productId().toString(),
                new StockReducedEvent(event.orderId(), event.productId(), event.quantity()));
    }
}
