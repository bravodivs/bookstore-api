package api.bookstore.common.events;

import java.util.UUID;

public record ShipmentCreatedEvent(
        UUID eventId,
        String type,
        UUID orderId,
        String userEmail,
        String trackingId,
        String carrier,
        String status
) {
}
