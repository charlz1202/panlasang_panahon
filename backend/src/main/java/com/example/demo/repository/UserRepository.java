package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.model.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Optional: You can add custom query methods here
	 Optional<User> findByEmail(String email); // Return Optional to handle nulls safe
}

