package api.bookstore.notification_service.consumer;

import api.bookstore.common.events.OrderCreatedEvent;
import api.bookstore.common.events.PaymentProcessedEvent;
import api.bookstore.common.events.ShipmentCreatedEvent;
import api.bookstore.notification_service.service.EmailService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationEventConsumer {
    private final EmailService emailService;

    public NotificationEventConsumer(EmailService emailService) {
        this.emailService = emailService;
    }

    @KafkaListener(topics = "order-events", groupId = "notification-group")
    public void consumeOrderCreated(OrderCreatedEvent event) {
        if (event.userEmail() == null || event.userEmail().isBlank()) {
            return;
        }
        emailService.sendMail(event.userEmail(),
                "Order Received - " + event.orderId(),
                "Your order is created and awaiting payment.");
    }

    @KafkaListener(topics = "payment-events", groupId = "notification-group")
    public void consumePaymentSuccess(PaymentProcessedEvent event) {
        if (event.userEmail() == null || event.userEmail().isBlank()) {
            return;
        }
        emailService.sendMail(
                event.userEmail(),
                event.success() ? "Payment Successful - " + event.orderId() : "Payment Failed - " + event.orderId(),
                event.success()
                        ? "Payment of " + event.amount() + " was successful. Transaction: " + event.transactionId()
                        : "Payment failed. Please retry checkout."
        );
    }

    @KafkaListener(topics = "shipping-events", groupId = "notification-group")
    public void consumeShipmentCreated(ShipmentCreatedEvent event) {
        if (event.userEmail() == null || event.userEmail().isBlank()) {
            return;
        }
        emailService.sendMail(
                event.userEmail(),
                "Shipment Created - " + event.orderId(),
                "Tracking ID: " + event.trackingId() + " via " + event.carrier()
        );
    }
}
