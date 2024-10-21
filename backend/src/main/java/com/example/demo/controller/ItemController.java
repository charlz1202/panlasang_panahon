package com.example.demo.controller;

import com.example.demo.model.Item;
import com.example.demo.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    @Autowired
    private ItemService itemService;

    // Get all items
    @GetMapping
    public List<Item> getAllItems() {
        return itemService.getAllItems();
    }

    // Get item by ID
    @GetMapping("/{id}")
    public ResponseEntity<Item> getItemById(@PathVariable Long id) {
        Optional<Item> item = itemService.getItemById(id);
        return item.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Get items by restaurant ID
    @GetMapping("/restaurant/{restaurantId}")
    public List<Item> getItemsByRestaurant(@PathVariable Long restaurantId) {
        return itemService.getItemsByRestaurant(restaurantId);
    }

    /*/ Create a new item
    @PostMapping
    public ResponseEntity<Item> createItem(@RequestBody Item item) {
        Item savedItem = itemService.saveItem(item);
        return ResponseEntity.ok(savedItem);
    }*/
    
    @PostMapping("/save")
    public ResponseEntity<Item> saveItem(@RequestBody Item item) {
        try {
            Item savedItem = itemService.saveItem(item);
            return ResponseEntity.ok(savedItem);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    
    @PostMapping("/upload")
    public ResponseEntity<List<Item>> uploadItems(@RequestBody List<Item> items) {
        try {
            List<Item> savedItems = itemService.saveItems(items);
            return ResponseEntity.ok(savedItems);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    
    /*
    // Endpoint for bulk uploading items
    @PostMapping("/upload")
    public ResponseEntity<String> uploadItems(@RequestBody List<Item> items) {
        try {
        	for (Item item : items) {
                if (item.getRestaurant() != null) {
                    System.out.println("Uploading item for restaurant: " + item.getRestaurant().getId());
                } else {
                    System.out.println("No restaurant for item: " + item.getName());
                }
            }
        	
            itemService.saveItems(items);
            return ResponseEntity.ok("Items uploaded successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error uploading items: " + e.getMessage());
        }
    }*/
    
    // Endpoint to delete all restaurants from HAL
    @DeleteMapping
    public ResponseEntity<String> deleteAllRestaurants() {
        itemService.deleteAllItems();
        return ResponseEntity.ok("All items deleted successfully.");
    }
}
