package api.bookstore.inventory_service.event;

import api.bookstore.inventory_service.models.Inventory;
import api.bookstore.inventory_service.models.InventoryDTO;
import api.bookstore.inventory_service.repositories.InventoryRepository;
import api.bookstore.inventory_service.services.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class CatalogEventListener {
    @Autowired
    private final InventoryService inventoryService;

    public record BookCreatedEvent(
            UUID eventId,
            String type,
            UUID bookId,
            String isbn,
            String title,
            BigDecimal price
    ) {}

    public CatalogEventListener(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @KafkaListener(topics = "book-events", groupId = "inventory-group")
    public void consume(BookCreatedEvent event) {
        if ("book.created".equals(event.type())) {
            InventoryDTO inventory = new InventoryDTO();
            inventory.setBookId(event.bookId());
            inventory.setAvailableQuantity(0); // start with 0

//            inventoryService.addStock(inventory);
            inventoryService.addStock(event.bookId(), 0);
            System.out.println("Initialized inventory for new book: " + event.title());
        }
    }
}
