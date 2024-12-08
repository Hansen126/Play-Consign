package com.example.playconsign;

public class Seller {
    String IDNumber;
    String sellerCityAndCountry;
    String shopName;

    public Seller() {
    }

    public Seller(User user, String IDNumber, String sellerCityAndCountry, String shopName) {
        this.IDNumber = IDNumber;
        this.sellerCityAndCountry = sellerCityAndCountry;
        this.shopName = shopName;
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
