package com.mytradein.model;

import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;

public class Seller {

    private final User user;

    private final AuthUser authUser;

    public Seller(User user, AuthUser authUser) {
        this.user = user;
        this.authUser = authUser;
    }

    public boolean isSupervisor() {
        return new ArrayList<>(user.getAuthorities()).stream()
                .anyMatch(r -> r.getAuthority().equals(AuthRoles.ADMIN.toString()));
    }

    public boolean isManager() {
        return new ArrayList<>(user.getAuthorities()).stream()
                .anyMatch(r -> r.getAuthority().equals(AuthRoles.USER.toString()));
    }

    public AuthUser getAuthUser() {
        return authUser;
    }

    public User getUser() {
        return user;
    }

    public Long getSellerId() {
        return authUser.getId();
    }
}
