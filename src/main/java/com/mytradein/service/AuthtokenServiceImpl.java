package com.mytradein.service;

import com.mytradein.model.Authtoken;
import com.mytradein.model.TokenTypes;
import com.mytradein.repository.AuthtokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;


@Service
public class AuthtokenServiceImpl implements AuthtokenService {

    @Autowired
    private AuthtokenRepository authtokenRepository;


    @Transactional(readOnly = true)
    public boolean isApiTokenConfigured() {
        return authtokenRepository.isApiTokenConfigured();
    }

    @Transactional
    public void saveAuthToken(Authtoken authtoken) {
        authtokenRepository.save(authtoken);
    }

    public Authtoken createAuthToken(String authLogin) {
        Authtoken authtoken = new Authtoken(authLogin);
        authtoken.setTokenValue(generateToken());
        LocalDateTime expired = LocalDateTime.now().plusMinutes(Constants.TOKEN_VALIDITY_MIN);
        authtoken.setExpired(java.sql.Timestamp.valueOf(expired));
        authtokenRepository.save(authtoken);

        return authtoken;
    }

    @Transactional
    public Authtoken getActiveAuthToken(String login) {
        return checkTokenValidity(
            authtokenRepository.getActiveTokenByType(login, TokenTypes.PASSWORD.name())
        );
    }

    @Transactional()
    public Authtoken getActiveTokenByValue(String tokenValue) {
        return checkTokenValidity(
            authtokenRepository.getActiveTokenByValue(tokenValue)
        );
    }

    public void deactivateToken(Authtoken authToken) {
        authToken.setActive(false);
        authtokenRepository.save(authToken);
    }

    public Authtoken getApiTokenCredentials (String login, String password) {
        Authtoken authToken = authtokenRepository.getApiTokenByLogin(login, TokenTypes.API.name());
        if (Objects.nonNull(authToken) && authToken.getPassword().equals(password)) {
            return authToken;
        }

        return null;
    }

    private Authtoken checkTokenValidity(Authtoken authToken) {
        if (!Objects.isNull(authToken)) {
            if (isTokenActive(authToken.getExpired())) {
                return authToken;
            }
            authToken.setActive(false);
            authtokenRepository.save(authToken);
        }

        return null;
    }

    private boolean isTokenActive(Date expiredDate) {
        LocalDateTime expiredDateTime = expiredDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        return LocalDateTime.now().isBefore(expiredDateTime);
    }

    private String generateToken() {
        return Utilities.encodeSha256(UUID.randomUUID().toString());
    }
}