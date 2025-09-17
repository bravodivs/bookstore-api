package api.bookstore.order_service.events;

import java.util.UUID;

public record PaymentProcessedEvent(
        UUID eventId,
                                    String type,
                                    UUID orderId,
                                    boolean success) {
}
