package api.bookstore.order_service.services;

import api.bookstore.common.events.StockReservedEvent;
import api.bookstore.order_service.models.OrderStatus;
import api.bookstore.order_service.repositories.OrderRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class InventoryReservationHandler {
    private final OrderRepository orderRepository;

    public InventoryReservationHandler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @KafkaListener(topics = "inventory-events", groupId = "order-group")
    public void handleStockReservation(StockReservedEvent event) {
        if (event.reserved()) {
            return;
        }
        orderRepository.findById(event.orderId()).ifPresent(order -> {
            order.setOrderStatus(OrderStatus.CANCELLED);
            orderRepository.save(order);
        });
    }
}
