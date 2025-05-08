package com.mytradein.service;

import com.mytradein.model.Authtoken;
import org.springframework.stereotype.Service;


@Service
public interface AuthtokenService {

    boolean isApiTokenConfigured();

    void saveAuthToken(Authtoken authtoken);

    Authtoken createAuthToken(String authLogin);

    Authtoken getActiveAuthToken(String login);

    Authtoken getActiveTokenByValue(String tokenValue);

    void deactivateToken(Authtoken authToken);

    Authtoken getApiTokenCredentials (String login, String password);
}