package com.example.playconsign;

public class SnapResponse {
    String snapToken;

    public SnapResponse(String snapToken) {
        this.snapToken = snapToken;
    }

    public String getSnapToken() {
        return snapToken;
    }

    public void setSnapToken(String snapToken) {
        this.snapToken = snapToken;
    }
}
