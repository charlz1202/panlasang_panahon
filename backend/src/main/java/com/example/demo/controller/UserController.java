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
@CrossOrigin(origins = "http://localhost:8081") // Allows frontend on localhost:8081
public class UserController {

    @Autowired
    private UserService userService;

    // Registration endpoint
    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@RequestBody User user) {
        try {
            // Register user via the service
            userService.registerUser(user);
            return ResponseEntity.ok().body("{\"message\": \"User registered successfully\"}");
        } catch (Exception e) {
            // Handle error gracefully with a proper response message
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\": \"Failed to register user: " + e.getMessage() + "\"}");
        }
    }

    // Login endpoint
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        try {
            // Log the incoming request for troubleshooting
            System.out.println("Attempting login for email: " + user.getEmail());

            // Authenticate the user via the service
            boolean isAuthenticated = userService.authenticate(user.getEmail(), user.getPassword());

            if (isAuthenticated) {
                // Retrieve the authenticated user from the service
                User authenticatedUser = userService.getUserByEmail(user.getEmail());

                // If the user exists in the database, return user details
                if (authenticatedUser != null) {
                    Map<String, Object> response = new HashMap<>();
                    response.put("id", authenticatedUser.getId());
                    response.put("fullname", authenticatedUser.getFullname());
                    response.put("email", authenticatedUser.getEmail());
                    response.put("phone", authenticatedUser.getPhone());
                    response.put("location", authenticatedUser.getLocation());
                    response.put("address", authenticatedUser.getAddress());

                    // Return user data with OK status
                    return ResponseEntity.ok().body(response);
                } else {
                    // User not found after authentication, return NOT_FOUND
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\": \"User not found in database\"}");
                }
            } else {
                // Authentication failed, invalid email or password
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"message\": \"Invalid email or password\"}");
            }
        } catch (Exception e) {
            // Handle any errors that might occur during login
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"message\": \"Internal server error: " + e.getMessage() + "\"}");
        }
    }
}