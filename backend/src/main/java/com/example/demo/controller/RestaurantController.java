package com.example.demo.controller;

import com.example.demo.model.Restaurant;
import com.example.demo.service.RestaurantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    private static final Logger logger = LoggerFactory.getLogger(RestaurantController.class);

    @Autowired
    private RestaurantService restaurantService;

    // List all restaurants
    @GetMapping
    public List<Restaurant> getAllRestaurants() {
        return restaurantService.getAllRestaurants();
    }

    // Get restaurant by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getRestaurant(@PathVariable("id") Long id) {
        Restaurant restaurant = restaurantService.getRestaurantById(id);
        if (restaurant != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("id", restaurant.getId());
            response.put("name", restaurant.getName());
            response.put("address", restaurant.getFullAddress());
            response.put("phone", restaurant.getPhone());
            response.put("location", restaurant.getCity());
            return ResponseEntity.ok().body(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Upload CSV file for Restaurants
    @PostMapping("/upload")
    public ResponseEntity<String> uploadRestaurants(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty() || !file.getOriginalFilename().endsWith(".csv")) {
            return ResponseEntity.badRequest().body("Please upload a valid CSV file.");
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            List<Restaurant> restaurantList = new ArrayList<>();

            // Skip header
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");

                if (data.length < 7) {
                    return ResponseEntity.badRequest().body("CSV file is missing required columns.");
                }

                // Create the restaurant object using the correct indices
                Restaurant restaurant = new Restaurant(
                    data[1].trim(), // Name
                    data[2].trim(), // Address
                    data[3].trim(), // City
                    data[4].trim(), // Province
                    data[5].trim(), // Postal Code
                    data[6].trim()  // Phone
                );

                restaurantList.add(restaurant);
            }

            restaurantService.saveRestaurants(restaurantList);
            return ResponseEntity.ok("Restaurants uploaded successfully!");
        } catch (Exception e) {
            logger.error("Error uploading restaurants: ", e);
            return ResponseEntity.status(500).body("Failed to upload restaurants: " + e.getMessage());
        }
    }
}