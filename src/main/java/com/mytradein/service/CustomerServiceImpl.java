package com.mytradein.service;

import com.mytradein.model.Customer;
import com.mytradein.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    @Transactional
    public void addCustomer(Customer customer) {
        customerRepository.save(customer);
    }

    @Transactional
    public void saveCustomer(Customer customer) {
        customerRepository.save(customer);
    }

    @Transactional
    public void deleteCustomer(Customer custom) {
        customerRepository.delete(custom);
    }

    @Transactional(readOnly=true)
    public Page findAllCustomers(Pageable pageable) {
        return customerRepository.findAll(pageable);
    }

    @Transactional(readOnly=true)
    public Customer findCustomerById(Long id) {
        return customerRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly=true)
    public Customer findCustomerByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    @Transactional(readOnly=true)
    public Customer findCustomerByPhone(Long phone) {
        return customerRepository.findByPhone(phone);
    }

    @Transactional(readOnly=true)
    public boolean isCustomerExist(Customer customer) {
        return customerRepository.existsById(customer.getId());
    }
}
