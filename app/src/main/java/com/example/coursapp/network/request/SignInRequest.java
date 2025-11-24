package com.example.coursapp.network.request;

public class SignInRequest {
    public String username;
    public String password;

    public SignInRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}