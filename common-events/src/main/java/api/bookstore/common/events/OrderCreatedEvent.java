package api.bookstore.common.events;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderCreatedEvent(
        UUID eventId,
        String type,
        UUID orderId,
        UUID bookId,
        int quantity,
        BigDecimal unitPrice,
        BigDecimal totalAmount,
        String userEmail
) {
}
