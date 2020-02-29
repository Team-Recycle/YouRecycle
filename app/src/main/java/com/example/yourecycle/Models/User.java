package com.example.yourecycle.Models;

import java.util.HashMap;
import java.util.Map;

public class User {

    private String name,
            email,
            phone,
            type;

    private Map<String, Object> properties;


    public User(String name, String email, String phone, String type, Map<String, Object> properties) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.type = type;
        this.properties = properties;
    }

    public User(String name, String email, String phone, String type) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.type = type;
        this.properties = new HashMap<>();
    }

    public User() {
        this.properties = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }
}
