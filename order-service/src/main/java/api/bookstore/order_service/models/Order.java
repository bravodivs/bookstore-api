package api.bookstore.order_service.models;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID bookId;
    private int quantity;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
}