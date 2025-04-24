package com.mytradein.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.Timestamp;
import java.time.*;
import java.util.*;

import com.mytradein.model.Authtoken;
import com.mytradein.model.TokenTypes;
import com.mytradein.repository.AuthtokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

public class AuthtokenServiceTest {

    @Mock
    private AuthtokenRepository authtokenRepository;

    @InjectMocks
    private AuthtokenService authtokenService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testIsApiTokenConfigured() {
        when(authtokenRepository.isApiTokenConfigured()).thenReturn(true);

        boolean result = authtokenService.isApiTokenConfigured();

        assertTrue(result);
        verify(authtokenRepository).isApiTokenConfigured();
    }

    @Test
    void testSaveAuthToken() {
        Authtoken token = new Authtoken("user");
        authtokenService.saveAuthToken(token);

        verify(authtokenRepository).save(token);
    }

    @Test
    void testCreateAuthToken() {
        Authtoken token = authtokenService.createAuthToken("login");

        assertNotNull(token);
        assertEquals("login", token.getLogin());
        assertNotNull(token.getTokenValue());
        assertNotNull(token.getExpired());

        verify(authtokenRepository).save(token);
    }

    @Test
    void testGetActiveAuthToken_ValidToken() {
        Authtoken token = mockActiveToken();
        when(authtokenRepository.getActiveTokenByType("user", TokenTypes.PASSWORD.name())).thenReturn(token);

        Authtoken result = authtokenService.getActiveAuthToken("user");

        assertEquals(token, result);
    }

    @Test
    void testGetActiveAuthToken_ExpiredToken() {
        Authtoken expiredToken = mockExpiredToken();
        when(authtokenRepository.getActiveTokenByType("user", TokenTypes.PASSWORD.name())).thenReturn(expiredToken);

        Authtoken result = authtokenService.getActiveAuthToken("user");

        assertNull(result);
        assertFalse(expiredToken.isActive());
        verify(authtokenRepository).save(expiredToken);
    }

    @Test
    void testGetActiveTokenByValue_Valid() {
        Authtoken token = mockActiveToken();
        when(authtokenRepository.getActiveTokenByValue("abc")).thenReturn(token);

        Authtoken result = authtokenService.getActiveTokenByValue("abc");

        assertEquals(token, result);
    }

    @Test
    void testGetActiveTokenByValue_Expired() {
        Authtoken token = mockExpiredToken();
        when(authtokenRepository.getActiveTokenByValue("abc")).thenReturn(token);

        Authtoken result = authtokenService.getActiveTokenByValue("abc");

        assertNull(result);
        verify(authtokenRepository).save(token);
    }

    @Test
    void testDeactivateToken() {
        Authtoken token = new Authtoken("login");
        token.setActive(true);

        authtokenService.deactivateToken(token);

        assertFalse(token.isActive());
        verify(authtokenRepository).save(token);
    }

    @Test
    void testGetApiTokenCredentials_ValidPassword() {
        Authtoken token = new Authtoken("user");
        token.setPassword("secret");

        when(authtokenRepository.getApiTokenByLogin("user", TokenTypes.API.name())).thenReturn(token);

        Authtoken result = authtokenService.getApiTokenCredentials("user", "secret");

        assertEquals(token, result);
    }

    @Test
    void testGetApiTokenCredentials_InvalidPassword() {
        Authtoken token = new Authtoken("user");
        token.setPassword("secret");

        when(authtokenRepository.getApiTokenByLogin("user", TokenTypes.API.name())).thenReturn(token);

        Authtoken result = authtokenService.getApiTokenCredentials("user", "wrong");

        assertNull(result);
    }

    private Authtoken mockActiveToken() {
        Authtoken token = new Authtoken("user");
        token.setActive(true);
        token.setExpired(Timestamp.valueOf(LocalDateTime.now().plusMinutes(5)));
        return token;
    }

    private Authtoken mockExpiredToken() {
        Authtoken token = new Authtoken("user");
        token.setActive(true);
        token.setExpired(Timestamp.valueOf(LocalDateTime.now().minusMinutes(5)));
        return token;
    }
}