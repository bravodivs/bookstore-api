package api.bookstore.analytics_service.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class AnalyticsEventConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(AnalyticsEventConsumer.class);

    @KafkaListener(topics = {"book-events", "order-events", "inventory-events", "payment-events", "shipping-events"},
            groupId = "analytics-group")
    public void consume(Object event) {
        LOGGER.info("Analytics event received: {}", event);
    }
}
