package com.mytradein.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import com.mytradein.model.AuthUser;
import com.mytradein.repository.AuthUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Sort;

public class AuthUserServiceTest {

    @Mock
    private AuthUserRepository authUserRepository;

    @InjectMocks
    private AuthUserService authUserService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindById_ReturnsUser() {
        AuthUser user = new AuthUser();
        when(authUserRepository.findById(1L)).thenReturn(Optional.of(user));

        AuthUser result = authUserService.findById(1L);

        assertEquals(user, result);
    }

    @Test
    public void testFindById_ReturnsNull() {
        when(authUserRepository.findById(1L)).thenReturn(Optional.empty());

        AuthUser result = authUserService.findById(1L);

        assertNull(result);
    }

    @Test
    public void testFindByLogin() {
        AuthUser user = new AuthUser();
        when(authUserRepository.findByLogin("john")).thenReturn(user);

        AuthUser result = authUserService.findByLogin("john");

        assertEquals(user, result);
    }

    @Test
    public void testFindByEmail() {
        AuthUser user = new AuthUser();
        when(authUserRepository.findByEmail("john@example.com")).thenReturn(user);

        AuthUser result = authUserService.findByEmail("john@example.com");

        assertEquals(user, result);
    }

    @Test
    public void testIsAdminExists() {
        when(authUserRepository.existsAdmin("ADMIN")).thenReturn(true);

        boolean result = authUserService.isAdminExists();

        assertTrue(result);
    }

    @Test
    public void testSaveUser() {
        AuthUser user = new AuthUser();
        authUserService.saveUser(user);

        verify(authUserRepository, times(1)).save(user);
    }

    @Test
    public void testAddUser_UserAlreadyExists() {
        when(authUserRepository.existsByLogin("john")).thenReturn(true);

        boolean result = authUserService.addUser("john", "hash", "john@example.com", "USER", "John Doe");

        assertFalse(result);
        verify(authUserRepository, never()).save(any());
    }

    @Test
    public void testAddUser_EmailAlreadyExists() {
        when(authUserRepository.existsByLogin("john")).thenReturn(false);
        when(authUserRepository.existsByEmail("john@example.com")).thenReturn(true);

        boolean result = authUserService.addUser("john", "hash", "john@example.com", "USER", "John Doe");

        assertFalse(result);
        verify(authUserRepository, never()).save(any());
    }

    @Test
    public void testAddUser_Successful() {
        when(authUserRepository.existsByLogin("john")).thenReturn(false);
        when(authUserRepository.existsByEmail("john@example.com")).thenReturn(false);

        boolean result = authUserService.addUser("john", "hash", "john@example.com", "USER", "John Doe");

        assertTrue(result);
        verify(authUserRepository, times(1)).save(any(AuthUser.class));
    }

    @Test
    public void testRemoveUser() {
        AuthUser user = new AuthUser();
        authUserService.removeUser(user);

        verify(authUserRepository, times(1)).delete(user);
    }

    @Test
    public void testGetAllUsers() {
        List<AuthUser> users = Arrays.asList(new AuthUser(), new AuthUser());
        when(authUserRepository.findAll(Sort.by(Sort.Direction.ASC, "username"))).thenReturn(users);

        List<AuthUser> result = authUserService.getAllUsers();

        assertEquals(users.size(), result.size());
    }
}
