package com.example.demo.service;

import com.example.demo.model.Item;
import com.example.demo.model.Restaurant;
import com.example.demo.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    // Save a new restaurant
    public Restaurant saveRestaurant(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    // Get all restaurants
    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }
    
    // Find a restaurant by id
    public Restaurant getRestaurantById(Long id) {
        return restaurantRepository.findById(id).orElse(null);
    }
    
    // Save multiple restaurants in bulk
    public List<Restaurant> saveRestaurants(List<Restaurant> restaurants) {
        return restaurantRepository.saveAll(restaurants);
    }
    
    // Method to delete all restaurants
    public void deleteAllRestaurants() {
        restaurantRepository.deleteAll();  // Deletes all restaurants from the database
    }
    
    // Other business logic methods (e.g., update, delete) can be added here
    
}
