package com.example.coursework.jwt;

public class UsernameAndPasswordAuthRequest {
    private String username;
    private String password;
    public UsernameAndPasswordAuthRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
    public UsernameAndPasswordAuthRequest() {

    }
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
