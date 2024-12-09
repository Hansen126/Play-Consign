package com.example.playconsign;

public class Seller {
    String IDNumber;
    String sellerDomicile;
    String shopName;

    public Seller() {
    }

    public Seller(String IDNumber, String sellerDomicile, String shopName) {
        this.IDNumber = IDNumber;
        this.sellerDomicile = sellerDomicile;
        this.shopName = shopName;
    }

    public String getIDNumber() {
        return IDNumber;
    }

    public void setIDNumber(String IDNumber) {
        this.IDNumber = IDNumber;
    }

    public String getSellerDomicile() {
        return sellerDomicile;
    }

    public void setSellerDomicile(String sellerDomicile) {
        this.sellerDomicile = sellerDomicile;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
}
