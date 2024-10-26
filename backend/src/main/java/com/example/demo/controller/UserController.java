package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        if (userService.authenticate(user.getEmail(), user.getPassword())) {
        	User authenticatedUser = userService.getUserByEmail(user.getEmail());
        	if (authenticatedUser != null) {
        		Map<String, Object> response = new HashMap<>();
	            response.put("fullname", authenticatedUser.getFullname());
	            response.put("email", authenticatedUser.getEmail());
	            response.put("phone", authenticatedUser.getPhone());
	            response.put("location", authenticatedUser.getLocation());  
	            response.put("address", authenticatedUser.getAddress());
	            return ResponseEntity.ok().body(response);
        	} else {
        		return ResponseEntity.notFound().build();
        	}
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"message\": \"Invalid email or password" + "\"}");
        }
    }
    
}
