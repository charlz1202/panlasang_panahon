package com.example.demo.controller;

import com.example.demo.model.Restaurant;
import com.example.demo.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/restaurants") // comment this when enabling the delete mapping
// @RequestMapping("/restaurants") // Uncomment to this when to delete all and the delete mapping
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    // Get all restaurants
    @GetMapping
    public List<Restaurant> getAllRestaurants() {
        return restaurantService.getAllRestaurants();
    }
    
    // Endpoint to create a new restaurant
    @PostMapping
    public ResponseEntity<Restaurant> createRestaurant(@Valid @RequestBody Restaurant restaurant) {
        Restaurant savedRestaurant = restaurantService.saveRestaurant(restaurant);
        return ResponseEntity.ok(savedRestaurant);
    }

    
 // Endpoint to get a restaurant by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getRestaurant(@PathVariable Long id) {
        Restaurant restaurant = restaurantService.getRestaurantById(id);
        if (restaurant != null) {
        	Map<String, Object> response = new HashMap<>();
        	response.put("id", restaurant.getId());
        	response.put("name", restaurant.getName());
            response.put("address", restaurant.getFullAddress());
            response.put("phone", restaurant.getPhone());
            response.put("location", restaurant.getLocation());  
            return ResponseEntity.ok().body(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    // For uploading Restaurant data
    @PostMapping("/upload")
    public ResponseEntity<String> uploadRestaurants(@RequestBody List<Restaurant> restaurants) {
        try {
            restaurantService.saveRestaurants(restaurants);
            return ResponseEntity.ok("Restaurants uploaded successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to upload restaurants: " + e.getMessage());
        }
    }
    
    // Endpoint to delete all restaurants from HAL
    @DeleteMapping
    public ResponseEntity<String> deleteAllRestaurants() {
        restaurantService.deleteAllRestaurants();
        return ResponseEntity.ok("All restaurants deleted successfully.");
    }
	
    
}
