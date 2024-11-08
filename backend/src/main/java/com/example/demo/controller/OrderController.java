package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Order;
import com.example.demo.service.OrderHistoryService;
import com.example.demo.service.OrderService;

@CrossOrigin(origins = "http://localhost:8081") 
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderHistoryService orderHistoryService;  // Service to save order history



    // Endpoint to create a new order
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        try {
            System.out.println("Received order: " + order);
            Order savedOrder = orderService.saveOrder(order);
            return ResponseEntity.ok(savedOrder);
        } catch (Exception e) {
            e.printStackTrace();  // Log the error
            return ResponseEntity.badRequest().body(null);  // Return a 400 Bad Request
        }
    }

    // POST for saving order history
    @CrossOrigin(origins = "http://localhost:8081")
    @PostMapping("/order-history")
    public ResponseEntity<String> saveOrderHistory(@RequestBody Order order) {
        orderService.saveOrder(order);
        return ResponseEntity.ok("Order saved to history!");
    }



    // Endpoint to get an order by ID
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable Long id) {
        Optional<Order> order = orderService.getOrderById(id);
        return order.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Endpoint to get all orders
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    // Endpoint to delete an order by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.ok("Order deleted successfully.");
    }

    // Endpoint to delete all orders
    @DeleteMapping
    public ResponseEntity<String> deleteAllOrders() {
        orderService.deleteAllOrders();
        return ResponseEntity.ok("All orders deleted successfully.");
    }
}
