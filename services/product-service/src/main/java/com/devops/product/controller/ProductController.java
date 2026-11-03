
package com.devops.product.controller;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/products")
public class ProductController {

    @GetMapping
    public List<Map<String, Object>> getProducts() {
        return List.of(
            Map.of("id", 1, "name", "Laptop", "price", 55000),
            Map.of("id", 2, "name", "Mobile", "price", 25000),
            Map.of("id", 3, "name", "Keyboard", "price", 1500)
        );
    }

    @GetMapping("/{id}")
    public Map<String, Object> getProduct(@PathVariable int id) {
        return Map.of(
            "id", id,
            "name", "Laptop",
            "price", 55000
        );
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of(
            "status", "UP",
            "service", "product-service"
        );
    }
}
