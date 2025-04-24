package com.mytradein.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.Date;

@Entity
public class Authtoken {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name="token_type", length=10, nullable = false)
    private String tokenType;

    @Column(nullable = false, length=20)
    private String login;

    @Column()
    private String password;

    @Column(name="token_value")
    private String tokenValue;

    @Column(nullable = false)
    private Date expired;

    @Column(columnDefinition = "boolean default true")
    private boolean active = true;

    public Authtoken() {}

    public Authtoken(String login) {
        this.login = login;
        this.tokenType = TokenTypes.PASSWORD.name();
    }

    public Authtoken(String tokenType, String login, Date expired) {
        this.tokenType = tokenType;
        this.login = login;
        this.expired = expired;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTokenValue() {
        return tokenValue;
    }

    public void setTokenValue(String tokenValue) {
        this.tokenValue = tokenValue;
    }

    public Date getExpired() {
        return expired;
    }

    public void setExpired(Date expired) {
        this.expired = expired;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
