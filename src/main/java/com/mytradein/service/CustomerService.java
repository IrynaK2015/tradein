package com.mytradein.service;

import com.mytradein.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface CustomerService {

    void addCustomer(Customer customer);

    void saveCustomer(Customer customer);

    void deleteCustomer(Customer customer);

    Page findAllCustomers(Pageable pageable);

    Customer findCustomerById(Long id);

    Customer findCustomerByEmail(String email);

    Customer findCustomerByPhone(Long phone);

    boolean isCustomerExist(Customer customer);
}
