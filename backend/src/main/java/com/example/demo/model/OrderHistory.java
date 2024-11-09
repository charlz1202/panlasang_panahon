package com.example.demo.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "order_history")
public class OrderHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "weather")
    private Weather weather;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "date_time")
    private String dateTime;

    @ManyToMany
    @JoinTable(
      name = "order_history_items", // Join table name for the many-to-many relation
      joinColumns = @JoinColumn(name = "order_history_id"), // Foreign key for order_history
      inverseJoinColumns = @JoinColumn(name = "item_id")) // Foreign key for item
    private List<Item> items;

    @Column(name = "price")
    private Double price;

    // Default constructor
    public OrderHistory() {
    }

    // Constructor with parameters
    public OrderHistory(Long id, User user, String dateTime, Weather weather, List<Item> items, Double price) {
        this.id = id;
        this.user = user;
        this.dateTime = dateTime;
        this.weather = weather;
        this.items = items;
        this.price = price;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public Weather getWeather() {
        return weather;
    }

    public void setWeather(Weather weather) {
        this.weather = weather;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}