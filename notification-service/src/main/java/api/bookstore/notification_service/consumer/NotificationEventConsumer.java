package api.bookstore.notification_service.consumer;

import api.bookstore.notification_service.models.OrderConfirmedEvent;
import api.bookstore.notification_service.models.PaymentSuccessEvent;
import api.bookstore.notification_service.service.EmailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;

public class NotificationEventConsumer {
    private final EmailService emailService;
    private final ObjectMapper objectMapper;

    public NotificationEventConsumer(EmailService emailService, ObjectMapper objectMapper) {
        this.emailService = emailService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "order-confirmed", groupId = "notification-service")
    public void consumeOrderConfirmed(String message) throws Exception {
        OrderConfirmedEvent event = objectMapper.readValue(message, OrderConfirmedEvent.class);
        emailService.sendMail(
                event.getUserEmail(),
                "Order Confirmed - " + event.getOrderId(),
                "Hi, your order for " + event.getBookTitle() + " has been confirmed. Amount: " + event.getAmount()
        );
    }

    @KafkaListener(topics = "payment-success", groupId = "notification-service")
    public void consumePaymentSuccess(String message) throws Exception {
        PaymentSuccessEvent event = objectMapper.readValue(message, PaymentSuccessEvent.class);
        emailService.sendMail(
                event.getUserEmail(),
                "Payment Successful - " + event.getPaymentId(),
                "Hi, your payment of " + event.getAmount() + " for order " + event.getOrderId() + " was successful."
        );
    }
}
