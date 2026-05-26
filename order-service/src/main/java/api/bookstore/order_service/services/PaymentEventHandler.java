package api.bookstore.order_service.services;

import api.bookstore.common.events.PaymentProcessedEvent;
import api.bookstore.common.events.ShipmentCreatedEvent;
import api.bookstore.order_service.models.Order;
import api.bookstore.order_service.models.OrderDTO;
import api.bookstore.order_service.models.OrderStatus;
import api.bookstore.order_service.repositories.OrderRepository;
import api.bookstore.order_service.utils.OrderUtil;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PaymentEventHandler {
    private final OrderRepository orderRepository;

    public PaymentEventHandler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @KafkaListener(topics = "payment-events", groupId = "order-group")
    public void handlePaymentEvent(PaymentProcessedEvent event) {
        Optional<Order> orderOpt = orderRepository.findById(event.orderId());
        if (orderOpt.isPresent()) {
            OrderDTO order = OrderUtil.toDto(orderOpt.get());
            if (order.getOrderStatus() == OrderStatus.CANCELLED || order.getOrderStatus() == OrderStatus.SHIPPED) {
                return;
            }
            if (event.success()) {
                order.setOrderStatus(OrderStatus.PAID);
            } else {
                order.setOrderStatus(OrderStatus.PAYMENT_FAILED);
            }
            orderRepository.save(OrderUtil.toDao(order));
        }
    }

    @KafkaListener(topics = "shipping-events", groupId = "order-group")
    public void handleShipmentEvent(ShipmentCreatedEvent event) {
        orderRepository.findById(event.orderId()).ifPresent(order -> {
            if (order.getOrderStatus() != OrderStatus.PAID) {
                return;
            }
            order.setTrackingId(event.trackingId());
            order.setOrderStatus(OrderStatus.SHIPPED);
            orderRepository.save(order);
        });
    }
}
