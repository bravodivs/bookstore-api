package api.bookstore.order_service.models;

public enum OrderStatus {
    CREATED,
    PENDING_PAYMENT,
    PAID,
    SHIPPED,
    PAYMENT_FAILED,
    CANCELLED
}