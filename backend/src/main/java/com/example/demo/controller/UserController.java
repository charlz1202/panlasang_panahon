package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.Restaurant;
import com.example.demo.model.User;
import com.example.demo.service.UserService;

@RestController
@RequestMapping("/api/users")
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
        if (userService.authenticate(user.getEmail(), user.getPassword())) {
        	User authenticatedUser = userService.getUserByEmail(user.getEmail());
        	if (authenticatedUser != null) {
        		Map<String, Object> response = new HashMap<>();
	            response.put("fullname", authenticatedUser.getFullname());
	            response.put("email", authenticatedUser.getEmail());
	            response.put("location", authenticatedUser.getLocation());  // Assuming location is stored in the User object
	            return ResponseEntity.ok().body(response);
        	} else {
        		return ResponseEntity.notFound().build();
        	}
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"message\": \"Invalid email or password" + "\"}");
        }
    }
    
}
