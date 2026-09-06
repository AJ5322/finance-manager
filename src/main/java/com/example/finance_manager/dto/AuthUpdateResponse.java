package com.example.finance_manager.dto;

public class AuthUpdateResponse {

    private UserResponse user;
    private String token;

    public AuthUpdateResponse() {
    }

    public AuthUpdateResponse(UserResponse user, String token) {
        this.user = user;
        this.token = token;
    }

    public UserResponse getUser() {
        return user;
    }

    public void setUser(UserResponse user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}