package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "restaurants")
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Restaurant name is mandatory")
    private String name;
    
    @NotBlank(message = "Address is mandatory")
    private String address;
    
    @NotBlank(message = "City is mandatory")
    private String city;
    
    @NotBlank(message = "Province is mandatory")
    private String province;
    
    @NotBlank(message = "Postal code is mandatory")
    private String postalcode;
    
    // Phone number is now just a simple string without validation
    private String phone;

    // No-argument constructor for JPA
    public Restaurant() {
    }

    // Parameterized Constructor
    public Restaurant(String name, String address, String city, String province, String postalcode, String phone) {
        this.name = name;
        this.address = address;
        this.city = city;
        this.province = province;
        this.postalcode = postalcode;
        this.phone = phone;
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }    

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getPostalcode() {
        return postalcode;
    }

    public void setPostalcode(String postalcode) {
        this.postalcode = postalcode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
       
    public String getFullAddress() {
        return address + ", " + city + ", " + province + ", " + postalcode;
    }
}