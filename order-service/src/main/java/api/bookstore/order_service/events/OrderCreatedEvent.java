package api.bookstore.order_service.events;

import java.util.UUID;

public record OrderCreatedEvent(UUID eventId,
                                String type,
                                UUID orderId,
                                UUID bookId,
                                int quantity) {
}
