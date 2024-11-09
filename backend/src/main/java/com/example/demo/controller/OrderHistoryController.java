package com.example.demo.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.model.OrderHistory;
import com.example.demo.service.OrderHistoryService;

@RestController
@RequestMapping("/api/order_history")
public class OrderHistoryController {

    @Autowired
    private OrderHistoryService orderHistoryService;

    // Save a new order history
    @PostMapping
    public ResponseEntity<OrderHistory> saveOrderHistory(@RequestBody OrderHistory orderHistory) {
        OrderHistory savedOrderHistory = orderHistoryService.saveOrderHistory(orderHistory);
        return new ResponseEntity<>(savedOrderHistory, HttpStatus.CREATED);
    }

    // Retrieve order history for a specific user
    @GetMapping("/{userId}")
    public ResponseEntity<?> getOrderHistory(@PathVariable Long userId) {
        List<OrderHistory> orderHistory = orderHistoryService.getOrderHistory(userId);

        if (orderHistory.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                     .body("No order history found for user ID: " + userId);
        }

        return new ResponseEntity<>(orderHistory, HttpStatus.OK);
    }
}