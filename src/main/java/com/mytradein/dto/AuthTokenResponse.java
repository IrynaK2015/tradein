package com.mytradein.dto;

import com.mytradein.model.Authtoken;

public class AuthTokenResponse {

   private final String authToken;

    public AuthTokenResponse(Authtoken authtoken) {
        this.authToken = authtoken.getTokenValue();
    }

    public String getAuthToken() {
        return authToken;
    }
}
