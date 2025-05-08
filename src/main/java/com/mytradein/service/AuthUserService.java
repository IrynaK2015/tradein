package com.mytradein.service;

import com.mytradein.model.AuthUser;

import java.util.List;


public interface AuthUserService {

    AuthUser findById(Long id);

    AuthUser findByLogin(String login);

    AuthUser findByEmail(String email);

    boolean isAdminExists();

    void saveUser(AuthUser user);

    boolean addUser(String username, String passHash, String email, String role, String fullname);

    void removeUser(AuthUser user);

    List<AuthUser> getAllUsers();
}