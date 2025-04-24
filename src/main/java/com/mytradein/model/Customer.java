package com.mytradein.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Customer {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, length=128)
    private String firstname;

    @Column(nullable = false, length=128)
    private String lastname;

    @Column(nullable = false, unique=true)
    private String email;

    @Column(nullable = false)
    private Long phone;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    final private List<Offer> offerList = new ArrayList<>();

    public Customer() {}

    public Customer(String firstname, String lastname, String email, Long phone) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.phone = phone;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getPhone() {
        return phone;
    }

    public void setPhone(Long phone) {
        this.phone = phone;
    }
}
