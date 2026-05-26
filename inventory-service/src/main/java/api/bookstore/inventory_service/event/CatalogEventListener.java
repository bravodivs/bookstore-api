package api.bookstore.inventory_service.event;

import api.bookstore.common.events.BookCreatedEvent;
import api.bookstore.inventory_service.services.InventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class CatalogEventListener {
    private final InventoryService inventoryService;
    private static final Logger logger = LoggerFactory.getLogger(CatalogEventListener.class);

    public CatalogEventListener(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @KafkaListener(topics = "book-events", groupId = "inventory-group")
    public void consume(BookCreatedEvent event) {
        if ("book.created".equals(event.type())) {
            inventoryService.addStock(event.bookId(), 0);
            logger.info("Initialized inventory for new book: {}", event.title());
        }
    }
}
