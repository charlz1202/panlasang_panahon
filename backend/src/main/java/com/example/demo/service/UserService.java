package com.example.demo.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // Method to register a user
    public User registerUser(User user) throws Exception {
        // Check if email is already registered
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());

        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException("User with this email already exists!");
        }

        // Hash the password
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);

        // Save user to the database
        return userRepository.save(user);
    }

    // Method to authenticate a user during login
    public boolean authenticate(String email, String password) {
        // Log the incoming authentication attempt
        logger.info("Attempting login for email: " + email);

        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Compare provided password with the hashed password in the database
            boolean isMatch = passwordEncoder.matches(password, user.getPassword());
            logger.info("Password Match: " + isMatch);  // Log the result of password comparison
            
            return isMatch;
        } else {
            logger.warn("User with email " + email + " does not exist.");
        }

        return false;  // User not found or password does not match
    }

    // Method to get user by email
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }
    
    // Custom exception for handling cases where a user already exists
    public static class UserAlreadyExistsException extends Exception {
        public UserAlreadyExistsException(String message) {
            super(message);
        }
    }
}
