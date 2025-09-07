package api.bookstore.catalog_service.event;

import api.bookstore.catalog_service.models.Book;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
public class BookEventPublisher {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public BookEventPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishBookCreated(Book book) {
        Map<String, Object> event = Map.of(
                "eventId", UUID.randomUUID().toString(),
                "type", "book.created",
                "bookId", book.getId().toString(),
                "isbn", book.getIsbn(),
                "title", book.getTitle(),
                "price", book.getPrice()
        );
        kafkaTemplate.send("book-events", event);
    }
}
