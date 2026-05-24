package api.bookstore.inventory_service.event;

import api.bookstore.common.events.OrderCreatedEvent;
import api.bookstore.common.events.PaymentProcessedEvent;
import api.bookstore.common.events.StockReservedEvent;
import api.bookstore.inventory_service.services.InventoryService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class InventoryEventListener {
    private final InventoryService service;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public InventoryEventListener(InventoryService service, KafkaTemplate<String, Object> kafkaTemplate) {
        this.service = service;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "order-events", groupId = "inventory-group")
    public void handleOrderCreated(OrderCreatedEvent event) {
        boolean reserved = service.reserveStock(event.bookId(), event.quantity());
        StockReservedEvent reservedEvent = new StockReservedEvent(
                UUID.randomUUID(),
                "stock.reserved",
                event.orderId(),
                event.bookId(),
                event.quantity(),
                reserved,
                event.totalAmount(),
                event.userEmail()
        );
        kafkaTemplate.send("inventory-events", event.orderId().toString(), reservedEvent);
    }

    @KafkaListener(topics = "payment-events", groupId = "inventory-group")
    public void handlePaymentProcessed(PaymentProcessedEvent event) {
        if (event.success()) {
            service.finalizeReservation(event.bookId(), event.quantity());
            return;
        }
        service.releaseReservation(event.bookId(), event.quantity());
    }
}
