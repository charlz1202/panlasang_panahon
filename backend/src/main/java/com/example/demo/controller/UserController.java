package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.User;
import com.example.demo.service.UserService;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:8081")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@RequestBody User user) {
        try {
            userService.registerUser(user);
            return ResponseEntity.ok().body("{\"message\": \"User registered successfully\"}");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("{\"message\": \"Failed to register user: " + e.getMessage() + "\"}");
        }
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        // Log the incoming request for troubleshooting
        System.out.println("Attempting login for email: " + user.getEmail());
    
        // Authenticate the user
        boolean isAuthenticated = userService.authenticate(user.getEmail(), user.getPassword());
        if (isAuthenticated) {
            User authenticatedUser = userService.getUserByEmail(user.getEmail());
            
            // If user is found, return user details
            if (authenticatedUser != null) {
                Map<String, Object> response = new HashMap<>();
                response.put("id", authenticatedUser.getId());
                response.put("fullname", authenticatedUser.getFullname());
                response.put("email", authenticatedUser.getEmail());
                response.put("phone", authenticatedUser.getPhone());
                response.put("location", authenticatedUser.getLocation());
                response.put("address", authenticatedUser.getAddress());
    
                return ResponseEntity.ok().body(response);
            } else {
                // User was not found after authentication, return not found response
                System.out.println("User not found in database.");
                return ResponseEntity.notFound().build();
            }
        } else {
            // Authentication failed due to invalid password
            System.out.println("Invalid email or password for user: " + user.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"message\": \"Invalid email or password\"}");
        }
    }
    
    
}
