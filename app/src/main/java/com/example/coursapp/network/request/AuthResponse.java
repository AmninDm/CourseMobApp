package com.example.coursapp.network.request;

public class AuthResponse {
    public String access_token;
    public String refresh_token;
    public String token_type;
    public long expires_in;
    public User user;

    public static class User {
        public String id;
        public String email;
        public String aud;
        public String role;
    }
}