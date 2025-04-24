package com.mytradein.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

import com.mytradein.model.Customer;
import com.mytradein.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;

public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddCustomer() {
        Customer customer = new Customer();
        customerService.addCustomer(customer);
        verify(customerRepository).save(customer);
    }

    @Test
    public void testSaveCustomer() {
        Customer customer = new Customer();
        customerService.saveCustomer(customer);
        verify(customerRepository).save(customer);
    }

    @Test
    public void testDeleteCustomer() {
        Customer customer = new Customer();
        customerService.deleteCustomer(customer);
        verify(customerRepository).delete(customer);
    }

    @Test
    public void testFindAllCustomers() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Customer> mockPage = new PageImpl<>(List.of(new Customer(), new Customer()));
        when(customerRepository.findAll(pageable)).thenReturn(mockPage);

        Page result = customerService.findAllCustomers(pageable);

        assertEquals(2, result.getTotalElements());
    }

    @Test
    public void testFindCustomerById_Found() {
        Customer customer = new Customer();
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        Customer result = customerService.findCustomerById(1L);

        assertEquals(customer, result);
    }

    @Test
    public void testFindCustomerById_NotFound() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        Customer result = customerService.findCustomerById(1L);

        assertNull(result);
    }

    @Test
    public void testFindCustomerByEmail() {
        Customer customer = new Customer();
        when(customerRepository.findByEmail("test@example.com")).thenReturn(customer);

        Customer result = customerService.findCustomerByEmail("test@example.com");

        assertEquals(customer, result);
    }

    @Test
    public void testFindCustomerByPhone() {
        Customer customer = new Customer();
        when(customerRepository.findByPhone(1234567890L)).thenReturn(customer);

        Customer result = customerService.findCustomerByPhone(1234567890L);

        assertEquals(customer, result);
    }

    @Test
    public void testIsCustomerExist_True() {
        Customer customer = new Customer();
        customer.setId(1L);
        when(customerRepository.existsById(1L)).thenReturn(true);

        boolean exists = customerService.isCustomerExist(customer);

        assertTrue(exists);
    }

    @Test
    public void testIsCustomerExist_False() {
        Customer customer = new Customer();
        customer.setId(1L);
        when(customerRepository.existsById(1L)).thenReturn(false);

        boolean exists = customerService.isCustomerExist(customer);

        assertFalse(exists);
    }
}