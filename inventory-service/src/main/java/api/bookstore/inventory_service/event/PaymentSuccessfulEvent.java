package api.bookstore.inventory_service.event;

import java.util.UUID;

public record PaymentSuccessfulEvent(UUID orderId, UUID bookId, int quantity) {}

