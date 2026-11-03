
package com.devops.order.controller;

import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @GetMapping
    public Map<String, Object> getOrders() {
        return Map.of(
            "orderId", 101,
            "productId", 1,
            "quantity", 2,
            "status", "CONFIRMED"
        );
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of(
            "status", "UP",
            "service", "order-service"
        );
    }
}
