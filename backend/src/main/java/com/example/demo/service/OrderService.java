package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Item;
import com.example.demo.model.Order;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.OrderRepository;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ItemRepository itemRepository;

    public Order placeOrder(Order order) throws Exception {
        // Fetch the items from the database to ensure they exist
        List<Long> itemIds = order.getItems().stream().map(Item::getId).toList();
        List<Item> items = itemRepository.findByIdIn(itemIds);

        if (items.isEmpty()) {
            throw new Exception("No valid items found for the order.");
        }

        // Link the fetched items to the order
        order.setItems(items);

        // Set the current timestamp for the order
        order.setDatetime(LocalDateTime.now());

        // Save the order to the database
        return orderRepository.save(order);
    }

    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }
}
