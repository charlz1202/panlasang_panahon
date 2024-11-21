package com.example.demo.controller;

import com.example.demo.model.Order;
import com.example.demo.service.OrderService;

import java.util.List;

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
            // Call service to save the order
            Order savedOrder = orderService.placeOrder(order);
            return ResponseEntity.ok().body("{\"message\": \"Order placed successfully\", \"orderId\": " + savedOrder.getId() + "}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"message\": \"Failed to place order: " + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/history")
    public ResponseEntity<?> getOrderHistory(@RequestHeader("Authorization") String token) {
        if (!isValidToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"message\": \"User is not authenticated\"}");
        }

        try {
            // Extract user ID from the token (you may need to decode the token to get the user info)
            Long userId = getUserIdFromToken(token); // Implement this method as per your auth logic

            // Fetch order history for the user
            List<Order> orders = orderService.getOrdersByUserId(userId);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"message\": \"Failed to fetch order history\"}");
        }
    }


    private boolean isValidToken(String token) {
        return token != null && token.startsWith("Bearer ");
    }

    private Long getUserIdFromToken(String token) {
        try {
            // Token format: "Bearer <userId>"
            String[] parts = token.split(" ");
            if (parts.length == 2 && parts[0].equals("Bearer")) {
                return Long.parseLong(parts[1]); // Parse the user ID from the token
            }
        } catch (Exception e) {
            System.err.println("Invalid token format: " + e.getMessage());
        }
        return null; // Return null if the token is invalid
    }
}

