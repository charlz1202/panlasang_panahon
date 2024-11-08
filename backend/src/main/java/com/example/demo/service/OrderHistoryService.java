package com.example.demo.service;

import com.example.demo.model.Order;
import com.example.demo.repository.OrderHistoryRepository;  // Your repository for history
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderHistoryService {

    @Autowired
    private OrderHistoryRepository orderHistoryRepository;  // Repository for order history

    public Order saveOrderHistory(Order order) {
        
        return orderHistoryRepository.save(order);
    }
}
