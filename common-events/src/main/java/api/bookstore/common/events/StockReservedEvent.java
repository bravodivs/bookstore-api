package api.bookstore.common.events;

import java.math.BigDecimal;
import java.util.UUID;

public record StockReservedEvent(
        UUID eventId,
        String type,
        UUID orderId,
        UUID bookId,
        int quantity,
        boolean reserved,
        BigDecimal amount,
        String userEmail
) {
}
