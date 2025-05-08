package com.mytradein.service;

import com.mytradein.model.AuthRoles;
import com.mytradein.model.AuthUser;
import com.mytradein.repository.AuthUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class AuthUserServiceImpl implements AuthUserService {

    @Autowired
    private AuthUserRepository authUserRepository;

    @Transactional(readOnly = true)
    public AuthUser findById(Long id) {
        return authUserRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public AuthUser findByLogin(String login) {
        return authUserRepository.findByLogin(login);
    }

    @Transactional(readOnly = true)
    public AuthUser findByEmail(String email) {
        return authUserRepository.findByEmail(email);
    }

    @Transactional(readOnly = true)
    public boolean isAdminExists() {
        return authUserRepository.existsAdmin(String.valueOf(AuthRoles.ADMIN));
    }

    @Transactional
    public void saveUser(AuthUser user) {
        authUserRepository.save(user);
    }

    @Transactional
    public boolean addUser(String username,
                           String passHash,
                           String email,
                           String role,
                           String fullname) {
        if (authUserRepository.existsByLogin(username) || authUserRepository.existsByEmail(email))
            return false;

        AuthUser user = new AuthUser(username, passHash, email, role, fullname);
        authUserRepository.save(user);

        return true;
    }

    @Transactional
    public void removeUser(AuthUser user) {
        authUserRepository.delete(user);
    }

    @Transactional(readOnly = true)
    public List<AuthUser> getAllUsers() {
        return authUserRepository.findAll(Sort.by(Sort.Direction.ASC, "fullname"));
    }
}