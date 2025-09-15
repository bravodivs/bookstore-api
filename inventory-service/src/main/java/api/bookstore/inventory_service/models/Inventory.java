package api.bookstore.inventory_service.models;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "inventory")
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private UUID bookId;

    private int availableQuantity;

    //todo not required
    private int reservedQuantity;
}
