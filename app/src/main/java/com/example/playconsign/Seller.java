package com.example.playconsign;

public class Seller {
    User user;
    String IDNumber;
    String sellerCityAndCountry;
    String shopName;

    public Seller(User user, String IDNumber, String sellerCityAndCountry, String shopName) {
        this.user = user;
        this.IDNumber = IDNumber;
        this.sellerCityAndCountry = sellerCityAndCountry;
        this.shopName = shopName;
    }

    public Seller() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getIDNumber() {
        return IDNumber;
    }

    public void setIDNumber(String IDNumber) {
        this.IDNumber = IDNumber;
    }

    public String getSellerCityAndCountry() {
        return sellerCityAndCountry;
    }

    public void setSellerCityAndCountry(String sellerCityAndCountry) {
        this.sellerCityAndCountry = sellerCityAndCountry;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
}
