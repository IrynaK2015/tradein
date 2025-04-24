package com.mytradein.dto;

public class AuthTokenRequest {
    private final String login;

    private final String password;

    public AuthTokenRequest(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
