package com.example.playconsign;

public class SnapRequest {
    String orderId;
    int amount;
    String name;
    String email;
    String phone;

    public SnapRequest(String orderId, int amount, String name, String email, String phone) {
        this.orderId = orderId;
        this.amount = amount;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
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
}
