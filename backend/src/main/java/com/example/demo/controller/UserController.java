package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    public ResponseEntity<String> login(@RequestBody User user) {
        if (userService.authenticate(user.getEmail(), user.getPassword())) {
            return ResponseEntity.ok().body("{\"message\": \"Login successful for: " + user.getEmail() + "\"}");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"message\": \"Invalid email or password" + "\"}");
        }
    }
    
}
