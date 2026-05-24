package api.bookstore.order_service.services;

import api.bookstore.common.events.OrderCreatedEvent;
import api.bookstore.order_service.models.Order;
import api.bookstore.order_service.models.OrderDTO;
import api.bookstore.order_service.models.PlaceOrderRequest;
import api.bookstore.order_service.models.OrderStatus;
import api.bookstore.order_service.repositories.OrderRepository;
import api.bookstore.order_service.utils.OrderUtil;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public OrderService(OrderRepository orderRepository, KafkaTemplate<String, Object> kafkaTemplate) {
        this.orderRepository = orderRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    public OrderDTO placeOrder(PlaceOrderRequest request) {
        if (request.getBookId() == null) {
            throw new IllegalArgumentException("bookId is required");
        }
        if (request.getQuantity() <= 0) {
            throw new IllegalArgumentException("quantity must be greater than zero");
        }

        BigDecimal unitPrice = request.getUnitPrice() == null ? BigDecimal.ZERO : request.getUnitPrice();
        BigDecimal totalAmount = unitPrice.multiply(BigDecimal.valueOf(request.getQuantity()));

        OrderDTO order = new OrderDTO();
        order.setBookId(request.getBookId());
        order.setQuantity(request.getQuantity());
        order.setUnitPrice(unitPrice);
        order.setTotalAmount(totalAmount);
        order.setUserEmail(request.getUserEmail());
        order.setOrderStatus(OrderStatus.PENDING_PAYMENT);
        order = OrderUtil.toDto(orderRepository.save(OrderUtil.toDao(order)));

        OrderCreatedEvent event = new OrderCreatedEvent(
                UUID.randomUUID(),
                "order.created",
                order.getId(),
                order.getBookId(),
                order.getQuantity(),
                order.getUnitPrice(),
                order.getTotalAmount(),
                order.getUserEmail()
        );
        kafkaTemplate.send("order-events", order.getId().toString(), event);

        return order;
    }

    public OrderDTO getOrderById(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        return OrderUtil.toDto(order);
    }

    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(OrderUtil::toDto)
                .toList();
    }
}
