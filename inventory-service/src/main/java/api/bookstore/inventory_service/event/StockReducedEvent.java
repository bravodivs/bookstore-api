package api.bookstore.inventory_service.event;

import java.util.UUID;

public record StockReducedEvent(UUID orderId, UUID productId, int reducedQty) {}
