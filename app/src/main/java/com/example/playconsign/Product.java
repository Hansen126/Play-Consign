package com.example.playconsign;

public class Product {
    String productName;
    int productPrice;
    String productCategory;
    String productDesc;
    String productImage;
    String productCondition;
    String productSellerUID;

    public Product() {
    }

    public Product(String productName, int productPrice, String productCategory, String productCondition,
                   String productDesc, String productImage, String productSellerUID) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.productCategory = productCategory;
        this.productCondition = productCondition;
        this.productDesc = productDesc;
        this.productImage = productImage;
        this.productSellerUID = productSellerUID;
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

    public String getProductSellerUID() {
        return productSellerUID;
    }

    public void setProductSellerUID(String productSellerUID) {
        this.productSellerUID = productSellerUID;
    }
}

