package api.bookstore.order_service.controller;

import api.bookstore.order_service.models.Order;
import api.bookstore.order_service.models.OrderDTO;
import api.bookstore.order_service.services.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    private final OrderService orderService;
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/{bookId}/{quantity}")
    public OrderDTO placeOrder(@PathVariable UUID bookId, @PathVariable int quantity) {
        return orderService.placeOrder(bookId, quantity);
    }
}
