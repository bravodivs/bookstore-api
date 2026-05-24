package api.bookstore.common.events;

import java.math.BigDecimal;
import java.util.UUID;

public record BookCreatedEvent(
        UUID eventId,
        String type,
        UUID bookId,
        String isbn,
        String title,
        BigDecimal price
) {
}
