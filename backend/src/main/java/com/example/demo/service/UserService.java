package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Item;
import com.example.demo.model.Restaurant;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public User registerUser(User user) throws Exception {
        // Check if email is already registered
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        
        if (existingUser.isPresent()) {
            throw new Exception("User with this email already exists!");
        }

        // Hash the password
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        //System.out.println("Hashed Password: " + hashedPassword);  // Log the hashed password
        user.setPassword(hashedPassword);

        // Save user to the database
        return userRepository.save(user);
    }


    public boolean authenticate(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            
            // Log the hashed password stored in the database
            //System.out.println("Stored Hashed Password: " + user.getPassword());
            
            // Compare hashed password with the provided password
            boolean isMatch = passwordEncoder.matches(password, user.getPassword());
            System.out.println("Password Match: " + isMatch);  // Log the result of password comparison
            
            return isMatch;
        } else {
        	System.out.println("User " + email + "does not exist.");
        }
        return false;  // User not found or password does not match
    }
    
    // Get user by email
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

}
