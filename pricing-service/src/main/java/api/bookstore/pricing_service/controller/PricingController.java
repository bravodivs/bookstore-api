package api.bookstore.pricing_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/pricing")
public class PricingController {

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("status", "up");
    }
}
