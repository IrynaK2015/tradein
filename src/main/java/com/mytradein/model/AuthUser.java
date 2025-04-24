package com.mytradein.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class AuthUser {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, length=20, unique=true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length=10)
    private String role;

    @Column(nullable = false, unique=true)
    private String email;

    @Column(length=128, nullable = false)
    private String fullname;

    @OneToMany(mappedBy = "authuser", cascade = CascadeType.ALL)
    final private List<Offer> offerList = new ArrayList<>();

    public AuthUser() {}

    public AuthUser(String username, String password, String email, String role, String fullname) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.fullname = fullname;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void addAOffer(Offer offer) {
        if (!offerList.contains(offer)) {
            offer.setAuthuser(this);
            offerList.add(offer);
        }
    }

    public List<Offer> getOfferList() {
        return offerList;
    }

    public void setFullName(String fullName) {
        this.fullname = fullName;
    }

    public String getFullName() {
        return fullname;
    }
}
