package api.bookstore.order_service.models;

public enum OrderStatus {
    PENDING,
    CONFIRMED,
    CANCELLED,
    PAID,
    PAYMENT_FAILED
}