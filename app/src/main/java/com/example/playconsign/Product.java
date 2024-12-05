package com.example.playconsign;

public class Product {
    String productName;
    int productPrice;
    String productCategory;
    String productDesc;
    String productImage;
    String productCondition;
    String productSeller;
    String productSellerAddress;

    public Product(String productName, int productPrice, String productCategory, String productDesc, String productImage, String productCondition, String productSeller, String productSellerAddress) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.productCategory = productCategory;
        this.productDesc = productDesc;
        this.productImage = productImage;
        this.productCondition = productCondition;
        this.productSeller = productSeller;
        this.productSellerAddress = productSellerAddress;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductCondition() {
        return productCondition;
    }

    public void setProductCondition(String productCondition) {
        this.productCondition = productCondition;
    }

    public String getProductSeller() {
        return productSeller;
    }

    public void setProductSeller(String productSeller) {
        this.productSeller = productSeller;
    }

    public String getProductSellerAddress() {
        return productSellerAddress;
    }

    public void setProductSellerAddress(String productSellerAddress) {
        this.productSellerAddress = productSellerAddress;
    }
}
