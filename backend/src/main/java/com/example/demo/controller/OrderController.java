package com.example.demo.controller;

import com.example.demo.model.Order;
import com.example.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:8081")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<?> placeOrder(@RequestBody Order order, @RequestHeader("Authorization") String token) {
        if (!isValidToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"message\": \"User is not authenticated\"}");
        }
        try {
            Order savedOrder = orderService.placeOrder(order);
            return ResponseEntity.ok().body("{\"message\": \"Order placed successfully\", \"orderId\": " + savedOrder.getId() + "}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\": \"Failed to place order\"}");
        }
    }

    private boolean isValidToken(String token) {
       
        return token != null && token.startsWith("Bearer ");
    }
}
