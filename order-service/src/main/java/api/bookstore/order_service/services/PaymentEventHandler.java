package api.bookstore.order_service.services;

import api.bookstore.order_service.events.PaymentProcessedEvent;
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
            if (event.success()) {
                order.setOrderStatus(OrderStatus.PAID);
            } else {
                order.setOrderStatus(OrderStatus.PAYMENT_FAILED);
            }
            orderRepository.save(OrderUtil.toDao(order));
            System.out.println("Order updated after payment: " + order);
        }
    }
}
