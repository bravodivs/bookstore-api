package api.bookstore.order_service.services;

import api.bookstore.order_service.events.OrderCreatedEvent;
import api.bookstore.order_service.models.Order;
import api.bookstore.order_service.models.OrderDTO;
import api.bookstore.order_service.models.OrderStatus;
import api.bookstore.order_service.repositories.OrderRepository;
import api.bookstore.order_service.utils.OrderUtil;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public OrderService(OrderRepository orderRepository, KafkaTemplate<String, Object> kafkaTemplate) {
        this.orderRepository = orderRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    public OrderDTO placeOrder(UUID bookId, int quantity) {
        OrderDTO order = new OrderDTO();
        order.setBookId(bookId);
        order.setQuantity(quantity);
        order.setOrderStatus(OrderStatus.PENDING);
        order = OrderUtil.toDto(orderRepository.save(OrderUtil.toDao(order)));

        OrderCreatedEvent event = new OrderCreatedEvent(
                UUID.randomUUID(),
                "order.created",
                order.getId(),
                bookId,
                quantity
        );
        kafkaTemplate.send("order-events", order.getId().toString(), event);

        return order;
    }
}
