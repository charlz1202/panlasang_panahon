package com.example.demo.service;

import com.example.demo.model.Item;
import com.example.demo.model.Restaurant;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;
    
    @Autowired
    private RestaurantRepository restaurantRepository;

    // Save an item
    public Item saveItem(Item item, Long restaurantId) {
        // Fetch the related Restaurant using its ID
        Optional<Restaurant> restaurantOptional = restaurantRepository.findById(restaurantId);
        if (restaurantOptional.isPresent()) {
            item.setRestaurant(restaurantOptional.get()); // Set the restaurant in the item
            return itemRepository.save(item); // Save the item
        } else {
            throw new RuntimeException("Restaurant not found");
        }
    }

    // Get all items
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    // Get item by ID
    public Optional<Item> getItemById(Long id) {
        return itemRepository.findById(id);
    }

    // Get items by restaurant
    public List<Item> getItemsByRestaurant(Long restaurantId) {
        return itemRepository.findAll().stream()
                .filter(item -> item.getRestaurant().getId().equals(restaurantId))
                .toList();
    }
    
    public List<Item> saveItems(List<Item> items) {
        for (Item item : items) {
            // Find the Restaurant by its ID (this assumes the ID is already provided in the CSV)
            Long restaurantId = item.getRestaurant().getId();
            Optional<Restaurant> restaurantOptional = restaurantRepository.findById(restaurantId);
            if (restaurantOptional.isPresent()) {
                item.setRestaurant(restaurantOptional.get()); // Set the restaurant
            } else {
                throw new RuntimeException("Restaurant not found with ID: " + restaurantId);
            }
        }
        return itemRepository.saveAll(items); // Bulk save all items
    }
    
    // Method to delete all items
    public void deleteAllItems() {
        itemRepository.deleteAll();  // Deletes all restaurants from the database
    }
}
