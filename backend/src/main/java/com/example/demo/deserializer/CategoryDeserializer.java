package com.example.demo.deserializer; // The package where your deserializer is stored

import com.example.demo.model.Item.Category; // Import the Category enum
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;

// Custom deserializer class for Category enum
public class CategoryDeserializer extends JsonDeserializer<Category> {

    @Override
    public Category deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String value = p.getText().toUpperCase();  // Convert incoming string to uppercase for matching
        try {
            return Category.valueOf(value);  // Match the string with Category enum
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid value for Category: " + value);
        }
    }
}
