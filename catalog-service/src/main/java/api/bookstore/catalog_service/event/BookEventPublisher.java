package api.bookstore.catalog_service.event;

import api.bookstore.common.events.BookCreatedEvent;
import api.bookstore.catalog_service.models.Book;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

/// Kafka is used for asynchronous communication between services
/// When a new book is added, you can send an event (BookCreated) to Kafka instead of calling Notification directly.
/// When an order is placed, publish an OrderPlaced event. Payment picks it up.
@Component
public class BookEventPublisher {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public BookEventPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishBookCreated(Book book) {
        BookCreatedEvent event = new BookCreatedEvent(UUID.randomUUID(),
                "book.created",
                book.getId(),
                book.getIsbn(),
                book.getTitle(),
                book.getPrice());
        kafkaTemplate.send("book-events",event.bookId().toString(), event);
    }
}
