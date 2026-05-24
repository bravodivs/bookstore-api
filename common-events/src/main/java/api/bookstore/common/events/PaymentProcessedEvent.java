package api.bookstore.common.events;

import java.math.BigDecimal;
import java.util.UUID;

public record PaymentProcessedEvent(
        UUID eventId,
        String type,
        UUID orderId,
        UUID bookId,
        int quantity,
        BigDecimal amount,
        String userEmail,
        boolean success,
        String transactionId
) {
}
