package com.mytradein.repository;

import com.mytradein.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Customer c WHERE c.email = :email")
    boolean existsByEmail(String email);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Customer c WHERE c.phone = :phone")
    boolean existsByPhone(Long phone);

    @Query("SELECT c FROM Customer c WHERE c.email = :email")
    Customer findByEmail(String email);

    @Query("SELECT c FROM Customer c WHERE c.phone = :phone")
    Customer findByPhone(Long phone);
}
