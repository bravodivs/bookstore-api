package api.bookstore.payment_service.service;

import api.bookstore.common.events.PaymentProcessedEvent;
import api.bookstore.common.events.StockReservedEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PaymentsService {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final boolean forceFailure;

    public PaymentsService(KafkaTemplate<String, Object> kafkaTemplate,
                           @Value("${payment.force-failure:false}") boolean forceFailure) {
        this.kafkaTemplate = kafkaTemplate;
        this.forceFailure = forceFailure;
    }

    @KafkaListener(topics = "inventory-events", groupId = "payment-group")
    public void handleStockReserved(StockReservedEvent event) {
        if (!event.reserved()) {
            return;
        }
        boolean success = !forceFailure;

        PaymentProcessedEvent paymentEvent = new PaymentProcessedEvent(
                UUID.randomUUID(),
                success ? "payment.succeeded" : "payment.failed",
                event.orderId(),
                event.bookId(),
                event.quantity(),
                event.amount(),
                event.userEmail(),
                success,
                UUID.randomUUID().toString()
        );

        kafkaTemplate.send("payment-events", event.orderId().toString(), paymentEvent);
    }
}
