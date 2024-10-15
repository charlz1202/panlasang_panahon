package com.example.demo.controller;

import com.example.demo.model.Restaurant;
import com.example.demo.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/restaurants") // comment this when enabling the delete mapping
// @RequestMapping("/restaurants") // Uncomment to this when to delete all and the delete mapping
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    // Endpoint to create a new restaurant
    @PostMapping
    public ResponseEntity<Restaurant> createRestaurant(@Valid @RequestBody Restaurant restaurant) {
        Restaurant savedRestaurant = restaurantService.saveRestaurant(restaurant);
        return ResponseEntity.ok(savedRestaurant);
    }

    // Endpoint to get a restaurant by ID
    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> getRestaurant(@PathVariable Long id) {
        Restaurant restaurant = restaurantService.getRestaurantById(id);
        if (restaurant != null) {
            return ResponseEntity.ok(restaurant);
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
