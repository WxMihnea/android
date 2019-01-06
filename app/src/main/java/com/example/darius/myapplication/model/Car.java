package com.example.darius.myapplication.model;

public class Car {
    private String id;
    private String brand;
    private String model;
    private String userEmail;

    public Car() {
    }

    public Car(String id, String brand, String model, String email) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.userEmail = email;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
