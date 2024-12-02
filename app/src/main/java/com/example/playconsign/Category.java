package com.example.playconsign;

import java.util.Collection;

public class Category {
    private String categoryName;
    private int image;

    public Category(String categoryName, int image) {
        this.categoryName = categoryName;
        this.image = image;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

}
