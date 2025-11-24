
package com.example.coursapp.network.request;

public class SignUpRequest {
    public String username;
    public String password;
    public String phone;

    public SignUpRequest(String username, String password, String phone) {
        this.username = username;
        this.password = password;
        this.phone = phone;
    }
}