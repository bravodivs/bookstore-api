package api.bookstore.inventory_service.event;

import api.bookstore.common.events.BookCreatedEvent;
import api.bookstore.inventory_service.services.InventoryService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class CatalogEventListener {
    private final InventoryService inventoryService;

    public CatalogEventListener(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @KafkaListener(topics = "book-events", groupId = "inventory-group")
    public void consume(BookCreatedEvent event) {
        if ("book.created".equals(event.type())) {
            inventoryService.addStock(event.bookId(), 0);
            System.out.println("Initialized inventory for new book: " + event.title());
        }
    }
}
