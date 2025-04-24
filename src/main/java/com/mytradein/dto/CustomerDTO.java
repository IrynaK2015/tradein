package com.mytradein.dto;

import com.mytradein.model.Customer;
import jakarta.validation.constraints.*;

public class CustomerDTO {

    private String id;

    @NotEmpty(message = "First name cannot be null")
    private String firstname;

    @NotEmpty(message = "Last name cannot be null")
    private String lastname;

    @NotEmpty(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @Pattern(regexp = "^38[0-9]{10}+$", message = "expected format 38XXXXXXXXXX")
    private String phone;

    public CustomerDTO() {}

    public CustomerDTO(Customer customer) {
        this.id = customer.getId().toString();
        this.firstname = customer.getFirstname();
        this.lastname = customer.getLastname();
        this.email = customer.getEmail();
        this.phone = customer.getPhone().toString();
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Customer getCustomer() {
        return new Customer(
            firstname, lastname, email, Long.parseLong(phone)
        );
    }
}
